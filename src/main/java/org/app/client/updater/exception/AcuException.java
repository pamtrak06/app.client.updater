package org.app.client.updater.exception;

import org.apache.log4j.Logger;
import org.app.client.updater.AppClientUpdater;
import org.app.client.updater.UpdaterNotifier;
import org.app.client.updater.utils.FileUtil;
import org.app.client.updater.utils.ResourceUtil;
import org.app.client.updater.utils.UiUtil;

// TODO to be implemented
public class AcuException extends Exception {

	private static final long serialVersionUID = 149788183909501063L;
	private static Logger LOG = Logger.getLogger(AcuException.class);

	public AcuException(Class<?> exceptionClass, Class<?> classInstance, String message) {

		super("Exception: " + exceptionClass.getSimpleName() + ", Class: " + classInstance.getClass().getCanonicalName() + "\n" + message);

		String logMessage = "Exception: " + exceptionClass.getSimpleName() + ", Class: " + classInstance.getClass().getCanonicalName() + "\n"
				+ message;
		LOG.error(logMessage);

		UiUtil.showError(ResourceUtil.DIALOG_TITLE + "error", logMessage);

		AppClientUpdater instance = AppClientUpdater.getInstance();
		instance.setNextStep(AppClientUpdater.UPDATE_STEPS.ABORT);
		// TODO send notification to server, how to get server url ?
		try {
			UpdaterNotifier.sendNotification(instance.getLatestVersion(), instance.getTempFolder(), instance.getConfigPath(), instance.getAppName(), "ERROR", false);
		} catch (AcuException e) {
			UiUtil.showError(ResourceUtil.DIALOG_TITLE + "error", "Send notification failed");
		}
		// TODO close stream properly

		FileUtil.rmDirectory(FileUtil.createFolderTemporary("."), true);
	}

	public AcuException(Class<?> exceptionClass, Class<?> classInstance, String key, String... arguments) {
		this(exceptionClass, classInstance, ResourceUtil.getI18Message(key, arguments));
	}

	public AcuException(Class<?> exceptionClass, Class<?> classInstance, String key, int arguments) {
		this(exceptionClass, classInstance, ResourceUtil.getI18Message(key, String.valueOf(arguments)));
	}

}
