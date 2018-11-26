package org.app.client.updater;

import org.app.client.updater.exception.AcuException;

public class Test {
	public static void main(String[] args) throws AcuException {
		AppClientUpdater appClientUpdater = AppClientUpdater.getInstance("muco", "http://localhost:8080/admin-console/", false, false);
		appClientUpdater.update();
	}
}