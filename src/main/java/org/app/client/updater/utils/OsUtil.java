package org.app.client.updater.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

public final class OsUtil {

	public static enum OS_ID {
		WIN, LIN, MAC
	};

	public static String getEnvOs() {
		OS_ID[] values = OS_ID.values();
		for (OS_ID os_ID : values) {
			if (LocalEnv.osName.toLowerCase().contains(os_ID.name().toLowerCase())) {
				return os_ID.name();
			}
		}
		return null;
	}

	public static String getMacAddress() throws UnknownHostException, SocketException {
		InetAddress ip = InetAddress.getLocalHost();
		NetworkInterface network = NetworkInterface.getByInetAddress(ip);
		byte[] mac = network.getHardwareAddress();
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < mac.length; i++) {
			sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
		}
		return sb.toString();

	}
}
