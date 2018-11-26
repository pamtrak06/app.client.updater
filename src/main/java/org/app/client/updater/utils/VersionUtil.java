package org.app.client.updater.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.osgi.framework.Version;

public final class VersionUtil {

	// TODO add monitoring of RC = Release Candidate, alpha, beta, GA = General
	// Availability

	public static String VERSION_PREFIX_LIST[] = new String[] { "version", "v", "release", "r" };
	public static String VERSION_SEPARATOR_LIST[] = new String[] { "-", "_", "" };

	public static enum COMPARE_CONSTANTS {
		LOWER(-1), EQUALS(0), UPPER(1), ERROR(Integer.MIN_VALUE);

		int value;

		COMPARE_CONSTANTS(final int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

	};

	public static boolean checkVersion(final String version) {
		try {
			Version parsedVersion1 = Version.parseVersion(formatVersion(version));
			if (parsedVersion1 == null) {
				return false;
			}
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}

	public static String formatVersion(final String version) {
		StringBuffer buffer = new StringBuffer();
		String formattedVersion = version.replaceAll(" ", "");
		
		for (String separator : VERSION_SEPARATOR_LIST) {
			for (String versionPrefix : VERSION_PREFIX_LIST) {
				if (formattedVersion.toLowerCase().startsWith(versionPrefix + separator)) {
					formattedVersion =  formattedVersion.substring(versionPrefix.length() + separator.length());
					break;
				}
			}
		}
		
		String[] split = formattedVersion.split("\\.");
		for (String val : split) {
			if (!NumericUtil.isEqualsToZero(val)) {
				buffer.append(val.replaceFirst("^0+(?!$)", ""));
				buffer.append(".");
			}
		}
		formattedVersion = buffer.substring(0, buffer.length() - 1);

		return formattedVersion;
	}

	public static int compareVersion(final String version1, final String version2) {
		Version parsedVersion1 = Version.parseVersion(formatVersion(version1));
		Version parsedVersion2 = Version.parseVersion(formatVersion(version2));
		int compareTo = parsedVersion1.compareTo(parsedVersion2);
		if (compareTo > 0) {
			return COMPARE_CONSTANTS.UPPER.getValue();
		} else if (compareTo < 0) {
			return COMPARE_CONSTANTS.LOWER.getValue();
		}
		return COMPARE_CONSTANTS.EQUALS.getValue();
	}

	// public static int compareVersion(final String version1, final String
	// version2) {
	// String[] splitVMin = null;
	// String[] splitVMax = null;
	//
	// if (Strings.isNullOrEmpty(version1) || Strings.isNullOrEmpty(version2)) {
	// throw new
	// IllegalArgumentException("Version argument could not be null or empty");
	// }
	//
	// String ver1 = extractVersion(version1);
	// String ver2 = extractVersion(version2);
	//
	// if (ver1.trim().equals(ver2.trim())) {
	// return COMPARE_CONSTANTS.EQUALS.getValue();
	// }
	//
	// String[] splitV1 = ver1.split("\\.");
	// String[] splitV2 = ver2.split("\\.");
	//
	// splitVMin = (splitV1.length < splitV2.length) ? splitV1 : splitV2;
	// splitVMax = (splitV1.length >= splitV2.length) ? splitV1 : splitV2;
	//
	// for (int i = 0; i < splitVMin.length; i++) {
	// final String trim1 = splitV1[i].trim();
	// final String trim2 = splitV2[i].trim();
	//
	// if (!NumericUtil.isNumeric(trim1) && i == 0) {
	// throw new
	// IllegalArgumentException("First argument of version must be numeric at least");
	// }
	// if (!NumericUtil.isNumeric(trim2) && i == 0) {
	// throw new
	// IllegalArgumentException("First argument of version must be numeric at least");
	// }
	// if (!NumericUtil.isNumeric(trim1) && i > 0) {
	// // TODO take account about alpha, beta, rc, GA, ...
	// throw new
	// IllegalArgumentException("Argument of version must be numeric at least");
	// }
	// if (!NumericUtil.isNumeric(trim2) && i > 0) {
	// // TODO take account about alpha, beta, rc, GA, ...
	// throw new
	// IllegalArgumentException("Argument of version must be numeric at least");
	// }
	//
	// int val1 = Integer.parseInt(trim1.replaceFirst("^0+(?!$)", ""));
	// int val2 = Integer.parseInt(trim2.replaceFirst("^0+(?!$)", ""));
	// if (val1 > val2) {
	// return COMPARE_CONSTANTS.UPPER.getValue();
	// } else if (val1 < val2) {
	// return COMPARE_CONSTANTS.LOWER.getValue();
	// } else if (i + 1 == splitVMin.length) {
	//
	// if (splitV1.length == splitV2.length) {
	// return COMPARE_CONSTANTS.EQUALS.getValue();
	// } else if (splitV1.length > splitV2.length) {
	// boolean upper = false;
	// for (int u = i + 1; u < splitVMax.length; u++) {
	// String last = splitVMax[u];
	// if (Integer.parseInt(last) != 0) {
	// upper = true;
	// break;
	// }
	// }
	// if (upper) {
	// return COMPARE_CONSTANTS.UPPER.getValue();
	// } else {
	// return COMPARE_CONSTANTS.EQUALS.getValue();
	// }
	// } else {
	// boolean upper = false;
	// for (int u = i + 1; u < splitVMax.length; u++) {
	// String last = splitVMax[u];
	// if (Integer.parseInt(last) != 0) {
	// upper = true;
	// break;
	// }
	// }
	// if (upper) {
	// return COMPARE_CONSTANTS.LOWER.getValue();
	// } else {
	// return COMPARE_CONSTANTS.EQUALS.getValue();
	// }
	// }
	// }
	// }
	//
	// return COMPARE_CONSTANTS.ERROR.getValue();
	// }

	// TODO deprecated ???
	public static String extractVersion(final String version) {
		String value = null;
		// <major>.<minor>.<release>.<build>
		String regex = "([a-z]*)?(([0-9]+\\.)?([0-9]+\\.)?([0-9]+\\.)?([0-9]+)?)";
		// String regex ="(V)?(.*)";
		Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(version);
		if (matcher.find()) {
			value = matcher.group(2);
		}
		return value;
	}
}
