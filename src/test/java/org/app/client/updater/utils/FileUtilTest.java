package org.app.client.updater.utils;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.app.client.updater.mock.MockUtil;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class FileUtilTest {
	private static String TEST_OUTPUT_PATH;

	final String file = "src/test/resources/client/app1/app1_V3.2_id-pc-001.json";

	@BeforeClass
	public static void setUp() throws Exception {
		TEST_OUTPUT_PATH = MockUtil.setupPathTmp();
	}

	@AfterClass
	public static void tearDown() throws Exception {
		MockUtil.tearDownPathServer();
	}

	@Test
	public void testCopyFile() {
		boolean copyFile = FileUtil.copyFile(file, TEST_OUTPUT_PATH);
		Assert.assertTrue(copyFile);
		String name = new File(file).getName();
		File result = new File(TEST_OUTPUT_PATH + "/" + name);
		Assert.assertTrue(result.exists());
	}

	@Test
	public void testCopyFolder() {
		boolean copyFolder = FileUtil.copyFolder("src/test/resources/client/", TEST_OUTPUT_PATH);
		Assert.assertTrue(copyFolder);
		String name = new File("src/test/resources/client/").getName();
		File result = new File(TEST_OUTPUT_PATH + "/" + name);
		Assert.assertTrue(result.exists());
	}

	@Test
	public void testGetChecksumMd5() {
		try {
			String checksumMd5 = FileUtil.getChecksumMd5(new File(file));
			Assert.assertNotNull(checksumMd5);
		} catch (IOException e) {
			Assert.assertNotNull(e.toString(), null);
		}
	}

	@Test
	public void testGetChecksumSha1() {
		try {
			String checksumSha1 = FileUtil.getChecksumSha1(new File("src/test/resources/client/app1/app1_V3.2_id-pc-001.json"));
			Assert.assertNotNull(checksumSha1);
		} catch (IOException e) {
			Assert.assertNotNull(e.toString(), null);
		}
	}

	@Test
	public void testEndPath() {
		String endPath = FileUtil.endPath("path");
		Assert.assertTrue(endPath.endsWith(File.separator));
	}

	@Test
	public void testEndRemotePath() {
		String endRemotePath = FileUtil.endRemotePath("path");
		Assert.assertTrue(endRemotePath.endsWith("/"));
	}

	@Test
	public void testDriveExist() {
		if (LocalEnv.osName.toLowerCase().contains("win")) {
			Assert.assertFalse(FileUtil.driveExist("c"));
		} else {
			Assert.assertFalse(FileUtil.driveExist("/"));
		}

	}

	@Test
	public void testRmDirectory() {
		boolean rmDirectory = FileUtil.rmDirectory(TEST_OUTPUT_PATH, true);
		Assert.assertTrue(rmDirectory);
		Assert.assertFalse(new File(TEST_OUTPUT_PATH).exists());
	}

	@Test
	public void testCleanDirectory() {
		boolean rmDirectory = FileUtil.cleanDirectory(TEST_OUTPUT_PATH);
		Assert.assertTrue(rmDirectory);
		Assert.assertTrue(new File(TEST_OUTPUT_PATH).exists());
	}

	@Test
	public void testExist() {
		Assert.assertFalse(FileUtil.exists("path"));
		Assert.assertTrue(FileUtil.exists("src/test/resources/client/app1/app1_V3.2_id-pc-001.json"));
	}

	@Test
	public void testGetFileName() {
		Assert.assertEquals(FileUtil.getFileName("src/test/resources/client/app1/app1_V3.2_id-pc-001.json"), "app1_V3.2_id-pc-001.json");
	}

	@Test
	public void testGetActiveDriveList() {
		List<File> driveList;
		if (LocalEnv.osName.toLowerCase().contains("win")) {
			driveList = FileUtil.getActiveDriveList(new String[] { "c" });
		} else {
			driveList = FileUtil.getActiveDriveList(new String[] { "/" });
		}
		Assert.assertNotNull(driveList);
		Assert.assertFalse(driveList.isEmpty());

	}

	@Test
	public void testGetDriveAsString() {
		File drive;
		if (LocalEnv.osName.toLowerCase().contains("win")) {
			drive = FileUtil.getDrive("c");
		} else {
			drive = FileUtil.getDrive("/");
		}
		Assert.assertNotNull(drive);
		Assert.assertTrue(drive.exists());

		drive = FileUtil.getDrive("");
		Assert.assertNull(drive);
	}

	@Test
	public void testGetDrive() {
		String drive = FileUtil.getDrive(new File("."));
		Assert.assertNotNull(drive);
	}

	@Test
	public void testGetDriveList() {
		List<File> driveList = FileUtil.getDriveList();
		Assert.assertNotNull(driveList);
		Assert.assertFalse(driveList.isEmpty());
	}
}
