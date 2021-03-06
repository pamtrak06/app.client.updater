package org.app.client.updater.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

public class StreamPrinter implements Runnable {

	private static Logger LOG = Logger.getLogger(StreamPrinter.class);

	private final InputStream inputStream;

	StreamPrinter(InputStream inputStream) {
		this.inputStream = inputStream;
	}

	private BufferedReader getBufferedReader(InputStream is) {
		return new BufferedReader(new InputStreamReader(is));
	}

	public void run() {
		BufferedReader br = getBufferedReader(inputStream);
		String ligne = "";
		try {
			while ((ligne = br.readLine()) != null) {
				System.out.println(ligne);
			}
		} catch (IOException e) {
			LOG.error("Reading stream: " + e.getMessage());
		}
	}
}
