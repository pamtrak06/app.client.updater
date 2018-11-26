package org.app.client.updater.config.version;

import org.app.client.updater.config.IValidation;
import org.app.client.updater.utils.JSonUtil;

public class OsTarget implements IValidation {
	private final String name;
	private final String version;
	private final String arch;

	public OsTarget(String name, String version, String arch) {
		super();
		this.name = name;
		this.version = version;
		this.arch = arch;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getArch() {
		return arch;
	}

	public boolean validate() {
		// TODO Auto-generated method stub
		return true;
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
