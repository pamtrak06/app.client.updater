package org.app.client.updater.mock;

import org.app.client.updater.config.AppDeployment;
import org.app.client.updater.config.update.DeploymentStrategy;
import org.app.client.updater.config.update.DeploymentStrategy.CRITERIA_LAST_VERSION;
import org.app.client.updater.config.version.AppStructure;
import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.config.version.AppVersions;
import org.app.client.updater.config.version.Archive;
import org.app.client.updater.config.version.OsTarget;
import org.app.client.updater.config.version.Version;
import org.app.client.updater.config.version.VersionFinder;
import org.app.client.updater.utils.FileUtil;

public class MockVersion {

	public static AppStructure createAppStructure(String localAppPath, String appName, String crc) {
		AppStructure appStructure = new AppStructure();
		appStructure.addCheckFileSystem("CHECK_FOLDER", FileUtil.endPath(localAppPath), FileUtil.endPath(appName)
				+ "plugins", "exist");
		appStructure.addCheckFileSystem("CHECK_FOLDER", FileUtil.endPath(localAppPath), FileUtil.endPath(appName)
				+ "jre", "exist");
		appStructure.addCheckFileSystem("CHECK_FILE", FileUtil.endPath(localAppPath), FileUtil.endPath(appName)
				+ "app1.exe", "exist");
		appStructure.addCheckFileSystem("CHECK_CHECKSUM", FileUtil.endPath(localAppPath), FileUtil.endPath(appName)
				+ "app1.exe", crc);
		return appStructure;
	}

	public static AppVersion createAppversionV34() {
		return createAppversion("app1", "False", "V3.4", "20130911",
				VersionFinder.VERSION_FIND_METHOD.GETVERSION_FROM_FILENAME_SYNTAX.name(), "app1-main-(V.*)\\.jar",
				"app1-archive-V3.1.glis", "app1", "app1/app1.exe", "Windows 7", "6.1", "x86",
				"src/test/resources/client/app1/App1V3.1", AppVersion.UPDATE_METHOD.BACKUP.name(),
				"759e1a30ca2b3bfe0c2f86e99360767a");
	}

	public static AppVersion createAppversionV33() {
		return createAppversion("app1", "True", "V3.3", "20130911",
				VersionFinder.VERSION_FIND_METHOD.GETVERSION_FROM_FILENAME_SYNTAX.name(), "app1-main-(V.*)\\.jar",
				"app1-archive-V3.1.glis", "app1", "app1/app1.exe", "Windows 7", "6.1", "x86",
				"src/test/resources/client/app1/App1V3.1", AppVersion.UPDATE_METHOD.BACKUP.name(),
				"759e1a30ca2b3bfe0c2f86e99360767a");
	}

	public static AppVersion createAppversionV32() {
		return createAppversion("app1", "False", "V3.2", "20130911",
				VersionFinder.VERSION_FIND_METHOD.GETVERSION_FROM_FILENAME_SYNTAX.name(), "app1-main-(V.*)\\.jar",
				"app1-archive-V3.1.glis", "app1", "app1/app1.exe", "Windows 7", "6.1", "x86",
				"src/test/resources/client/app1/App1V3.1", AppVersion.UPDATE_METHOD.BACKUP.name(),
				"759e1a30ca2b3bfe0c2f86e99360767a");
	}

	public static AppVersion createAppversionV31() {
		return createAppversion("app1", "False", "V3.1", "20130911",
				VersionFinder.VERSION_FIND_METHOD.GETVERSION_FROM_FILENAME_SYNTAX.name(), "app1-main-(V.*)\\.jar",
				"app1-archive-V3.1.glis", "app1", "app1/app1.exe", "Windows 7", "6.1", "x86",
				"src/test/resources/client/app1/App1V3.1", AppVersion.UPDATE_METHOD.BACKUP.name(),
				"759e1a30ca2b3bfe0c2f86e99360767a");
	}

	public static AppVersion createAppversion(String appName, String activated, String versionValue,
			String deliveredDate, String findVersionMethod, String versionParameter, String archiveName,
			String pathArchive, String mainRun, String osName, String osVersion, String osArch, String installPath,
			String updateMethod, String crc) {
		Version version = new Version(versionValue, deliveredDate, findVersionMethod, versionParameter);
		AppStructure appStructure = MockVersion.createAppStructure(installPath, appName, crc);
		Archive archive = new Archive(archiveName, pathArchive, mainRun);
		OsTarget osTarget = new OsTarget(osName, osVersion, osArch);
		AppVersion appVersion = new AppVersion(appName, archive, version, osTarget, appStructure, activated,
				installPath, "false", installPath, updateMethod);
		return appVersion;
	}

	public static AppDeployment createAppVersions() {
		DeploymentStrategy deploymentStrategy = new DeploymentStrategy(CRITERIA_LAST_VERSION.ENABLED,
				Boolean.toString(true));

		AppDeployment appDeployment = new AppDeployment("app1", deploymentStrategy);

		AppVersions appVersions = appDeployment.getAppVersions();

		appVersions.addAppVersion(createAppversionV31());

		appVersions.addAppVersion(createAppversionV32());

		appVersions.addAppVersion(createAppversionV33());

		appVersions.addAppVersion(createAppversionV34());

		return appDeployment;
	}

}
