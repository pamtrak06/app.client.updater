package org.app.client.updater;

import org.app.client.updater.exception.AcuException;
import org.app.client.updater.mock.MockServer;
import org.app.client.updater.mock.MockUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UpdateServerVersionsTest {

	private static String TEST_OUTPUT_PATH;

	@BeforeClass
	public static void setUp() throws Exception {
		TEST_OUTPUT_PATH = MockUtil.setupPathServer();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		MockUtil.tearDownPathServer();
	}

	@Test
	public void testUpdate() {
		UpdaterVersions serverVersions = new UpdaterVersions(MockServer.APPNAME1, TEST_OUTPUT_PATH, TEST_OUTPUT_PATH);
		try {
			serverVersions.update();
		} catch (AcuException e) {
			Assert.assertNotNull("update, cause:" + e.getMessage(), null);
		}
	}

}
