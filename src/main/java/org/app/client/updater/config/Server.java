package org.app.client.updater.config;

import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.NumericUtil;

public abstract class Server implements IValidation {

	protected final String hostName;
	protected final String port;

	public Server(String hostName, String port) {
		super();
		this.hostName = hostName;
		this.port = port;
	}

	public String getHostName() {
		return hostName;
	}

	public String getPort() {
		return port;
	}

	@Override
	public boolean validate() throws AcuException {
		// TODO validation
		if (hostName.contains("\\")) {
			// invalid host name separator
		}

		if (!port.isEmpty() && !NumericUtil.isNumeric(port)) {
			// invalid port value
		}
		return false;
	}

}
