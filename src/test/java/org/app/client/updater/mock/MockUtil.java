package org.app.client.updater.mock;

import org.app.client.updater.utils.FileUtil;

public final class MockUtil {

	// TODO put to false endly
	public static boolean rmOutput = false;
	public static String TEST_PATH = "./tmp";

	private static String createTestOutput() {
		FileUtil.mkdirs(TEST_PATH);
		FileUtil.cleanDirectory(TEST_PATH);
		return FileUtil.endPath(TEST_PATH);
	}

	private static void cleanTestOuput() {
		if (rmOutput) {
			FileUtil.rmDirectory(TEST_PATH, true);
		}
	}

	private static String createTestPathOutput(final String pathToCopy) {

		createTestOutput();

		FileUtil.copyFolder(pathToCopy, FileUtil.endPath(TEST_PATH));

		return FileUtil.endPath(FileUtil.endPath(TEST_PATH) + FileUtil.getFileName(pathToCopy));
	}

	public static String setupPathTmp() {
		return createTestOutput();
	}

	public static String setupPathClient() {
		String path = MockClient.LOCAL_CLIENT_PATH;
		return createTestPathOutput(path) + FileUtil.endPath(MockServer.APPNAME1);
	}

	public static String setupPathServer() {
		String path = MockServer.LOCAL_SERVER_PATH;
		return createTestPathOutput(path) + FileUtil.endPath(MockServer.APPNAME1);

	}

	public static void tearDownPathClient() {
		cleanTestOuput();
	}

	public static void tearDownPathServer() {
		cleanTestOuput();
	}
}
