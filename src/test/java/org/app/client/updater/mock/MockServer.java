package org.app.client.updater.mock;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;

import java.io.File;
import java.io.IOException;

import org.apache.commons.codec.Charsets;
import org.junit.Assert;
import org.junit.Rule;

import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.common.io.Files;

public class MockServer {

	public final static String LOCAL_SERVER_PATH = "src/test/resources/server/";

	public final static String HOST = "localhost";
	public final static String PORT = "3333";

	public final static String PROXY_HOST = "proxy.host.org";
	public final static String PROXY_PORT = "2222";
	public final static String PROXY_USER = "user1";
	public final static String PROXY_PWD = "PassW0rd";

	public final static String VERSION_EXT = ".json";
	public final static String ARCHIVE_EXT = ".glis";

	public final static String APPNAME1 = "app1";
	public final static String APPNAME2 = "app2";

	private String appName;

	private String remotePath;

	private String hostUrl;

	private String appPackageName1;
	private String appPackageName2;
	private String appPackageName3;
	private String appPackageName4;

	private String appConfigName;
	private String updateStatusName;

	private File filePackageArchive1;
	private File filePackageVersion1;

	private File filePackageArchive2;
	private File filePackageVersion2;

	private File filePackageArchive3;
	private File filePackageVersion3;

	private File filePackageArchive4;
	private File filePackageVersion4;

	private File fileVersions;
	private File fileConfig;
	private File fileUpdateStatus;

	public static boolean isFolderEnabled = true;
	public static boolean isArchive = true;
	public static boolean isJsonVersions = true;
	public static boolean isJsonVersion = true;
	public static boolean isJSonConfig = true;
	public static boolean isJSonUpdate = true;

	@Rule
	public WireMockRule wireMockRule = new WireMockRule(Integer.parseInt(PORT));

	public void startServer(final String appName) {

		this.appName = appName;
		this.remotePath = "/" + appName + "-updater/";

		hostUrl = "http://" + HOST + ":" + PORT + remotePath;

		appPackageName1 = appName + "-archive-V3.1";
		appPackageName2 = appName + "-archive-V3.2";
		appPackageName3 = appName + "-archive-V3.3";
		appPackageName4 = appName + "-archive-V3.4";

		appConfigName = appName + "-config.json";
		updateStatusName = appName + "_V3.1_id-pc-001.json";

		filePackageArchive1 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName1 + ARCHIVE_EXT);
		filePackageVersion1 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName1 + VERSION_EXT);

		filePackageArchive2 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName2 + ARCHIVE_EXT);
		filePackageVersion2 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName2 + VERSION_EXT);

		filePackageArchive3 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName3 + ARCHIVE_EXT);
		filePackageVersion3 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName3 + VERSION_EXT);

		filePackageArchive4 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName4 + ARCHIVE_EXT);
		filePackageVersion4 = new File(LOCAL_SERVER_PATH + appName + "/" + appPackageName4 + VERSION_EXT);

		fileVersions = new File(LOCAL_SERVER_PATH + appName + "/" + appName + "-versions" + VERSION_EXT);
		fileConfig = new File(LOCAL_SERVER_PATH + appName + "/" + appConfigName);
		fileUpdateStatus = new File(LOCAL_SERVER_PATH + appName + "/" + updateStatusName);

		setupServer();
	}

	public void setupProxy() {
		// TODO setupProxy
		// "proxy.host.org", "2222", "user", "P@ssW0rd"

		stubFor(get(urlMatching(remotePath + ".*")).willReturn(
				aResponse().proxiedFrom("http://" + PROXY_USER + ":" + PROXY_PWD + "@" + PROXY_HOST + ":" + PROXY_PORT + "/" + remotePath)));
	}

	private void setupServer() {

		try {
			// Define mapping for folder
			if (isFolderEnabled) {
				stubFor(post(urlMatching(remotePath + ".*\\.json")).withHeader("Content-Type", equalTo("application/json"))
						.withHeader("Accept", equalTo("application/json"))
						.willReturn(aResponse().withStatus(200).withHeader("Content-Type", "application/json")));

				// stubFor(put(urlMatching(REMOTEPATH +
				// ".*\\.json")).withHeader("Content-Type",
				// equalTo("application/json"))
				// .withHeader("Accept", equalTo("application/json"))
				// .willReturn(aResponse().withStatus(200).withHeader("Content-Type",
				// "application/json")));
			}

			updateArchiveVersion(filePackageArchive1, filePackageVersion1);
			updateArchiveVersion(filePackageArchive2, filePackageVersion2);
			updateArchiveVersion(filePackageArchive3, filePackageVersion3);
			updateArchiveVersion(filePackageArchive4, filePackageVersion4);

			// Upload json version file to server
			if (isJsonVersions) {
				String url = remotePath + fileVersions.getName();
				String content = Files.toString(fileVersions, Charsets.UTF_8);
				stubFor(get(urlEqualTo(url)).withHeader("Accept", equalTo("application/json")).willReturn(
						aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(content)));
			}

			// Upload json config file to server
			if (isJSonConfig) {
				String url = remotePath + fileConfig.getName();
				String content = Files.toString(fileConfig, Charsets.UTF_8);
				stubFor(get(urlEqualTo(url)).withHeader("Accept", equalTo("application/json")).willReturn(
						aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(content)));
			}

			// Upload json update status file to server
			if (isJSonUpdate) {
				String url = remotePath + fileUpdateStatus.getName();
				String content = Files.toString(fileUpdateStatus, Charsets.UTF_8);
				stubFor(get(urlEqualTo(url)).withHeader("Accept", equalTo("application/json")).willReturn(
						aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(content)));
			}
		} catch (IOException e) {
			Assert.assertNotNull("Building and start mock server, cause: " + e.toString(), null);
		}

	}

	private void updateArchiveVersion(File archive, File version) throws IOException {
		// Upload archive file to server
		if (isArchive) {
			String url = remotePath + archive.getName();
			byte[] content = Files.toByteArray(archive);
			stubFor(get(urlEqualTo(url)).withHeader("Accept", equalTo("application/x-zip-compressed")).willReturn(
					aResponse().withStatus(200).withHeader("Content-Type", "application/x-zip-compressed").withBody(content)));
		}

		// Upload json version file to server
		if (isJsonVersion) {
			String url = remotePath + version.getName();
			String content = Files.toString(version, Charsets.UTF_8);
			stubFor(get(urlEqualTo(url)).withHeader("Accept", equalTo("application/json")).willReturn(
					aResponse().withStatus(200).withHeader("Content-Type", "application/json").withBody(content)));
		}
	}

	public String getAppName() {
		return appName;
	}

	public String getRemotePath() {
		return remotePath;
	}

	public String getAppPackageName1() {
		return appPackageName1;
	}

	public String getAppPackageName2() {
		return appPackageName2;
	}

	public String getAppPackageName3() {
		return appPackageName3;
	}

	public String getAppPackageName4() {
		return appPackageName4;
	}

	public String getAppConfigName() {
		return appConfigName;
	}

	public String getUpdateStatusName() {
		return updateStatusName;
	}

	public File getFilePackageArchive1() {
		return filePackageArchive1;
	}

	public File getFilePackageVersion1() {
		return filePackageVersion1;
	}

	public File getFilePackageArchive2() {
		return filePackageArchive2;
	}

	public File getFilePackageVersion2() {
		return filePackageVersion2;
	}

	public File getFilePackageArchive3() {
		return filePackageArchive3;
	}

	public File getFilePackageVersion3() {
		return filePackageVersion3;
	}

	public File getFilePackageArchive4() {
		return filePackageArchive4;
	}

	public File getFilePackageVersion4() {
		return filePackageVersion4;
	}

	public File getFileVersions() {
		return fileVersions;
	}

	public File getFileConfig() {
		return fileConfig;
	}

	public File getFileUpdateStatus() {
		return fileUpdateStatus;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	// @After
	// public void tearDown() {
	// // TODO stopServer
	// }

}
