package org.app.client.updater.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class ExecUtil {

	public static String run(final String cmds[]) throws IOException {
		StringBuffer buffer = new StringBuffer();
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(cmds);
		process.getOutputStream().close();
		InputStream inputStream = process.getInputStream();
		InputStreamReader inputstreamreader = new InputStreamReader(inputStream);
		BufferedReader bufferedrReader = new BufferedReader(inputstreamreader);
		String strLine = "";
		while ((strLine = bufferedrReader.readLine()) != null) {
			buffer.append(strLine);
		}
		return buffer.toString();
	}

	public static void runMain(String[] commands) {
		try {
			Process p = Runtime.getRuntime().exec(commands);
			StreamPrinter fluxSortie = new StreamPrinter(p.getInputStream());
			StreamPrinter fluxErreur = new StreamPrinter(p.getErrorStream());

			new Thread(fluxSortie).start();
			new Thread(fluxErreur).start();

			p.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
