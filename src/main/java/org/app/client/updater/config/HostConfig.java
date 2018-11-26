package org.app.client.updater.config;

import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.FileUtil;

public class HostConfig extends Server {

	private final String path;

	public HostConfig(String hostName, String port, String path) {
		super(hostName, port);
		this.path = path;
	}

	public String getPath() {
		return path;
	}

	public String toUrlForm() {
		return FileUtil.addHttp(hostName) + ":" + port;
	}

	@Override
	public boolean validate() throws AcuException {
		return super.validate();
	}

	@Override
	public boolean checkContent() {
		// TODO Auto-generated method stub
		return true;
	}
}
