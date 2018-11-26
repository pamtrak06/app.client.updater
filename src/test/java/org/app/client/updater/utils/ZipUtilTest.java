package org.app.client.updater.utils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

import org.app.client.updater.mock.MockUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class ZipUtilTest {
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
	public void testUnZipIt() {
		String zipFilename = "src/test/resources/server/mock/App1V3.1.zip";
		ZipFile unZip = null;
		try {
			unZip = ZipUtil.unZip(zipFilename, TEST_OUTPUT_PATH);
			Assert.assertTrue("Unzip file: " + zipFilename, unZip.isValidZipFile());
		} catch (ZipException e) {
			Assert.assertNull("Unzip file: " + zipFilename, null);
		}
	}

}
