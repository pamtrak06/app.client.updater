package org.app.client.updater;

import org.app.client.updater.exception.AcuException;
import org.app.client.updater.mock.MockServer;
import org.app.client.updater.mock.MockUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppClientUpdaterTest extends MockServer {

	private static String TEST_OUTPUT_PATH_SERVER;
	private static String TEST_OUTPUT_PATH_CLIENT;

	@BeforeClass
	public static void setUp() throws Exception {
		TEST_OUTPUT_PATH_SERVER = MockUtil.setupPathServer();
		TEST_OUTPUT_PATH_CLIENT = MockUtil.setupPathClient();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		MockUtil.tearDownPathServer();
		MockUtil.tearDownPathClient();
	}

	@Test
	public void testUpdateAbort() {
		AppClientUpdater.initInstance();
		startServer(MockServer.APPNAME1);

		// More than one local version found, abort process
		AppClientUpdater appClientUpdater = AppClientUpdater.getInstance(getAppName(), getHostUrl());
		try {
			Assert.assertFalse(appClientUpdater.update());
		} catch (AcuException e) {
			Assert.assertNotNull("Update: " + e.getMessage(), null);
		}
	}

	@Test
	public void testUpdate() {
		AppClientUpdater.initInstance();
		startServer(MockServer.APPNAME2);

		AppClientUpdater appClientUpdater = AppClientUpdater.getInstance(getAppName(), getHostUrl(), false, false);
		try {
			Assert.assertTrue(appClientUpdater.update());
		} catch (AcuException e) {
			Assert.assertNotNull("Update: " + e.getMessage(), null);
		}
	}

}
