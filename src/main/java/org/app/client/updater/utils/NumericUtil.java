package org.app.client.updater.utils;

public final class NumericUtil {

	public static boolean isNumeric(final String value) {
		try {
			Long.parseLong(value);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isEqualsToZero(final String value) {
		if (isNumeric(value)) {
			return (Long.parseLong(value) == 0);
		}
		return false;
	}

}
