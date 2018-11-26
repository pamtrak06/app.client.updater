package org.app.client.updater.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

public final class PropertiesUtil {

	private static Logger LOG = Logger.getLogger(PropertiesUtil.class);
	private static final String EXTENSION = ".properties";
	private static Map<String, Properties> mapProperties = new HashMap<String, Properties>();

	private static String endExtension(final String resourceProperties) {
		String resName = resourceProperties;
		if (!resourceProperties.endsWith(EXTENSION)) {
			resName += EXTENSION;
		}
		return resName;
	}

	private static String endOsExtension(final String resourceProperties) {
		String resName = null;
		if (!resourceProperties.endsWith(EXTENSION)) {
			resName = endExtension(resourceProperties + "-" + OsUtil.getEnvOs().toLowerCase());
		} else {
			resName = endExtension(resourceProperties.substring(0, resourceProperties.indexOf(EXTENSION)) + "-"
					+ OsUtil.getEnvOs().toLowerCase());
		}
		return resName;
	}

	private static Properties readProperties(final String resourceProperties) {
		
		Properties properties = mapProperties.get(resourceProperties);

		if (properties == null) {
			URL url = Resources.getResource(endExtension(resourceProperties));
			InputSupplier<InputStream> inputSupplier = Resources.newInputStreamSupplier(url);
			properties = new Properties();
			try {
				properties.load(inputSupplier.getInput());
			} catch (IOException e) {
				LOG.error("Error loading internal properties file: " + resourceProperties + ", cause: " + e.toString());
			}
			mapProperties.put(resourceProperties, properties);
		}
		return properties;
	}

	private static Properties readOsProperties(final String resourceProperties) {
		return readProperties(endOsExtension(resourceProperties));
	}

	public static String getOsProperty(final String resourceProperties, final String property) {
		return readOsProperties(resourceProperties).getProperty(property);
	}
	
	public static List<String> getOsPropertyList(final String resourceProperties, final String property) {
		return Arrays.asList(getOsProperty(resourceProperties, property).split(";"));
	}
}
