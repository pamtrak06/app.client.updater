package org.app.client.updater;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.exception.ZipException;

import org.apache.log4j.Logger;
import org.app.client.updater.config.AppDeployment;
import org.app.client.updater.config.HostConfig;
import org.app.client.updater.config.update.UpdaterConfig;
import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.FileSearcher;
import org.app.client.updater.utils.FileUtil;
import org.app.client.updater.utils.HttpUtil;
import org.app.client.updater.utils.JSonUtil;
import org.app.client.updater.utils.LocalEnv;
import org.app.client.updater.utils.ProxyUtil;
import org.app.client.updater.utils.ResourceUtil;
import org.app.client.updater.utils.UiUtil;
import org.app.client.updater.utils.ZipUtil;

public class AppClientUpdater implements IUpdater {

	private static Logger LOG = Logger.getLogger(AppClientUpdater.class);

	public static enum UPDATE_STEPS {
		GET_LAST_VERSION, GET_ACTIVE_VERSION, COMPARE_VERSIONS, INSTALL_APP, RUN_APP, SEND_NOTIFICATION, ABORT
	}

	private UPDATE_STEPS currentStep_;
	private UPDATE_STEPS nextStep_;

	private final String appName_;
	private final String configPath_;
	private String updaterConfigFilename_;
	private boolean offlineMode_;
	private boolean silentMode_;

	private String tmpOutputPath_;

	// TODO use for rollback
	private File zipBackup_;

	private AppDeployment appVersions_;

	private AppVersion latestVersion_;

	private UpdaterConfig updaterConfig_;

	private List<AppVersion> appVersionLocs_;

	private static AppClientUpdater instance_ = null;

	public static void initInstance() {
		 instance_ = null;
	}

	public static AppClientUpdater getInstance() {
		return instance_;
	}

	public static AppClientUpdater getInstance(final String appName, final String remotePath) {
		if (instance_ == null) {
			instance_ = new AppClientUpdater(appName, remotePath);
		}
		return instance_;
	}

	public static AppClientUpdater getInstance(final String appName, final String configPath, final boolean offline, final boolean silentMode) {
		if (instance_ == null) {
			instance_ = new AppClientUpdater(appName, configPath, offline, silentMode);
		}
		return instance_;
	}

	private AppClientUpdater(final String appName, final String remotePath) {
		this(appName, remotePath, false, true);
	}

	private AppClientUpdater(final String appName, final String configPath, final boolean offline, final boolean silentMode) {
		super();
		this.appName_ = appName;
		this.configPath_ = configPath;
		this.offlineMode_ = offline;
		this.silentMode_ = silentMode;

		UiUtil.SILENT_MODE = this.silentMode_;

		buildConfigFilename(appName);

	}

	public AppVersion getLatestVersion() {
		return latestVersion_;
	}

	public void setOffline(boolean offline) {
		this.offlineMode_ = offline;
	}

	public void setSilentMode(boolean silentMode) {
		this.silentMode_ = silentMode;
	}

	public void setNextStep(UPDATE_STEPS nextStep) {
		this.nextStep_ = nextStep;
	}

	public String getAppName() {
		return appName_;
	}

	public String getConfigPath() {
		return configPath_;
	}

	private void buildConfigFilename(final String appName) {
		String configPathFormatted;

		LOG.info("Offline update for app: " + appName + ": " + this.offlineMode_);

		if (this.offlineMode_) {
			configPathFormatted = FileUtil.endPath(this.configPath_);
		} else {
			configPathFormatted = FileUtil.endRemotePath(this.configPath_);
		}

		this.updaterConfigFilename_ = configPathFormatted + this.appName_ + "-config.json";
	}

	public boolean update() throws AcuException {
		nextStep_ = UPDATE_STEPS.GET_LAST_VERSION;

		int confirmResponse = UiUtil.showConfirmI18n(ResourceUtil.getI18Message("application.update.confirmation", appName_));

		if (confirmResponse == UiUtil.YES) {

			getLastVersion();

			getActiveVersion();

			installOrUpdate();

			runApplication();

			sendNotification();

		}
		return true;
	}

	@Override
	public void rollback() throws AcuException {
		// TODO implement rollback
		switch (currentStep_) {
		case COMPARE_VERSIONS:
			break;
		case GET_ACTIVE_VERSION:
			break;
		case GET_LAST_VERSION:
			break;
		case INSTALL_APP:
			break;
		case RUN_APP:
			break;
		case SEND_NOTIFICATION:
			break;
		default:
			break;
		}
	}

	@Override
	public void getLastVersion() throws AcuException {
		currentStep_ = UPDATE_STEPS.GET_LAST_VERSION;

		if (nextStep_.equals(UPDATE_STEPS.GET_LAST_VERSION)) {
			LOG.info("#STEP: " + nextStep_.name());

			updaterConfig_ = readAppConfig();

			appVersions_ = readAppVersions(updaterConfig_);

			latestVersion_ = appVersions_.getLatestVersion(LocalEnv.osName, LocalEnv.osVersion);

			nextStep_ = UPDATE_STEPS.GET_ACTIVE_VERSION;
		}
	}

	@Override
	public void getActiveVersion() {
		currentStep_ = UPDATE_STEPS.GET_ACTIVE_VERSION;

		if (nextStep_.equals(UPDATE_STEPS.GET_ACTIVE_VERSION)) {
			LOG.info("#STEP: " + nextStep_.name());

			appVersionLocs_ = findLocalVersions(appVersions_);

			if (appVersionLocs_.size() > 1) {
				LOG.error("Local install versions list : ");
				StringBuffer message = new StringBuffer();
				StringBuffer msg = new StringBuffer();
				for (AppVersion appVersion : appVersionLocs_) {
					msg = new StringBuffer();
					msg.append("Version: " + appVersion.getVersion().getValue());
					msg.append(" is installed at: ");
					msg.append(appVersion.getLocalPath());
					LOG.error(msg.toString());
					message.append(msg);
					message.append("\n");
				}

				String i18Message = ResourceUtil.getI18Message("application.localVersion.search.multiple.error", message.toString());
				UiUtil.showErrorI18n(i18Message);
				return;
			}

			nextStep_ = UPDATE_STEPS.INSTALL_APP;
		}
	}

	@Override
	public void installOrUpdate() throws AcuException {
		currentStep_ = UPDATE_STEPS.INSTALL_APP;

		if (nextStep_.equals(UPDATE_STEPS.INSTALL_APP)) {
			LOG.info("#STEP: " + nextStep_.name());

			// update : download or/and install
			boolean localUpperVersionFound = false;
			if (appVersionLocs_ == null || appVersionLocs_.isEmpty()
					|| Boolean.parseBoolean(updaterConfig_.getDeploymentStrategy().getForceInstallation())) {
				localUpperVersionFound = false;
			} else {
				for (AppVersion appVer : appVersionLocs_) {
					if (appVer == null || appVer.compareTo(latestVersion_) > 0) {
						localUpperVersionFound = true;
						break;
					}
				}
			}
			if (!localUpperVersionFound) {
				AppVersion localAppVersion = (!appVersionLocs_.isEmpty()) ? appVersionLocs_.get(0) : null;
				installLatestVersion(updaterConfig_, latestVersion_, localAppVersion);
			}

			nextStep_ = UPDATE_STEPS.RUN_APP;
		}
	}

	@Override
	public void runApplication() {
		currentStep_ = UPDATE_STEPS.RUN_APP;

		if (nextStep_.equals(UPDATE_STEPS.RUN_APP)) {
			LOG.info("#STEP: " + nextStep_.name());
			nextStep_ = UPDATE_STEPS.SEND_NOTIFICATION;
		}
		// TODO run version or not ?
		// ExecUtil.runMain(new String[] {
		// latestVersion_.getArchive().getMainRun() });
	}

	@Override
	public void sendNotification() throws AcuException {
		currentStep_ = UPDATE_STEPS.SEND_NOTIFICATION;

		if (nextStep_.equals(UPDATE_STEPS.SEND_NOTIFICATION)) {
			LOG.info("#STEP: " + nextStep_.name());
			// TODO Send back notification informations :
			UpdaterNotifier.sendNotification(latestVersion_, getTempFolder(), this.configPath_, appName_, "OK", offlineMode_);
		}
	}

	public UpdaterConfig readAppConfig() throws AcuException {
		UpdaterConfig updaterConfig = null;

		LOG.info("Read config file: " + this.updaterConfigFilename_);

		if (this.offlineMode_) {
			try {
				updaterConfig = (UpdaterConfig) JSonUtil.readJson(this.updaterConfigFilename_, UpdaterConfig.class);
			} catch (IOException e) {
				throw new AcuException(IOException.class, this.getClass(), "application.configuration.read.error", this.updaterConfigFilename_,
						e.toString());
			}
		} else {
			try {
				ProxyUtil.configureProxySettings();
			} catch (URISyntaxException e1) {
				throw new AcuException(URISyntaxException.class, this.getClass(), "application.configuration.proxy.error", e1.toString());
			}
			try {
				updaterConfig = (UpdaterConfig) JSonUtil.readRemoteJson(this.updaterConfigFilename_, UpdaterConfig.class);
			} catch (IOException e) {
				throw new AcuException(IOException.class, this.getClass(), "application.configuration.read.error", this.updaterConfigFilename_,
						e.toString());
			}

			// TODO test all configured servers ???
			// ProxyConfig proxyConfig =
			// updaterConfig.getUpdateServers().get(0).getProxyConfig();
			// ProxyUtil.configureProxySettings(proxyConfig.getHostName(),
			// proxyConfig.getPort(), proxyConfig.getUsername(),
			// proxyConfig.getPasword());
		}
		return updaterConfig;
	}

	public AppDeployment readAppVersions(final UpdaterConfig updaterConfig) throws AcuException {
		AppDeployment appDeployment = null;

		final String versionsFilename = updaterConfig.getVersionsFilename();
		LOG.info("Read versions file:" + versionsFilename);

		if (this.offlineMode_) {
			try {
				appDeployment = (AppDeployment) JSonUtil.readJson(versionsFilename, AppDeployment.class);
			} catch (IOException e) {
				throw new AcuException(ExceptionInInitializerError.class, this.getClass(), "application.versions.read.error", versionsFilename,
						e.toString());
			}
		} else {
			try {
				final HostConfig hostConfig = updaterConfig.getUpdateServers().get(0).getHostConfig();
				String remotePath = hostConfig.toUrlForm() + FileUtil.endRemotePath(hostConfig.getPath()) + versionsFilename;
				appDeployment = (AppDeployment) JSonUtil.readRemoteJson(remotePath, AppDeployment.class);
			} catch (IOException e) {
				throw new AcuException(ExceptionInInitializerError.class, this.getClass(), "application.versions.read.error", versionsFilename,
						e.toString());
			}
		}

		// Add deployment strategy
		appDeployment.setDeploymentStrategy(updaterConfig.getDeploymentStrategy());

		return appDeployment;
	}

	// TODO activate to ask for search path
	private File selectSearchPathForLocalApp(String searchPaths) {
		File activePath = null;
		String i18Message = ResourceUtil.getI18Message("application.LocalVersion.search.definePath.confirmation", latestVersion_.getVersion()
				.getValue(), searchPaths);
		int confirmSearchAlldisk = UiUtil.showConfirmI18n(i18Message);
		if (confirmSearchAlldisk == UiUtil.YES) {
			String i18Message2 = ResourceUtil.getI18Message("application.LocalVersion.search.definePath.message", appName_);
			activePath = UiUtil.showFileChooserI18n(i18Message2, searchPaths);
		} else {
			return null;
		}

		return activePath;
	}

	public List<AppVersion> findLocalVersions(AppDeployment appVersions) {
		List<AppVersion> listAppVersion = new ArrayList<AppVersion>();

		LOG.info("Search local versions on disk(s)...");

		List<AppVersion> applicationList = appVersions.getAppVersions().getAppVersionList();
		for (AppVersion appVersion : applicationList) {

			int confirmSearchAlldisk = UiUtil.YES;
			List<String> searcPathList = new ArrayList<String>();

			String searchPaths = appVersion.getSearchPaths();
			String diskFilter = null;

			if (searchPaths != null && !searchPaths.isEmpty()) {
				String[] searchPathSplit = searchPaths.split(";");

				for (String searchPath : searchPathSplit) {

					if (FileUtil.exists(searchPath)) {
						diskFilter = FileUtil.getDrive(new File(searchPath));
						searcPathList.add(searchPath);
					} else {
						searchPath = null;
					}
				}
			}

			if (searcPathList.isEmpty()) {
				LOG.warn("No valid search path");

				if (!silentMode_) {

					// Define a search path
					File pathLocalApp = selectSearchPathForLocalApp(searchPaths);

					// Else find on all disk
					if (pathLocalApp == null) {

						// TODO input dialog to put specified search path list
						String i18Message = ResourceUtil.getI18Message("application.LocalVersion.search.allDisks.confirmation", appVersion
								.getVersion().getValue(), searchPaths);
						confirmSearchAlldisk = UiUtil.showConfirmI18n(i18Message);

						if (confirmSearchAlldisk == UiUtil.YES) {
							LOG.warn("Searching on all available disks");
							searcPathList.add("");
						}
					}

				}
			}

			if (confirmSearchAlldisk == UiUtil.YES) {

				for (String searchPath : searcPathList) {

					LOG.debug("Search in path " + searchPath + "...");
					List<File> listFiles = FileSearcher.searchFile(appVersion.getVersion().getVersionFinder().getParameter(), diskFilter, searchPath);

					if (listFiles != null && !listFiles.isEmpty()) {

						for (File file : listFiles) {

							AppVersion appVer = new AppVersion(appVersion);
							// TODO how to find real relative app path ???
							appVer.setLocalPath(file.getAbsolutePath());
							boolean validate = appVer.validate();

							if (validate) {
								LOG.debug("Add version " + appVer.getVersion().getValue() + "...");
								listAppVersion.add(appVer);
							}
						}
					}
				}
			}

		}
		return listAppVersion;
	}

	public void installLatestVersion(final UpdaterConfig updaterConfig, final AppVersion latestVersion, final AppVersion localAppVersion)
			throws AcuException {
		String zipFile = null;
		String remotePathData = null;
		final String installPath = latestVersion.getInstallPath();
		final String version = latestVersion.getVersion().getValue();

		// TODO local app exist
		boolean incrementalMode = false;
		if (localAppVersion != null) {
			incrementalMode = localAppVersion.getUpdateMethod().compareTo(AppVersion.UPDATE_METHOD.INCREMENTAL) == 0;
			// TODO Validation activation or not ???
			boolean validate = localAppVersion.getAppStructure().validate();
			if (!validate) {
				// TODO if app structure is invalid : so no incremental
				// validation possible
				LOG.warn("Structure of application is invalid");

			}
			// TODO confirm upgrade compatibility
			// TODO local version V3.0 latest 3.3, must install 3.1 and 3.2
			// before !!

			// TODO manage local and remot install path :
			// TODO if local<>remote path => zip and clean local and install in
			// remote install path
			// TODO if local==remote path => zip and clean local and install in
			// remote install path

			// TODO deployment mode
			String i18Message = ResourceUtil.getI18Message("application.installation.confirmation", localAppVersion.getVersion().getValue(),
					localAppVersion.getLocalPath(), localAppVersion.getUpdateMethod().name().toLowerCase());
			int showConfirm = UiUtil.showConfirmI18n(i18Message);
			if (showConfirm == UiUtil.YES) {
				if (localAppVersion.getUpdateMethod().compareTo(AppVersion.UPDATE_METHOD.CLEAN) == 0) {
					FileUtil.cleanDirectory(localAppVersion.getLocalPath());
				} else if (localAppVersion.getUpdateMethod().compareTo(AppVersion.UPDATE_METHOD.BACKUP) == 0) {
					// TODO path must be app path and not jar file path
					LOG.info("Zip folder " + zipFile + " ...");
					try {
						zipBackup_ = ZipUtil.zip(localAppVersion.getLocalPath(), getTempFolder());
					} catch (ZipException e) {
						throw new AcuException(ZipException.class, this.getClass(), "application.installation.unzip.error", zipFile, installPath,
								e.toString());
					}

					FileUtil.cleanDirectory(installPath);
				} else if (incrementalMode) {
					// TODO implement incremental method : make a diff between
					// two version
					throw new AcuException(ZipException.class, this.getClass(), "application.installation.update.incremental.error", "");
				}
			} else {
				String i18Message2 = ResourceUtil.getI18Message("application.installation.abort", latestVersion.getAppName(), version);
				UiUtil.showInformationI18n(i18Message2);
				return;
			}
		}

		if (FileUtil.exists(installPath)) {
			// TODO deployment mode
			String i18Message = ResourceUtil.getI18Message("application.installation.clean.confirmation", latestVersion.getAppName(), version,
					installPath);
			int showConfirm = UiUtil.showConfirmI18n(i18Message);
			if (showConfirm == UiUtil.YES) {
				FileUtil.cleanDirectory(installPath);
			} else {
				String i18Message2 = ResourceUtil.getI18Message("application.installation.abort", latestVersion.getAppName(), version);
				UiUtil.showInformationI18n(i18Message2);
				return;
			}
		} else {
			FileUtil.mkdirs(installPath);
		}

		LOG.info("Install latest version: " + version + "...");

		if (this.offlineMode_) {
			zipFile = FileUtil.endPath(latestVersion.getArchive().getPath()) + latestVersion.getArchive().getName();
			File zip = new File(zipFile);
			if (!zip.exists()) {
				throw new AcuException(IllegalArgumentException.class, this.getClass(), "application.installation.archive.absent.error", zipFile);
			}
		} else {
			remotePathData = FileUtil.endRemotePath(updaterConfig.getUpdateServers().get(0).getHostConfig().toUrlForm())
					+ FileUtil.endRemotePath(latestVersion.getArchive().getPath()) + latestVersion.getArchive().getName();
			try {
				LOG.info("Download archive: " + remotePathData + " in path: " + getTempFolder() + "...");
				zipFile = HttpUtil.downloadZip(remotePathData, getTempFolder());
			} catch (IOException e) {
				throw new AcuException(IOException.class, this.getClass(), "application.installation.archive.download.error", zipFile, e.toString());
			}
		}

		if (incrementalMode) {
			// TODO Copy content of older version
			// TODO confirm upgrade compatibility
		}

		LOG.info("Unzip archive " + zipFile + " ...");
		try {
			ZipUtil.unZip(zipFile, installPath);
		} catch (ZipException e) {
			throw new AcuException(ZipException.class, this.getClass(), "application.installation.unzip.error", zipFile, installPath, e.toString());
		}

		String i18Message = ResourceUtil.getI18Message("application.installation.terminated.success", appName_, version);
		UiUtil.showInformationI18n(i18Message);
	}

	public String getTempFolder() {
		if (tmpOutputPath_ == null) {
			tmpOutputPath_ = FileUtil.createFolderTemporary(".");
			if (FileUtil.exists(tmpOutputPath_)) {
				LOG.info("Temporary working folder: " + tmpOutputPath_ + " created");
			} else {
				LOG.error("Unable to create temporary folder");
			}
		}
		return tmpOutputPath_;
	}

	public static void usage() {
		LOG.info("AppClientUpdater : [applicatiom name] [remote path: http://app-update/app1]");
		LOG.info("or");
		LOG.info("AppClientUpdater :  [applicatiom name] "
				+ "[local path to files <appName>-config.json and <appName>-versions.json and *.glis archives] "
				+ "[offline:true|false];[silentMode:true|false]");
	}

	/**
	 * Main application client updater<br />
	 * Argument 1 : application name<br />
	 * Argument 2 : server:port/path where to find jar, jnlp, json configuration
	 * files<br />
	 *
	 * @throws AcuException
	 */
	public static void main(String[] args) throws AcuException {

		UiUtil.setLookAndFeel();

		if (args.length == 2) {
			String appName = args[0];
			String remotePath = args[1];

			AppClientUpdater.getInstance(appName, remotePath).update();

		} else if (args.length == 3) {
			boolean offline = false;
			boolean silentMode = false;

			String appName = args[1];
			String remotePath = args[2];
			String[] options = args[3].split(";");
			for (String option : options) {
				if (option.startsWith("offline:")) {
					offline = Boolean.parseBoolean(option.substring(option.indexOf("offline:".length())));
				} else if (option.startsWith("silentMode:")) {
					silentMode = Boolean.parseBoolean(option.substring(option.indexOf("silentMode:".length())));
				} else if (option.startsWith("locale:")) {
					String local = option.substring(option.indexOf("locale:".length()));
					ResourceUtil.initI18n(local);
				}
			}

			AppClientUpdater.getInstance(appName, remotePath, offline, silentMode).update();

		} else {
			usage();
		}

	}

}
