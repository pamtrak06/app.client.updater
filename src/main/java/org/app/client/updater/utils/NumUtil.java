package org.app.client.updater.utils;

public final class NumUtil {

	public static boolean isNumeric(final String value) {
		try {
			Long.parseLong(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

}
