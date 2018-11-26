package org.app.client.updater.config.version;

import java.util.Arrays;

import joptsimple.internal.Strings;

import org.apache.log4j.Logger;
import org.app.client.updater.config.IValidation;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.JSonUtil;

public class VersionFinder implements IValidation {

	private static Logger LOG = Logger.getLogger(VersionFinder.class);

	public static enum VERSION_FIND_METHOD {
		GETVERSION_FROM_FILENAME_SYNTAX, GETVERSION_FROM_FILENAME_CRC, GETVERSION_FROM_XML, GETVERSION_FROM_PROPERTIES, GETVERSION_FROM_RUN
	};

	// private final String versionFilename;
	private final VERSION_FIND_METHOD findMethod;
	private final String parameter;
	final String comment = "findMethod : method to find version from filename: " + Arrays.asList(VERSION_FIND_METHOD.values()) + JSonUtil.CR_SYMBOL
			+ "parameter : for findMethod GETVERSION_FROM_FILENAME_SYNTAX : regex with version in group 1" + JSonUtil.CR_SYMBOL
			+ "parameter : for findMethod GETVERSION_FROM_FILENAME_CRC : checksum value" + JSonUtil.CR_SYMBOL
			+ "parameter : for findMethod GETVERSION_FROM_XML : XPath to version value" + JSonUtil.CR_SYMBOL
			+ "parameter : for findMethod GETVERSION_FROM_PROPERTIES : marker to get version value" + JSonUtil.CR_SYMBOL
			+ "parameter : for findMethod GETVERSION_FROM_RUN : command to run to retrieve version";

	public VersionFinder(VERSION_FIND_METHOD findMethod, String parameter) {
		super();
		this.findMethod = findMethod;
		this.parameter = parameter;

	}

	public VERSION_FIND_METHOD getFindMethod() {
		return findMethod;
	}

	public String getParameter() {
		return parameter;
	}

	public boolean validate() throws AcuException {

		// if (!FileUtil.exists(versionFilename)) {
		// throw new AcuException(FileNotFoundException.class, this.getClass(),
		// "file: " + versionFilename
		// + " doesn't exist");
		// }

		return true;
	}

	@Override
	public boolean checkContent() {
		boolean status = true;

		if (VERSION_FIND_METHOD.valueOf(findMethod.toString()) != null) {
			LOG.error("wrong find method: " + findMethod.toString() + " specified");
			status = false;
		}

		if (Strings.isNullOrEmpty(parameter)) {
			LOG.error("parameter could not be empty");
			status = false;
		}

		return status;
	}

	@Override
	public String toString() {
		return JSonUtil.toJSon(this, this.getClass());
	}

}
