package org.app.client.updater.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.app.client.updater.exception.AcuException;

public final class HttpUtil {

	private static Logger LOG = Logger.getLogger(HttpUtil.class);

	private static final int TIMEOUT = 60000;

	public static String getRemoteJsonContent(final String url, final int timeout) throws IOException {
		URL u = new URL(url);
		HttpURLConnection c = (HttpURLConnection) u.openConnection();
		c.setRequestMethod("GET");
		c.setRequestProperty("Content-length", "0");
		c.setRequestProperty("Accept", "application/json");
		c.setRequestProperty("Authorization", "application/json");
		c.setUseCaches(false);
		c.setAllowUserInteraction(false);
		c.setConnectTimeout(timeout);
		c.setReadTimeout(timeout);
		c.connect();
		int status = c.getResponseCode();

		switch (status) {
		case 200:
		case 201:
		case 202:
			BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line + "\n");
			}
			br.close();
			return sb.toString();
		}

		return c.getResponseMessage();
	}

	public static String downloadZip(final String remotePathData, final String outputPath) throws IOException {
		URL u = new URL(remotePathData);
		URLConnection uc = u.openConnection();
		uc.setRequestProperty("Accept", "application/x-zip-compressed");
		uc.setRequestProperty("Authorization", "application/x-zip-compressed");
		String contentType = uc.getContentType();
		int contentLength = uc.getContentLength();
		if (contentType.startsWith("text/") || contentLength == -1) {
			throw new IOException("This is not a binary file.");
		}
		InputStream raw = uc.getInputStream();
		InputStream in = new BufferedInputStream(raw);
		byte[] data = new byte[contentLength];
		int bytesRead = 0;
		int offset = 0;
		while (offset < contentLength) {
			bytesRead = in.read(data, offset, data.length - offset);
			if (bytesRead == -1)
				break;
			offset += bytesRead;
		}
		in.close();

		if (offset != contentLength) {
			throw new IOException("Only read " + offset + " bytes; Expected " + contentLength + " bytes");
		}

		String filename = FileUtil.endPath(outputPath) + u.getFile().substring(u.getFile().lastIndexOf('/') + 1);
		FileOutputStream out = new FileOutputStream(filename);
		out.write(data);
		out.flush();
		out.close();

		return filename;

		// TODO interact with ui : progress bar
		// URL verifiedUrl = verifyUrl(remotePathData);
		// if (verifiedUrl != null) {
		// Download downloaded = new Download(verifiedUrl);
		// ProgressDialog renderer = new ProgressDialog(0, 100);
		// renderer.setStringPainted(true); // show progress text
		// downloaded.addObserver(renderer);
		// } else {
		// UiUtil.showError("Download archive", "Invalid Download URL: " +
		// remotePathData);
		// }
		// return null;
	}

	public static String getRemoteFileContent(String remotePath) throws IOException {
		return getRemoteJsonContent(remotePath, TIMEOUT);
	}

	public static boolean putRemoteJson(final String locaFile, final String remotePath) throws ClientProtocolException,
			IOException {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			HttpPost httppost = new HttpPost(remotePath);

			File file = new File(locaFile);

			InputStreamEntity reqEntity = new InputStreamEntity(new FileInputStream(file), -1);
			// reqEntity.setContentType("binary/octet-stream");
			reqEntity.setContentType("application/json");
			reqEntity.setChunked(true);
			// It may be more appropriate to use FileEntity class in this
			// particular
			// instance but we are using a more generic InputStreamEntity to
			// demonstrate
			// the capability to stream out data from any arbitrary source
			//
			// FileEntity entity = new FileEntity(file, "binary/octet-stream");

			httppost.setEntity(reqEntity);

			LOG.debug("executing request " + httppost.getRequestLine());
			CloseableHttpResponse response = httpclient.execute(httppost);
			try {
				HttpEntity resEntity = response.getEntity();

				StatusLine statusLine = response.getStatusLine();
				LOG.debug(statusLine);
				if (statusLine.getStatusCode() < 200 || statusLine.getStatusCode() > 205) {
					LOG.debug("Status: " + statusLine.getProtocolVersion() + " " + statusLine.getStatusCode() + " "
							+ statusLine.getReasonPhrase());
					return false;
				}
				if (resEntity != null) {
					LOG.debug("Response content length: " + resEntity.getContentLength());
					LOG.debug("Chunked?: " + resEntity.isChunked());
				}
				EntityUtils.consume(resEntity);
			}
//			catch (Exception e) {
//				throw new AcuException(Exception.class, HttpUtil.class, "Post file: " + locaFile + " to: " + remotePath);
//			}
			finally {
				response.close();
			}
		}  finally {
			httpclient.close();
		}
		return true;
	}

	// Verify download URL.
	public static URL verifyUrl(String url) {
		// Only allow HTTP URLs.
		if (!url.toLowerCase().startsWith("http://"))
			return null;

		// Verify format of URL.
		URL verifiedUrl = null;
		try {
			verifiedUrl = new URL(url);
		} catch (Exception e) {
			return null;
		}

		// Make sure URL specifies a file.
		if (verifiedUrl.getFile().length() < 2)
			return null;

		return verifiedUrl;
	}
}
