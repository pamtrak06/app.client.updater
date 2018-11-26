package org.app.client.updater;

import java.io.File;
import java.io.IOException;

import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.mock.MockClient;
import org.app.client.updater.mock.MockServer;
import org.app.client.updater.mock.MockUtil;
import org.app.client.updater.mock.MockVersion;
import org.app.client.updater.utils.FileUtil;
import org.app.client.updater.utils.JSonUtil;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UpdateStatusTest extends MockServer {

	private static String TEST_OUTPUT_PATH;
	private static String id;

	@BeforeClass
	public static void setUp() throws Exception {
		id = "id-pc-001";
		TEST_OUTPUT_PATH = MockUtil.setupPathServer();
	}

	@Before
	public void setUpForTest() throws Exception {
		startServer(MockServer.APPNAME1);
	}

	@AfterClass
	public static void tearDown() throws Exception {
		MockUtil.tearDownPathServer();
	}

	@After
	public void tearDownTest() throws Exception {
		// super.tearDown();
	}

	@Test
	public void testWrite() {
		UpdaterNotifier updateStatus = new UpdaterNotifier(id, MockClient.APPNAME, "V3.1", "OK");
		updateStatus.setAppVersion(MockVersion.createAppversionV32());

		File file = new File(TEST_OUTPUT_PATH + MockClient.APPNAME + "_V3.2_" + id + JSonUtil.EXTENSION);
		try {
			JSonUtil.writeJson(file, updateStatus, UpdaterNotifier.class);
		} catch (IOException e) {
			Assert.assertTrue("Writing update status file: " + file.getPath() + " failed, cause: " + e.getMessage(), false);
		}

	}

	// TODO remote writing
	// @Test
	// public void testWriteRemote() {
	// File file = new File(MockServer.HOST_URL + MockClient.APPNAME + "_V3.1_"
	// + id + JSonUtil.EXTENSION);
	// try {
	// String jsonContent = JSonUtil.readRemoteJson(HOST_URL + APP_CONFIG_NAME,
	// AppVersion.class).toString();
	// Assert.assertNotNull("Read remote json (" + HOST_URL + APP_CONFIG_NAME +
	// ")", jsonContent);
	// } catch (IOException e) {
	// Assert.assertNotNull("Read remote json (" + HOST_URL + APP_CONFIG_NAME +
	// "), cause: " + e.getMessage(), null);
	// }
	// }

	@Test
	public void testRead() {
		File file = new File(MockClient.LOCAL_CLIENT_PATH + FileUtil.endPath(MockClient.APPNAME) + MockClient.APPNAME + "_V3.2_" + id
				+ JSonUtil.EXTENSION);
		try {
			UpdaterNotifier updateStatus = (UpdaterNotifier) JSonUtil.readJson(file, UpdaterNotifier.class);
			Assert.assertNotNull("Read update status file (" + getFileConfig() + ")", updateStatus);
		} catch (IOException e) {
			Assert.assertTrue("Reading update status file, cause: " + e.getMessage(), false);
		}
	}

	@Test
	public void testRemoteWrite() {
		startServer(MockServer.APPNAME1);
		String updateStatusName = MockClient.APPNAME + "_V3.2_" + id + JSonUtil.EXTENSION;
		File file = new File(MockClient.LOCAL_CLIENT_PATH + FileUtil.endPath(MockClient.APPNAME) + updateStatusName);
		try {
			boolean writeRemoteJson = JSonUtil.writeRemoteJson(file.getPath(), getHostUrl() + updateStatusName);
			Assert.assertTrue("Write remote json (" + getHostUrl() + updateStatusName + ")", writeRemoteJson);
		} catch (IOException e) {
			Assert.assertNotNull("Write remote json (" + getHostUrl() + updateStatusName + "), cause: " + e.getMessage(), null);
		}
	}

}
