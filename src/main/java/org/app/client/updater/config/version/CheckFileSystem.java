package org.app.client.updater.config.version;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.Logger;
import org.app.client.updater.config.IValidation;
import org.app.client.updater.utils.FileUtil;
import org.app.client.updater.utils.JSonUtil;

public class CheckFileSystem implements IValidation {

	private static Logger LOG = Logger.getLogger(CheckFileSystem.class);

	public static enum CHECK_ELEMENT {
		CHECK_FOLDER, CHECK_FILE, CHECK_CHECKSUM
	};

	private final CHECK_ELEMENT checkElement;
	private final String fileSystemElement;
	private final String parameter;

	public CheckFileSystem(String checkElement, String path, String fileSystemElement, String parameter) {
		super();
		this.checkElement = CHECK_ELEMENT.valueOf(checkElement);
		this.fileSystemElement = FileUtil.endPath(path) + fileSystemElement;
		this.parameter = parameter;
	}

	public CHECK_ELEMENT getCheckElement() {
		return checkElement;
	}

	public String getFileSystemElement() {
		return fileSystemElement;
	}

	public String getParameter() {
		return parameter;
	}

	public boolean validate() {

		File file = new File(fileSystemElement);
		if (!file.exists()) {
			LOG.error("Checking File " + fileSystemElement + ", doesn't exist");
			return false;
		}

		switch (checkElement) {
		case CHECK_CHECKSUM:
			boolean status = false;
			try {
				status = parameter.equals(FileUtil.getChecksumMd5(file));
			} catch (IOException e) {
				LOG.error("Checksum MD5 (" + parameter + ") is incorrect for " + fileSystemElement);
				return false;
			}
			return status;
		case CHECK_FILE:
			if (file.isFile()) {
				return true;
			}
			LOG.error("Checking File " + parameter + " not true " + fileSystemElement);
			return false;
		case CHECK_FOLDER:
			// TODO take account about shortcut in win/lin/mac
			if (!file.isFile()) {
				return true;
			}
			LOG.error("Checking Folder " + parameter + " not true " + fileSystemElement);
			return false;
		default:
			// TODO logger
			return false;
		}
	}

	@Override
	public boolean checkContent() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String toString() {
		return JSonUtil.toJSon(this, this.getClass());
	}
}
