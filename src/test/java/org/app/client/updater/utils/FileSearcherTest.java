package org.app.client.updater.utils;

import java.io.File;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileSearcherTest {

	private static String disk = null;

	@BeforeClass
	public static void setUp() throws Exception {
		if (LocalEnv.osName.toLowerCase().contains("win")) {
			disk = "d";
		} else if (LocalEnv.osName.toLowerCase().contains("mac") || LocalEnv.osName.toLowerCase().contains("lin")) {
			disk = "/";
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	@Test
	public void testSearchFileDegradated() {
		testSearchFile("", "", "", true, true);
		testSearchFile(null, "", "", true, true);
		testSearchFile(null, null, "", true, true);
		testSearchFile(null, null, null, true, true);
		testSearchFile(null, "", null, true, true);
		testSearchFile("", "", null, true, true);
		testSearchFile("", null, null, true, true);
		testSearchFile("", null, "src/test/resources/client", true, true);
		testSearchFile(null, "", "src/test/resources/client", true, true);
		testSearchFile(null, null, "src/test/resources/client", true, true);
		testSearchFile("", "", "src/test/resources/client", true, true);
		testSearchFile("", disk, "src/test/resources/client", true, true);
		testSearchFile("", "z", "src/test/resources/client", true, true);
		if (LocalEnv.osName.toLowerCase().contains("win")) {
			testSearchFile("app1-main-V(.*).jar", "", "./src/test/resources/client/app1WinShortcut", true, true);
		} else if (LocalEnv.osName.toLowerCase().contains("mac")) {
			// TODO TBD
		} else if (LocalEnv.osName.toLowerCase().contains("lin")) {
			// TODO TBD
		}

	}

	@Test
	public void testSearchFileNominal() {
		testSearchFile("app1-main-V(.*).jar", disk, "src/test/resources/client", false, false);
		testSearchFile("app1-main-V(.*).jar", null, "./src/test/resources/client", false, false);
		testSearchFile("app1-main-V(.*).jar", "", "./src/test/resources/client", false, false);
		//testSearchFile("app1-main-V(.*).jar", "", "", false, false);
	}

	private void testSearchFile(String regexFilename, String diskFilter, String pathFilter, boolean isExceptionExpected, boolean isEmptyResults) {
		List<File> foundedFiles = null;
		try {
			foundedFiles = FileSearcher.searchFile(regexFilename, diskFilter, pathFilter);
		} catch (Exception e) {
			if (isExceptionExpected) {
				Assert.assertNull("Unexpected exception: " + e.toString(), null);
				return;
			} else {
				Assert.assertNotNull(e.toString(), null);
			}
		}
		Assert.assertNotNull(foundedFiles);
		if (isEmptyResults) {
			Assert.assertTrue(foundedFiles.isEmpty());
		} else {
			Assert.assertFalse(foundedFiles.isEmpty());
		}
	}

}
