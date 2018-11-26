package org.app.client.updater;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.app.client.updater.config.AppDeployment;
import org.app.client.updater.config.update.UpdaterConfig;
import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.config.version.Archive;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.DateUtil;
import org.app.client.updater.utils.FileUtil;
import org.app.client.updater.utils.JSonUtil;

public class UpdaterVersions {

	private static Logger LOG = Logger.getLogger(UpdaterVersions.class);

	private final String appName;
	private final String archivesPath;
	private final String appVersionsPath;
	private FilenameFilter filterJson;
	private FilenameFilter filterArchive;
	private final String appVersionsFilename;
	private final String appConfigFilename;

	public UpdaterVersions(final String appName, final String archivesPath, final String appVersionsPath) {
		super();
		this.appName = appName;
		this.archivesPath = archivesPath;
		this.appVersionsPath = appVersionsPath;
		this.appVersionsFilename = appName + "-versions" + JSonUtil.EXTENSION;
		this.appConfigFilename = appName + "-config" + JSonUtil.EXTENSION;

		initialize(appName);
	}

	private void initialize(final String appName) {
		filterJson = new FilenameFilter() {

			@Override
			public boolean accept(File arg0, String arg1) {
				if (!arg1.equals(appVersionsFilename) && arg1.startsWith(appName) && arg1.endsWith(JSonUtil.EXTENSION)) {
					return true;
				}
				return false;
			}
		};

		filterArchive = new FilenameFilter() {
			@Override
			public boolean accept(File arg0, String arg1) {
				if (arg1.startsWith(appName) && arg1.endsWith(Archive.ARCHIVE_EXTENSION)) {
					return true;
				}
				return false;
			}
		};
	}

	public String update() throws AcuException {
		AppDeployment appDeployment = null;
		UpdaterConfig updaterConfig = null;

		try {
			updaterConfig = (UpdaterConfig) JSonUtil.readJson(FileUtil.endPath(this.appVersionsPath) + this.appConfigFilename, UpdaterConfig.class);
		} catch (IOException e) {
			throw new AcuException(ExceptionInInitializerError.class, this.getClass(), "application.configuration.read.error",
					this.appConfigFilename, e.toString());
		}

		appDeployment = new AppDeployment(appName, updaterConfig.getDeploymentStrategy());

		if (!FileUtil.exists(archivesPath)) {
			throw new AcuException(FileNotFoundException.class, this.getClass(), "application.updater.versions.archives.path.absent", appName,
					archivesPath);
		}

		if (!FileUtil.exists(appVersionsPath)) {
			throw new AcuException(FileNotFoundException.class, this.getClass(), "application.updater.app.versions.path.absent", appName,
					appVersionsPath);
		}

		// Read all version archive file
		File archivesFile = new File(archivesPath);
		File[] listArchives = archivesFile.listFiles(filterArchive);

		for (File archiveFile : listArchives) {
			AppVersion appVersion = null;

			// Verify if corresponding json file exist
			String archiveName = archiveFile.getName();
			String versionName = FileUtil.endPath(archiveFile.getParentFile().getPath())
					+ archiveName.substring(0, archiveName.indexOf(Archive.ARCHIVE_EXTENSION)) + JSonUtil.EXTENSION;

			File versionFile = new File(versionName);
			if (!versionFile.exists()) {
				throw new AcuException(FileNotFoundException.class, this.getClass(), "application.versions.absent", versionFile.getAbsolutePath());
			}

			// Read json file
			try {
				appVersion = (AppVersion) JSonUtil.readJson(versionFile, AppVersion.class);
			} catch (IOException e) {
				throw new AcuException(IOException.class, this.getClass(), "application.versions.read.error", versionFile.getAbsolutePath(),
						e.toString());
			}

			// Validate json file
			if (!appVersion.checkContent()) {
				throw new AcuException(IOException.class, this.getClass(), "application.versions.validation.error", versionFile.getAbsolutePath());
			}

			appDeployment.addAppVersion(appVersion);
		}

		// Backup older app versions file
		File appVersionsFile = new File(FileUtil.endPath(this.appVersionsPath) + this.appVersionsFilename);
		if (appVersionsFile.exists()) {
			String oldVersionsFile = FileUtil.endPath(this.appVersionsPath)
					+ this.appVersionsFilename.substring(0, this.appVersionsFilename.indexOf(JSonUtil.EXTENSION)) + "_" + DateUtil.formatNewDate()
					+ JSonUtil.EXTENSION;
			appVersionsFile.renameTo(new File(oldVersionsFile));
		}

		// Write new app versions file
		// TODO monitoring backup remote json if file already exist
		try {
			JSonUtil.writeJson(appVersionsFile, appDeployment, AppDeployment.class);
		} catch (IOException e) {
			throw new AcuException(IOException.class, this.getClass(), "application.updater.app.versions.write", appVersionsFile.getAbsolutePath(),
					e.toString());
		}

		return appVersionsFile.getPath();
	}

	private static void usage() {
		LOG.info("usage: java -jar <jar name> org.app.client.updater.UpdateServerVersions");
		LOG.info("create a new file <appname>-versions.json from all json files come with *.glis files");
		LOG.info("\targument 1: application name");
		LOG.info("\targument 2: archive path (path to *.glis files + version json files)");
		LOG.info("\targument 3: version path (path to <appname>-versions.json file)");
	}

	public static void main(String[] args) throws AcuException {

		if (args.length != 3) {
			usage();
			return;
		}

		UpdaterVersions serverVersions = new UpdaterVersions(args[0], args[1], args[2]);
		serverVersions.update();
	}

}
