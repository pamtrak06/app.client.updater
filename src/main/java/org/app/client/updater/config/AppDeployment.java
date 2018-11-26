package org.app.client.updater.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.app.client.updater.config.update.DeploymentStrategy;
import org.app.client.updater.config.update.DeploymentStrategy.CRITERIA_LAST_VERSION;
import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.config.version.AppVersions;
import org.app.client.updater.config.version.OsTarget;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.JSonUtil;
import org.app.client.updater.utils.VersionUtil;

import com.fasterxml.jackson.annotation.JsonUnwrapped;

public class AppDeployment implements IValidation {

	private static Logger LOG = Logger.getLogger(AppDeployment.class);

	private final AppVersions appVersions;
	private DeploymentStrategy deploymentStrategy;

	public AppDeployment(final String appName, DeploymentStrategy deploymentStrategy) {
		super();
		this.appVersions = new AppVersions(appName);
		this.deploymentStrategy = deploymentStrategy;
	}

	public void addAppVersion(final AppVersion appVersion) {
		appVersions.addAppVersion(appVersion);
	}

	public void removeAppVersion(final AppVersion appVersion) {
		List<AppVersion> appVersionList = appVersions.getAppVersionList();
		for (AppVersion app : appVersionList) {
			if (app.equals(appVersion)) {
				appVersionList.remove(appVersion);
			}
		}
	}

	public List<AppVersion> findAllVersion(String version) {
		List<AppVersion> appVList = new ArrayList<AppVersion>();
		List<AppVersion> appVersionList = appVersions.getAppVersionList();
		for (AppVersion app : appVersionList) {
			if (VersionUtil.compareVersion(version, app.getVersion().getValue()) == 0) {
				appVList.add(app);
			}
		}
		return appVList;
	}

	public void setDeploymentStrategy(DeploymentStrategy deploymentStrategy) {
		this.deploymentStrategy = deploymentStrategy;
	}

	public DeploymentStrategy getDeploymentStrategy() {
		return deploymentStrategy;
	}

	public AppVersions getAppVersions() {
		return appVersions;
	}

	@Override
	public boolean validate() {
		boolean status = true;
		Map<String, Integer> mapCheck = new HashMap<String, Integer>();
		final List<AppVersion> applicationList = appVersions.getAppVersionList();

		for (AppVersion appVersion1 : applicationList) {
			final String versionValue1 = appVersion1.getVersion().getValue();
			for (AppVersion appVersion2 : applicationList) {
				final String versionValue2 = appVersion2.getVersion().getValue();
				if (versionValue1.equalsIgnoreCase(versionValue2)) {
					Integer value = mapCheck.get(versionValue1);
					if (value == null) {
						mapCheck.put(versionValue1, 1);
					} else {
						mapCheck.put(versionValue1, value + 1);
						LOG.error("Version: " + versionValue1 + " is duplicated");
						status = false;
					}
				}
			}
		}
		return status;
	}

	public AppVersion getLatestVersion(String osName, String osVersion) throws AcuException {
		AppVersion latestVersion = null;
		int nbActivated = 0;
		final List<AppVersion> applicationList = appVersions.getAppVersionList();

		if (!validate()) {
			throw new AcuException(IllegalArgumentException.class, this.getClass(), "application.versions.list.validation", "");
		}

		// Criteria for latest version is enabled = true
		if (CRITERIA_LAST_VERSION.ENABLED.equals(deploymentStrategy.getCriteriaLatestVersion())) {
			for (AppVersion appVersion : applicationList) {
				if (appVersion.getActivated().equalsIgnoreCase("true")) {
					latestVersion = appVersion;
					nbActivated++;
				}
			}

			if (nbActivated > 1) {
				throw new AcuException(IllegalArgumentException.class, this.getClass(), "application.versions.list.activated.multiple.error",
						nbActivated);
			}
			if (nbActivated == 0) {
				throw new AcuException(IllegalArgumentException.class, this.getClass(), "application.versions.list.activated.noOne.error", "");
			}
		}
		// Criteria for latest version is the upper version
		else if (CRITERIA_LAST_VERSION.UPPER_VERSION.equals(deploymentStrategy.getCriteriaLatestVersion())) {
			latestVersion = applicationList.get(0);
			for (AppVersion appVersion : applicationList) {
				final OsTarget osTarget = appVersion.getOsTarget();
				if (osTarget.getName().equals(osName) && (osVersion == null || osTarget.getVersion().equalsIgnoreCase(osVersion))) {
					if (appVersion.compareTo(latestVersion) > 0) {
						latestVersion = appVersion;
					}
				}
			}
		}// Criteria for latest version is the upper version
		else if (CRITERIA_LAST_VERSION.CHECKSUM.equals(deploymentStrategy.getCriteriaLatestVersion())) {
			// TODO implements checksum identification
			throw new AcuException(IllegalArgumentException.class, this.getClass(), "application.lastVersion.checksum.criteria.error", "");
		}

		LOG.info("Found last version to be installed: " + latestVersion.getVersion().getValue());
		LOG.debug(latestVersion.toString());
		return latestVersion;
	}

	@Override
	public boolean checkContent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String toString() {
		return JSonUtil.toFlatJSon(this, this.getClass());
	}
}
