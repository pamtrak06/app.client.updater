package org.app.client.updater.config.version;

import org.apache.log4j.Logger;
import org.app.client.updater.config.IValidation;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.JSonUtil;

public class Archive implements IValidation {

	private static Logger LOG = Logger.getLogger(Archive.class);

	public static final String ARCHIVE_EXTENSION = ".glis";

	private final String name;
	private final String path;
	private final String mainRun;

	public Archive(String name, String path, String mainRun) {
		super();
		this.name = name;
		this.path = path;
		this.mainRun = mainRun;
	}

	public String getName() {
		return name;
	}

	public String getPath() {
		return path;
	}

	public String getMainRun() {
		return mainRun;
	}

	public boolean checkContent() {
		boolean status = name.endsWith(ARCHIVE_EXTENSION);
		if (!status) {
			LOG.error("Wrong archive extension for archive: " + name);
		}
		return status;
	}

	public boolean validate() throws AcuException {
		// TODO validate presence of archive, but where oninstalled path, with
		// remote url or server filesystem ?
		return true;
	}

	@Override
	public String toString() {
		return JSonUtil.toJSon(this, this.getClass());
	}
}
