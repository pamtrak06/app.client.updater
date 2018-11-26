package org.app.client.updater.config.update;

import java.util.Arrays;

import org.app.client.updater.config.IValidation;

public class DeploymentStrategy implements IValidation {

	public static enum CRITERIA_LAST_VERSION {
		ENABLED, UPPER_VERSION, CHECKSUM
	}

	private final CRITERIA_LAST_VERSION criteriaLatestVersion;
	private final String forceInstallation;
	final String comment = "Available values for criteriaLatestVersion :" + Arrays.toString(CRITERIA_LAST_VERSION.values());

	public DeploymentStrategy(CRITERIA_LAST_VERSION criteriaLatestVersion, String forceInstallation) {
		super();
		this.criteriaLatestVersion = criteriaLatestVersion;
		this.forceInstallation = forceInstallation;
	}

	public CRITERIA_LAST_VERSION getCriteriaLatestVersion() {
		return criteriaLatestVersion;
	}

	public String getForceInstallation() {
		return forceInstallation;
	}

	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean checkContent() {
		// TODO Auto-generated method stub
		return true;
	};

}
