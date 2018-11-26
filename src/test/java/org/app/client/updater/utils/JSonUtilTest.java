package org.app.client.updater.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.app.client.updater.UpdaterSchemas;
import org.app.client.updater.config.AppDeployment;
import org.app.client.updater.config.update.UpdaterConfig;
import org.app.client.updater.config.update.UpdaterConfigTest;
import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.config.version.AppVersionsTest;
import org.app.client.updater.mock.MockUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;

import eu.vahlas.json.schema.JSONSchema;
import eu.vahlas.json.schema.JSONSchemaProvider;
import eu.vahlas.json.schema.impl.JacksonSchemaProvider;

public class JSonUtilTest {
	private static UpdaterConfigTest updaterConfigTest;
	private static AppVersionsTest appVersionsTest;

	private static String TEST_OUTPUT_PATH;

	@BeforeClass
	public static void setUp() throws Exception {
		TEST_OUTPUT_PATH = MockUtil.setupPathTmp();
		updaterConfigTest = new UpdaterConfigTest();
		appVersionsTest = new AppVersionsTest();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		MockUtil.tearDownPathServer();
	}

	@Test
	public void testWrite() {
		updaterConfigTest.testReadUpdaterConfig();
		appVersionsTest.testAppVersionRead();
		appVersionsTest.testAppDeploymentsRead();
	}

	@Test
	public void testRead() {
		updaterConfigTest.testReadUpdaterConfig();
		appVersionsTest.testAppVersionWrite();
		appVersionsTest.testAppDeploymentWrite();
	}

	@Test
	public void testReadRemote() {
		// HttpUtilTest httpUtilTest = new HttpUtilTest();
		// httpUtilTest.setupServerNominal();
		// httpUtilTest.testReadRemoteJson();
	}

	public void testValidateSchema() {
		// TODO call for all aother json readed
	}

	@Test
	public void testGenerateSchema() {
		String appDeployment = UpdaterSchemas.buildPath(TEST_OUTPUT_PATH, AppDeployment.class);
		String appVersion = UpdaterSchemas.buildPath(TEST_OUTPUT_PATH, AppVersion.class);
		String updaterConfig = UpdaterSchemas.buildPath(TEST_OUTPUT_PATH, UpdaterConfig.class);

		try {
			UpdaterSchemas.generate(TEST_OUTPUT_PATH);
			Assert.assertTrue("Generate json schema: " + appDeployment + " in: " + TEST_OUTPUT_PATH + ", failed", FileUtil.exists(appDeployment));
			Assert.assertTrue("Generate json schema: " + appVersion + " in: " + TEST_OUTPUT_PATH + ", failed", FileUtil.exists(appVersion));
			Assert.assertTrue("Generate json schema: " + updaterConfig + " in: " + TEST_OUTPUT_PATH + ", failed", FileUtil.exists(updaterConfig));
		} catch (JsonProcessingException e) {
			Assert.assertTrue("Generate all json schemas failed, cause: " + e.getMessage(), false);
		} catch (IOException e) {
			Assert.assertTrue("Generate all json schemas failed, cause: " + e.getMessage(), false);
		}

	}

}
