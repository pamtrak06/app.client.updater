package org.app.client.updater.config.update;

import java.util.ArrayList;
import java.util.List;

import org.app.client.updater.config.HostConfig;
import org.app.client.updater.config.IValidation;
import org.app.client.updater.config.ProxyConfig;
import org.app.client.updater.config.update.DeploymentStrategy.CRITERIA_LAST_VERSION;
import org.app.client.updater.exception.AcuException;
import org.app.client.updater.utils.JSonUtil;

public class UpdaterConfig implements IValidation {

	private final List<UpdateServers> updateServers = new ArrayList<UpdateServers>();
	private final String versionsFilename;
	private DeploymentStrategy deploymentStrategy;

	public UpdaterConfig(HostConfig hostConfig, ProxyConfig proxyConfig, String versionsFilename) {
		super();
		this.updateServers.add(new UpdateServers(hostConfig, proxyConfig));
		this.versionsFilename = versionsFilename;
		this.deploymentStrategy = new DeploymentStrategy(CRITERIA_LAST_VERSION.ENABLED, Boolean.toString(false));
	}

	public UpdaterConfig(List<HostConfig> hostConfigs, List<ProxyConfig> proxyConfigs, String versionsFilename) throws AcuException {
		super();

		if (hostConfigs.size() != proxyConfigs.size()) {
			throw new AcuException(IllegalArgumentException.class, this.getClass(), "application.configuration.proxy.list.error", "");
		}
		int index = 0;
		for (HostConfig hostConfig : hostConfigs) {
			this.updateServers.add(new UpdateServers(hostConfig, proxyConfigs.get(index)));
			index++;
		}
		this.versionsFilename = versionsFilename;
		this.deploymentStrategy = new DeploymentStrategy(CRITERIA_LAST_VERSION.ENABLED, Boolean.toString(false));
	}

	public List<UpdateServers> getUpdateServers() {
		return updateServers;
	}

	public String getVersionsFilename() {
		return versionsFilename;
	}

	public DeploymentStrategy getDeploymentStrategy() {
		return deploymentStrategy;
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
