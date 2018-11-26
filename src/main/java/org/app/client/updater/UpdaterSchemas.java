package org.app.client.updater;

import java.io.IOException;

import org.app.client.updater.config.AppDeployment;
import org.app.client.updater.config.update.UpdaterConfig;
import org.app.client.updater.config.version.AppVersion;
import org.app.client.updater.utils.FileUtil;
import org.app.client.updater.utils.JSonUtil;

import com.fasterxml.jackson.core.JsonProcessingException;

public class UpdaterSchemas {

	public static void generate(final String path) throws JsonProcessingException, IOException {
		JSonUtil.createSchema(buildPath(path, AppDeployment.class), AppDeployment.class);
		JSonUtil.createSchema(buildPath(path, AppVersion.class), AppVersion.class);
		JSonUtil.createSchema(buildPath(path, UpdaterConfig.class), UpdaterConfig.class);
	}

	public static String buildPath(final String path, Class<?> clazz) {
		return FileUtil.endPath(path) + clazz.getSimpleName() + JSonUtil.EXTENSION;
	}

	public static void main(String[] args) throws JsonProcessingException, IOException {
		String path = args[0];

		FileUtil.cleanDirectory(path);
		FileUtil.mkdirs(path);

		generate(path);
	}
}
