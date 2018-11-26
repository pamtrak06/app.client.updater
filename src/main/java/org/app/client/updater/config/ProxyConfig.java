package org.app.client.updater.config;

import org.app.client.updater.exception.AcuException;

public class ProxyConfig extends Server {
	
	private final String username;
	private final String pasword;

	public ProxyConfig(String hostName, String port, String username, String pasword) {
		super(hostName, port);
		this.username = username;
		this.pasword = pasword;
	}

	public String getUsername() {
		return username;
	}

	public String getPasword() {
		return pasword;
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
