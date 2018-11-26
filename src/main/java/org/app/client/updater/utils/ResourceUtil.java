package org.app.client.updater.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

import org.apache.commons.lang.LocaleUtils;

import com.google.common.io.InputSupplier;
import com.google.common.io.Resources;

public final class ResourceUtil {

	public static final String DIALOG_TITLE = "Application Client Updater: ";
	private static Locale currentLocale_;
	private static ResourceBundle I18N;

	public static InputStream loadResource(final String resourceName) throws IOException {
		URL url = Resources.getResource(resourceName);
		InputSupplier<InputStream> inputStream = Resources.newInputStreamSupplier(url);
		return inputStream.getInput();
	}

	public static void initI18n(final String locale) {
		currentLocale_ = LocaleUtils.toLocale(locale);
		I18N = ResourceBundle.getBundle("MessagesBundle", currentLocale_);
	}
	
	public static void initI18n() {
		if (currentLocale_ == null) {
			currentLocale_ = Locale.getDefault();
			I18N = ResourceBundle.getBundle("MessagesBundle", currentLocale_);
		}
	}
	
	public static String getI18Message(final String key) {
		initI18n();
		return I18N.getString(key);
	}
	
	public static String getI18Message(final String key, final String argument) {
		initI18n();
		return I18N.getString(key).replace("{0}", argument);
	}
	
	public static String getI18Message(final String key, final String ... arguments) {
		int index = 0;
		
		initI18n();
		
		String message = I18N.getString(key);
		for (String arg : arguments) {
			message = message.replace("{"+index+"}", arg);
			index++;
		}
		return message;
	}

}
