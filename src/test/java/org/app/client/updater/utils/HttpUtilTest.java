package org.app.client.updater.utils;

import java.io.File;
import java.io.IOException;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.mock.MockClient;
import org.app.client.updater.mock.MockServer;
import org.app.client.updater.mock.MockUtil;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HttpUtilTest extends MockServer {

	private static String TEST_OUTPUT_PATH;

	@Before
	public void setupServerNominal() {
		startServer(MockServer.APPNAME1);
		TEST_OUTPUT_PATH = MockUtil.setupPathTmp();
	}

	@After
	public void tearDown() throws Exception {
		MockUtil.tearDownPathServer();
	}

	@Test
	public void testReadRemoteJson() {

		// Read version package json file
		final String remoteAppConfig = getHostUrl() + getAppConfigName();
		try {
			String jsonContent = JSonUtil.readRemoteJson(remoteAppConfig, AppVersion.class)
					.toString();
			Assert.assertNotNull("Read remote json (" + remoteAppConfig + ")", jsonContent);
		} catch (IOException e) {
			Assert.assertNotNull(
					"Read remote json (" + remoteAppConfig + "), cause: " + e.getMessage(), null);
		}

		// Read config app json file
		final String remoteAppPackage1 = getHostUrl() + getAppPackageName1();
		try {
			String jsonContent = JSonUtil.readRemoteJson(remoteAppPackage1 + VERSION_EXT,
					AppVersion.class).toString();
			Assert.assertNotNull("Read remote json (" + remoteAppPackage1 + VERSION_EXT + ")",
					jsonContent);
		} catch (IOException e) {
			Assert.assertNotNull("Read remote json (" + remoteAppPackage1 + VERSION_EXT + "), cause: " + e.getMessage(), null);
		}

	}

	@Test
	public void testReadRemoteJsonWithProxyNominal() {
		// setupProxy();
		//
		// HttpUtil.configureProxySettings(PROXY_HOST, PROXY_PORT, PROXY_USER,
		// PROXY_PWD);
		//
		// // Read version package json file
		// try {
		// String jsonContent = JSonUtil.readRemoteJson(getHostUrl() +
		// APP_CONFIG_NAME, AppVersion.class).toString();
		// Assert.assertNotNull("Read remote json (" + getHostUrl() +
		// APP_CONFIG_NAME + ")", jsonContent);
		// } catch (IOException e) {
		// Assert.assertNotNull("Read remote json (" + getHostUrl() +
		// APP_CONFIG_NAME + "), cause: " + e.getMessage(), null);
		// }
	}

	@Test
	public void testReadRemoteJsonWithProxyFailed() {
		// setupProxy();
		//
		// // HttpUtil.configureProxySettings("proxy.host.org", "2222", "user",
		// // "P@ssW0rd");
		//
		// // Read version package json file
		// try {
		// String jsonContent = JSonUtil.readRemoteJson(getHostUrl() +
		// APP_CONFIG_NAME, AppVersion.class).toString();
		// Assert.assertNotNull("Read remote json (" + getHostUrl() +
		// APP_CONFIG_NAME + ")", jsonContent);
		// } catch (IOException e) {
		// Assert.assertNotNull("Read remote json (" + getHostUrl() +
		// APP_CONFIG_NAME + "), cause: " + e.getMessage(), null);
		// }
	}

	@Test
	public void testWriteRemoteJson() {
		String id = "id-pc-001";
		String updateStatusName = MockClient.APPNAME + "_V3.2_" + id + JSonUtil.EXTENSION;
		File file = new File(MockClient.LOCAL_CLIENT_PATH + FileUtil.endPath(MockClient.APPNAME) + updateStatusName);
		try {
			boolean writeRemoteJson = JSonUtil.writeRemoteJson(file.getPath(), getHostUrl());
			Assert.assertTrue("Write remote json (" + getHostUrl() + updateStatusName + ")", writeRemoteJson);
		} catch (IOException e) {
			Assert.assertNotNull(
					"Write remote json (" + getHostUrl() + updateStatusName + "), cause: " + e.getMessage(), null);
		}
	}

	@Test
	public void testDownloadZip() {
		ZipFile zipFile = null;
		String glisFilename = null;

		// Download package glis file
		try {
			glisFilename = HttpUtil.downloadZip(getHostUrl() + getAppPackageName1() + ARCHIVE_EXT, TEST_OUTPUT_PATH)
					.toString();
			Assert.assertNotNull("remote archive (" + getHostUrl() + getAppConfigName() + ") download error",
					glisFilename);
			try {
				zipFile = new ZipFile(glisFilename);
				Assert.assertNotNull("downloaded archive (" + glisFilename + ") is not valid", glisFilename);
				Assert.assertTrue("downloaded archive (" + glisFilename + ") is not valid", zipFile.isValidZipFile());
			} catch (ZipException e) {
				Assert.assertNotNull("downloaded archive (" + glisFilename + ") is not valid, cause: "
						+ e.toString(), null);
			}
		} catch (Exception e) {
			Assert.assertNotNull("remote archive (" + getHostUrl() + getAppPackageName1() + ARCHIVE_EXT
					+ ") download error, cause: " + e.toString(), null);
		}
	}

}
