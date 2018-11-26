package org.app.client.updater.utils;

import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.log4j.Logger;

public class ProxyUtil {

	private static Logger LOG = Logger.getLogger(ProxyUtil.class);

	public static InetSocketAddress retrieveProxySettings() throws URISyntaxException {
		System.setProperty("java.net.useSystemProxies", "true");
		Proxy proxy = (Proxy) ProxySelector.getDefault().select(new URI("http://www.google.fr/")).iterator().next();
		LOG.debug("proxy hostname : " + proxy.type());
		InetSocketAddress socketAddress = (InetSocketAddress) proxy.address();
		if (socketAddress == null) {
			LOG.debug("No Proxy defined");
		} else {
			LOG.debug("proxy hostname : " + socketAddress.getHostName());
			LOG.debug("proxy port : " + socketAddress.getPort());
		}
		return socketAddress;
	}

	public static void configureProxySettings() throws URISyntaxException {
		InetSocketAddress proxySettings = retrieveProxySettings();
		configureProxySettings(proxySettings);
	}

	public static void configureProxySettings(final InetSocketAddress socketAddress) {
		if (socketAddress != null) {
			configureProxySettings(socketAddress.getHostName(), String.valueOf(socketAddress.getPort()), null, null);
		}
	}

	public static void configureProxySettings(final InetSocketAddress socketAddress, final String authUser, final String authPassword) {
		configureProxySettings(socketAddress.getHostName(), String.valueOf(socketAddress.getPort()), authUser, authPassword);
	}

	public static void configureProxySettings(final String host, final String port, final String authUser, final String authPassword) {
		Authenticator.setDefault(new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(authUser, authPassword.toCharArray());
			}
		});
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", port);
		System.setProperty("http.proxySet", "true");
		System.setProperty("http.nonProxyHosts", "localhost|127.0.0.1");
	}

}
