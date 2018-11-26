package org.app.client.updater.config.update;

import java.io.File;
import java.io.IOException;

import org.app.client.updater.mock.MockConfig;
import org.app.client.updater.mock.MockUtil;
import org.app.client.updater.utils.JSonUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class UpdaterConfigTest {

	private static final String MOCK_PATH = "src/test/resources/server/mock/";
	private static String TEST_OUTPUT_PATH;

	@BeforeClass
	public static void setUp() throws Exception {
		TEST_OUTPUT_PATH = MockUtil.setupPathTmp();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		MockUtil.tearDownPathServer();
	}

	@Test
	public void testWriteUpdaterConfig() {
		UpdaterConfig config = MockConfig.createUpdaterConfig("app1");
		try {
			JSonUtil.writeJson(new File(TEST_OUTPUT_PATH + "app1-config.json"), config, UpdaterConfig.class);
		} catch (IOException e) {
			Assert.assertTrue("Writing updater config file, cause: " + e.getMessage(), false);
		}
	}

	@Test
	public void testReadUpdaterConfig() {
		readUpdaterConfig("app1-config-1.json", false, DeploymentStrategy.CRITERIA_LAST_VERSION.ENABLED);
		readUpdaterConfig("app1-config-2.json", false, DeploymentStrategy.CRITERIA_LAST_VERSION.UPPER_VERSION);
		readUpdaterConfig("app1-config-3.json", false, DeploymentStrategy.CRITERIA_LAST_VERSION.CHECKSUM);
		readUpdaterConfig("app1-config-4.json", true, DeploymentStrategy.CRITERIA_LAST_VERSION.ENABLED);
		readUpdaterConfig("app1-config-5.json", true, null);
		readUpdaterConfig("app1-config-6.json", false, DeploymentStrategy.CRITERIA_LAST_VERSION.ENABLED);
	}

	public void readUpdaterConfig(String filename, Boolean isForceInstallation, DeploymentStrategy.CRITERIA_LAST_VERSION criteria) {
		File configFile = new File(MOCK_PATH + filename);
		Assert.assertTrue("Exist config file (" + configFile + ")", configFile.exists());
		try {
			UpdaterConfig configRef = (UpdaterConfig) JSonUtil.readJson(configFile, UpdaterConfig.class);
			Assert.assertNotNull("Read updater config file (" + configFile + ")", configRef);
			Assert.assertEquals("Read updater config file (" + configFile + ")", isForceInstallation,
					Boolean.parseBoolean(configRef.getDeploymentStrategy().getForceInstallation()));
			Assert.assertEquals("Read updater config file (" + configFile + ")", configRef.getDeploymentStrategy().getCriteriaLatestVersion(),
					criteria);
		} catch (IOException e) {
			Assert.assertTrue("Reading updater config file, cause: " + e.getMessage(), false);
		}

	}

}
