package org.app.client.updater.config.version;

import org.app.client.updater.mock.MockClient;
import org.app.client.updater.mock.MockVersion;
import org.app.client.updater.utils.FileUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class AppStructureTest {

	private static AppStructure APP_STRUCTURE;

	@BeforeClass
	public static void setUp() throws Exception {
		APP_STRUCTURE = MockVersion.createAppStructure(MockClient.LOCAL_CLIENT_PATH + FileUtil.endPath(MockClient.APPNAME) + "App1V3.1",
				MockClient.APPNAME, "759e1a30ca2b3bfe0c2f86e99360767a");
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	@Test
	public void testCheckContent() {
		Assert.assertTrue(APP_STRUCTURE.checkContent());
	}

	@Test
	public void testValidate() {
		Assert.assertTrue(APP_STRUCTURE.validate());
	}

}
