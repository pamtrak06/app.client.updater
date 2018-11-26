package org.app.client.updater.config.update;

import org.app.client.updater.config.HostConfig;
import org.app.client.updater.config.ProxyConfig;

public class UpdateServers {
	
	private final HostConfig hostConfig;
	private final ProxyConfig proxyConfig;

	public UpdateServers(HostConfig hostConfig, ProxyConfig proxyConfig) {
		super();
		this.hostConfig = hostConfig;
		this.proxyConfig = proxyConfig;
	}

	public HostConfig getHostConfig() {
		return hostConfig;
	}

	public ProxyConfig getProxyConfig() {
		return proxyConfig;
	}

}
