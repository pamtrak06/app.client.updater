package org.app.client.updater.config.version;

import joptsimple.internal.Strings;

import org.apache.log4j.Logger;
import org.app.client.updater.config.IValidation;
import org.app.client.updater.config.version.VersionFinder.VERSION_FIND_METHOD;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.JSonUtil;

public class Version implements IValidation {

	private static Logger LOG = Logger.getLogger(Version.class);

	private final String value;
	private final String deliveredDate;
	private final VersionFinder versionFinder;

	public Version(String value, String deliveredDate, String findMethod, String parameter) {
		super();
		this.value = value;
		this.deliveredDate = deliveredDate;
		this.versionFinder = new VersionFinder(VERSION_FIND_METHOD.valueOf(findMethod), parameter);
	}

	public String getValue() {
		return value;
	}

	public String getDeliveredDate() {
		return deliveredDate;
	}

	public VersionFinder getVersionFinder() {
		return versionFinder;
	}

	public boolean validate() throws AcuException {

		// TODO validate version value
		return true;
	}

	@Override
	public boolean checkContent() {
		boolean status = true;

		if (Strings.isNullOrEmpty(value)) {
			LOG.error("value could not be empty");
			status = false;
		}

		if (Strings.isNullOrEmpty(deliveredDate)) {
			LOG.error("deliveredDate could not be empty");
			status = false;
		}

		if (deliveredDate.length() != 8) {
			LOG.error("deliveredDate syntax must be like YYYYMMDD");
			status = false;
		}

		return status;
	}

	@Override
	public String toString() {
		return JSonUtil.toJSon(this, this.getClass());
	}
}
