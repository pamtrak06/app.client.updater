package org.app.client.updater.config.version;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.app.client.updater.config.IValidation;
import org.app.client.updater.utils.JSonUtil;
import org.app.client.updater.utils.VersionUtil;

public class AppVersions implements IValidation {

	private static Logger LOG = Logger.getLogger(AppVersions.class);

	private List<AppVersion> appVersionList = new ArrayList<AppVersion>();
	private final String appName;

	public AppVersions(String appName) {
		super();
		this.appName = appName;
	}

	public void addAppVersion(final AppVersion appVersion) {
		appVersionList.add(appVersion);
	}

	public void removeAppVersion(final AppVersion appVersion) {
		for (AppVersion app : appVersionList) {
			if (app.equals(appVersion)) {
				appVersionList.remove(appVersion);
			}
		}
	}

	public List<AppVersion> findAllVersion(String version) {
		List<AppVersion> appVersionLIst = new ArrayList<AppVersion>();
		for (AppVersion app : appVersionList) {
			if (VersionUtil.compareVersion(version, app.getVersion().getValue()) == 0) {
				appVersionLIst.add(app);
			}
		}
		return appVersionLIst;
	}

	public List<AppVersion> getAppVersionList() {
		return appVersionList;
	}

	public String getAppName() {
		return appName;
	}

	public boolean validate() {
		// TODO implements
		return true;
	}

	@Override
	public boolean checkContent() {
		boolean status = !appVersionList.isEmpty();
		if (!status) {
			LOG.error("List of versions must not be empty");
		}
		return status;
	}

	@Override
	public String toString() {
		return JSonUtil.toJSon(this, this.getClass());
	}
}
