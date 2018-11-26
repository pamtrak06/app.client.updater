package org.app.client.updater.mock;

import org.app.client.updater.config.HostConfig;
import org.app.client.updater.config.ProxyConfig;
import org.app.client.updater.config.update.UpdaterConfig;

public final class MockConfig {

	public static UpdaterConfig createUpdaterConfig(String appName) {
		HostConfig hostConfig = new HostConfig(MockServer.HOST, MockServer.PORT, "/" + appName + "-updater/");
		ProxyConfig proxyConfig = new ProxyConfig(MockServer.HOST, MockServer.PORT, "user", "password");
		String versionsFilename = appName + "-versions.json";
		UpdaterConfig config = new UpdaterConfig(hostConfig, proxyConfig, versionsFilename);
		return config;
	}

}
