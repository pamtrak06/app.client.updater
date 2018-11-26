package org.app.client.updater;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;
import org.app.client.updater.config.HostConfig;
import org.app.client.updater.config.update.UpdaterConfig;
import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.FileUtil;
import org.app.client.updater.utils.JSonUtil;
import org.app.client.updater.utils.OsUtil;
import org.app.client.updater.utils.ProxyUtil;
import org.app.client.updater.utils.UiUtil;

public class MiniUpdater  {

	private static final String DIALOG_TITLE = "Mini Updater: ";
	private static Logger LOG = Logger.getLogger(AppClientUpdater.class);

	public static enum UPDATE_STEPS {
		GET_LAST_VERSION, GET_ACTIVE_VERSION, COMPARE_VERSIONS, INSTALL_APP, RUN_APP, SEND_NOTIFICATION
	}

	private final String appName_;
	private final String configPath_;
	private final String updaterConfigFilename_;

	private UpdaterConfig updaterConfig_;

	private AppVersion latestVersion_;

	private UPDATE_STEPS currentStep_;
	private UPDATE_STEPS nextStep_;
	private File activePath;

	public MiniUpdater(final String appName, final String configPath) throws URISyntaxException, AcuException {
		super();
		this.appName_ = appName;
		this.configPath_ = configPath;

		this.updaterConfigFilename_ = FileUtil.endRemotePath(this.configPath_) + this.appName_ + "-config.json";
		LOG.info("Online update for app: " + appName);

		initialization();
	}

	private void initialization() throws URISyntaxException, AcuException {
		ProxyUtil.configureProxySettings();

		// read configuration app file
		updaterConfig_ = readAppConfig();
	}

	public void update() throws AcuException {

		int confirmResponse = UiUtil.showConfirm(DIALOG_TITLE + appName_, "Do you want to update/install application : " + appName_ + " ?");

		if (confirmResponse == UiUtil.YES) {

			getLastVersion();

			getActiveVersion();

			// Install or run ?
			compareVersions();

			installApp();

			sendNotification();

			runApplication();
		}
	}

	public void rollback() {
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

	public UpdaterConfig readAppConfig() throws AcuException {

		LOG.info("Read config file: " + this.updaterConfigFilename_);

		try {
			updaterConfig_ = (UpdaterConfig) JSonUtil.readRemoteJson(this.updaterConfigFilename_, UpdaterConfig.class);
		} catch (IOException e) {
			String message = "Reading remote updater configuration file: " + this.updaterConfigFilename_;
			throw new AcuException(IOException.class, this.getClass(), message);
		}

		return updaterConfig_;
	}

	public void getLastVersion() throws AcuException {
		nextStep_ = UPDATE_STEPS.GET_ACTIVE_VERSION;
		currentStep_ = UPDATE_STEPS.GET_LAST_VERSION;

		if (nextStep_.equals(UPDATE_STEPS.GET_LAST_VERSION)) {

			// Get version file name from os
			final String versionFilename = updaterConfig_.getVersionsFilename();
			versionFilename.replace("{os.name}", OsUtil.getEnvOs());
			LOG.info("Read versions file:" + versionFilename);

			// Retrieve latest version (from local or remote access)
			try {
				final HostConfig hostConfig = updaterConfig_.getUpdateServers().get(0).getHostConfig();
				String remotePath = hostConfig.toUrlForm() + FileUtil.endRemotePath(hostConfig.getPath()) + versionFilename;
				latestVersion_ = (AppVersion) JSonUtil.readRemoteJson(remotePath, AppVersion.class);
			} catch (IOException e) {
				String message = "Reading remote app version file: " + versionFilename;
				throw new AcuException(ExceptionInInitializerError.class, this.getClass(), message);
			}

			// // if hhtp problem, goto step RUN_APP
			// nextStep_ = UPDATE_STEPS.RUN_APP;

		}

	}

	public void getActiveVersion() {
		activePath = null;
		currentStep_ = UPDATE_STEPS.GET_ACTIVE_VERSION;

		if (nextStep_.equals(UPDATE_STEPS.GET_ACTIVE_VERSION)) {

			// Retrieve active path and ask for path of existing app
			activePath = specifyActiveAppPath(latestVersion_.getSearchPaths());
			
			if (activePath != null) {
				latestVersion_.setLocalPath(activePath.getPath());
			} else {
				UiUtil.showInformation(DIALOG_TITLE + appName_, "Abort update/install of version: " + latestVersion_.getVersion().getValue());
				return;
			}
		}
	}

	private File specifyActiveAppPath(String searchPaths) {
		int confirmSearchAlldisk = UiUtil.showConfirm(DIALOG_TITLE + appName_, "version: " + latestVersion_.getVersion().getValue()
				+ ", no application founded in search paths: " + searchPaths + ", do you want to search elsewhere ?");
		if (confirmSearchAlldisk == UiUtil.YES) {
			activePath = UiUtil.showFileChooser(DIALOG_TITLE + appName_, "Please specify path where application: \"" + appName_ + "\" is installed",
					null);
			if (!activePath.exists()) {
				specifyActiveAppPath(activePath.getPath());
			}
		} else {
			return null;
		}

		return activePath;
	}

	public void compareVersions() {
		currentStep_ = UPDATE_STEPS.COMPARE_VERSIONS;

		if (nextStep_.equals(UPDATE_STEPS.COMPARE_VERSIONS)) {

		}

	}

	public void installApp() {
		currentStep_ = UPDATE_STEPS.INSTALL_APP;

	}

	public void runApplication() {
		currentStep_ = UPDATE_STEPS.RUN_APP;

	}

	public void sendNotification() {
		currentStep_ = UPDATE_STEPS.SEND_NOTIFICATION;

	}

	public static void main(String[] args) throws AcuException {
		UiUtil.setLookAndFeel();

		if (args.length == 2) {
			String appName = args[0];
			String remotePath = args[1];

			try {
				new MiniUpdater(appName, remotePath).update();
			} catch (URISyntaxException e) {
				throw new AcuException(URISyntaxException.class, MiniUpdater.class, "Initialization of mini updater, cause: " + e.toString());
			}

		}

	}

}
