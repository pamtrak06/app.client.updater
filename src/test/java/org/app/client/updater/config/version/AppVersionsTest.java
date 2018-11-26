package org.app.client.updater.config.version;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.app.client.updater.config.AppDeployment;
import org.app.client.updater.config.update.UpdaterConfig;
import org.app.client.updater.mock.MockUtil;
import org.app.client.updater.mock.MockVersion;
import org.app.client.updater.utils.JSonUtil;
import org.app.client.updater.utils.ResourceUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class AppVersionsTest {

	private static final File FILE_VERSIONS = new File("src/test/resources/server/app1/app1-versions.json");
	private static final File FILE_CONFIG = new File("src/test/resources/server/app1/app1-config.json");
	private static final File FILE_VERSION = new File("src/test/resources/server/app1/app1-archive-V3.3.json");

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
	public void testAppDeploymentWrite() {
		AppDeployment appDeployment = MockVersion.createAppVersions();

		try {
			File file = new File(TEST_OUTPUT_PATH + "app1-versions.json");
			JSonUtil.writeJson(file, appDeployment, AppDeployment.class);
			Assert.assertTrue("Writing app versions file: " + file.getPath() + ", doesn't exist", file.exists());
		} catch (IOException e) {
			Assert.assertTrue("Writing app versions file, cause: " + e.getMessage(), false);
		}

	}

	@Test
	public void testAppDeploymentsRead() {
		AppDeployment appversionsRef = null;
		try {
			appversionsRef = (AppDeployment) JSonUtil.readJson(FILE_VERSIONS, AppDeployment.class);
			Assert.assertNotNull("Reading app version file: " + FILE_VERSIONS.getPath(), appversionsRef);
		} catch (IOException e) {
			Assert.assertTrue("Reading app version file: " + FILE_VERSIONS.getPath() + ", cause: " + e.getMessage(), false);
		}
	}

	@Test
	public void testAppDeploymentsStringValidation() {
		testAppDeploymentsStringValidation(FILE_VERSIONS, AppDeployment.class);
		testAppDeploymentsStringValidation(FILE_CONFIG, UpdaterConfig.class);
		testAppDeploymentsStringValidation(FILE_VERSION, AppVersion.class);
	}

	@Test
	public void testAppDeploymentsStreamValidation() {
		testAppDeploymentsStreamValidation(FILE_VERSIONS, AppDeployment.class);
		testAppDeploymentsStreamValidation(FILE_CONFIG, UpdaterConfig.class);
		testAppDeploymentsStreamValidation(FILE_VERSION, AppVersion.class);
	}

	public void testAppDeploymentsStringValidation(final File json, final Class<?> schemaClazz) {
		// Json string validation
		Object object = null;
		try {
			object = JSonUtil.readJson(json, schemaClazz);
			Assert.assertNotNull("Reading json file: " + json.getPath(), object);
		} catch (IOException e) {
			Assert.assertTrue("Reading json file: " + json.getPath() + ", cause: " + e.getMessage(), false);
		}

		try {
			InputStream schemaStream = ResourceUtil.loadResource(schemaClazz.getSimpleName() + JSonUtil.EXTENSION);
			boolean validateSchema = JSonUtil.validateSchema(object.toString(), schemaStream);
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName(), validateSchema);
		} catch (JsonMappingException e) {
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName() + ", cause: " + e.toString(),
					false);
		} catch (JsonProcessingException e) {
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName() + ", cause: " + e.toString(),
					false);
		} catch (IOException e) {
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName() + ", cause: " + e.toString(),
					false);
		}
	}

	public void testAppDeploymentsStreamValidation(final File json, final Class<?> schemaClazz) {
		// Json schema Stream validation
		InputStreamReader stream = null;
		try {
			stream = JSonUtil.readJsonStream(json);
			Assert.assertNotNull("Reading json file: " + json.getPath(), stream);
		} catch (IOException e) {
			Assert.assertTrue("Reading json file: " + json.getPath() + ", cause: " + e.getMessage(), false);
		}

		try {
			InputStream schemaStream = ResourceUtil.loadResource(schemaClazz.getSimpleName() + JSonUtil.EXTENSION);
			boolean validateSchema = JSonUtil.validateSchema(stream, schemaStream);
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName(), validateSchema);
		} catch (JsonMappingException e) {
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName() + ", cause: " + e.toString(),
					false);
		} catch (JsonProcessingException e) {
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName() + ", cause: " + e.toString(),
					false);
		} catch (IOException e) {
			Assert.assertTrue("Validate json file: " + json.getPath() + " with schema: " + schemaClazz.getSimpleName() + ", cause: " + e.toString(),
					false);
		}
	}

	@Test
	public void testAppVersionWrite() {
		try {
			File file = new File(TEST_OUTPUT_PATH + FILE_VERSION.getName());
			JSonUtil.writeJson(file, MockVersion.createAppversionV31(), AppVersion.class);
			Assert.assertTrue("Writing app versions file: " + file.getPath() + ", doesn't exist", file.exists());
		} catch (IOException e) {
			Assert.assertTrue("Writing app versions file, cause: " + e.getMessage(), false);
		}

	}

	@Test
	public void testAppVersionRead() {
		AppVersion appversionsRef = null;
		try {
			appversionsRef = (AppVersion) JSonUtil.readJson(FILE_VERSION, AppVersion.class);
			Assert.assertNotNull("Reading app version file: " + FILE_VERSION.getPath(), appversionsRef);
		} catch (IOException e) {
			Assert.assertTrue("Reading app version file, cause: " + e.getMessage(), false);
		}

	}

}
