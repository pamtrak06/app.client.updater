package org.app.client.updater;

import java.io.File;
import java.io.IOException;
import java.io.WriteAbortedException;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.JSonUtil;
import org.app.client.updater.utils.LocalEnv;
import org.app.client.updater.utils.OsUtil;
import org.app.client.updater.utils.ResourceUtil;
import org.app.client.updater.utils.UiUtil;

public class UpdaterNotifier {

	// TODO strategy for id ??
	// TODO get hardware information : mac adress / ip
	private final String id;
	private final String appName;
	private final String osName;
	private final String osVersion;
	private final String osArch;
	private final String versionInstalled;
	private final String updateStatus;
	private AppVersion appVersion;

	public UpdaterNotifier(String id, String appName, String versionInstalled, String updateStatus) {
		super();
		this.id = id;
		this.appName = appName;
		this.osName = LocalEnv.osName;
		this.osVersion = LocalEnv.osVersion;
		this.osArch = LocalEnv.osArch;
		this.versionInstalled = versionInstalled;
		this.updateStatus = updateStatus;
	}

	public String getAppName() {
		return appName;
	}

	public AppVersion getAppVersion() {
		return appVersion;
	}

	public void setAppVersion(AppVersion appVersion) {
		this.appVersion = appVersion;
	}

	public String getId() {
		return id;
	}

	public String getOsName() {
		return osName;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public String getOsArch() {
		return osArch;
	}

	public String getVersionInstalled() {
		return versionInstalled;
	}

	public String getUpdateStatus() {
		return updateStatus;
	}

	public static void sendNotification(final AppVersion appVersion, final String outputPath, final String remotePath,
			final String appName, final String status, final boolean offlineMode) throws AcuException {
		boolean writeRemoteJson = false;
		UpdaterNotifier updateStatus = null;
		String version = "";
		String id = null;

		try {
			id = OsUtil.getMacAddress();
		} catch (UnknownHostException e1) {
			String i18Message = ResourceUtil.getI18Message("application.utils.Identifier.error", e1.toString());
			UiUtil.showError(ResourceUtil.DIALOG_TITLE + "error", i18Message);
		} catch (SocketException e1) {
			String i18Message = ResourceUtil.getI18Message("application.utils.Identifier.error", e1.toString());
			UiUtil.showError(ResourceUtil.DIALOG_TITLE + "error", i18Message);
		}

		if (appVersion != null) {
			version = appVersion.getVersion().getValue();
			updateStatus = new UpdaterNotifier(id, appName, version, status);
			updateStatus.setAppVersion(appVersion);
		}
		if (!version.isEmpty()) {
			version = version + "_";
		}
		File file = new File(outputPath + appName + "_" + version + id + JSonUtil.EXTENSION);

		try {
			JSonUtil.writeJson(file, updateStatus, UpdaterNotifier.class);
		} catch (IOException e) {
			String i18Message = ResourceUtil.getI18Message("application.notifier.write.error", file.getPath(),
					e.getMessage());
			UiUtil.showError(ResourceUtil.DIALOG_TITLE + "error", i18Message);
		}
		int confirmISend = UiUtil.showConfirmI18n(ResourceUtil.getI18Message("application.notifier.send.confirm"));
		if (confirmISend == UiUtil.YES) {
			// TODO test if server is accessible, else show info dialog
			if (!offlineMode) {
				try {
					writeRemoteJson = JSonUtil.writeRemoteJson(file.getPath(), remotePath);
				} catch (IOException e) {
					String i18Message = ResourceUtil.getI18Message("application.notifier.write.error", remotePath
							+ file.getName(), e.getMessage());
					UiUtil.showError(ResourceUtil.DIALOG_TITLE + "error", i18Message);
				}
				if (!writeRemoteJson) {
					String i18Message = ResourceUtil.getI18Message("application.notifier.write.error", remotePath
							+ file.getName(), " undetermined");
					UiUtil.showError(ResourceUtil.DIALOG_TITLE + "error", i18Message);
				}
			}
		}
	}
}
