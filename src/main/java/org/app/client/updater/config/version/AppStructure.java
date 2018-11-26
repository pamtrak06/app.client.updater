package org.app.client.updater.config.version;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.app.client.updater.config.IValidation;
import org.app.client.updater.utils.JSonUtil;

public class AppStructure implements IValidation {

	private static Logger LOG = Logger.getLogger(AppStructure.class);

	private List<CheckFileSystem> checkList = new ArrayList<CheckFileSystem>();

	public AppStructure() {
		super();
	}

	public void addCheckFileSystem(final String checkElement, final String path, final String fileSystemElement,
			final String parameter) {
		checkList.add(new CheckFileSystem(checkElement, path, fileSystemElement, parameter));
	}

	public void removeCheckFileSystem(final CheckFileSystem checkExist) {
		for (CheckFileSystem check : checkList) {
			if (check.equals(checkExist)) {
				checkList.remove(checkExist);
			}
		}
	}

	public List<CheckFileSystem> getCheckList() {
		return checkList;
	}

	public boolean checkContent() {
		return true;
	}

	public boolean validate() {
		boolean status = true;
		for (CheckFileSystem checkFileSystem : checkList) {
			if (!checkFileSystem.validate()) {
				LOG.error("Checking file system: " + JSonUtil.toFlatJSon(checkFileSystem, CheckFileSystem.class));
				status = false;
			}
		}
		return status;
	}

	@Override
	public String toString() {
		return JSonUtil.toJSon(this, this.getClass());
	}
}
