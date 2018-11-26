package org.app.client.updater.config.version;

import org.app.client.updater.config.IValidation;
import org.app.client.updater.utils.JSonUtil;
import org.app.client.updater.utils.VersionUtil;

public class AppVersion implements IValidation, Comparable<AppVersion> {

	public static enum UPDATE_METHOD {
		INCREMENTAL, BACKUP, CLEAN
	};

	private String id;
	private final String appName;
	private final Archive archive;
	private final Version version;
	private final OsTarget osTarget;
	private final AppStructure appStructure;
	private final String activated;
	private final String searchPaths;
	private final String searchAllDisks;
	private final String installPath;
	private final UPDATE_METHOD updateMethod;
	private String localPath;

	public AppVersion(String appName, Archive archive, Version version, OsTarget osTarget, AppStructure appStructure,
			String activated, String searchPaths, String searchAllDisks, String installPath, String updateMethod) {
		super();
		this.appName = appName;
		this.archive = archive;
		this.version = version;
		this.osTarget = osTarget;
		this.appStructure = appStructure;
		this.activated = activated;
		this.installPath = installPath;
		this.searchPaths = searchPaths;
		this.searchAllDisks = searchAllDisks;
		this.updateMethod = UPDATE_METHOD.valueOf(updateMethod.toUpperCase());

		this.id = String.valueOf(hashCode());
	}

	public AppVersion(AppVersion appVersion) {
		super();
		this.appName = appVersion.getAppName();
		this.archive = appVersion.getArchive();
		this.version = appVersion.getVersion();
		this.osTarget = appVersion.getOsTarget();
		this.appStructure = appVersion.getAppStructure();
		this.activated = appVersion.getActivated();
		this.installPath = appVersion.getInstallPath();
		this.searchPaths = appVersion.getSearchPaths();
		this.searchAllDisks = appVersion.getSearchAllDisks();
		this.updateMethod = appVersion.getUpdateMethod();

		this.id = String.valueOf(appVersion.hashCode());
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAppName() {
		return appName;
	}

	public Archive getArchive() {
		return archive;
	}

	public Version getVersion() {
		return version;
	}

	public OsTarget getOsTarget() {
		return osTarget;
	}

	public AppStructure getAppStructure() {
		return appStructure;
	}

	public String getActivated() {
		return activated;
	}

	public String getInstallPath() {
		return installPath;
	}

	public String getSearchPaths() {
		return searchPaths;
	}

	public String getSearchAllDisks() {
		return searchAllDisks;
	}

	public UPDATE_METHOD getUpdateMethod() {
		return updateMethod;
	}

	public String getLocalPath() {
		return localPath;
	}

	public void setLocalPath(String localPath) {
		this.localPath = localPath;
	}

	@Override
	public int hashCode() {
		return appName.hashCode() + this.version.getValue().hashCode() + this.version.getDeliveredDate().hashCode();
	}

	public boolean validate() {
		return appStructure.validate();
	}

	public boolean checkContent() {
		boolean checkContent = true;
		if (!archive.checkContent()) {
			checkContent = false;
		}
		return checkContent;
	}

	public int compareTo(AppVersion o) {
		if (this.getAppName().equals(o.getAppName())) {
			return VersionUtil.compareVersion(o.getVersion().getValue(), this.getVersion().getValue());
		}
		return Integer.MIN_VALUE;
	}

	@Override
	public String toString() {
		return JSonUtil.toJSon(this, this.getClass());
	}

}
