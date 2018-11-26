package org.app.client.updater.utils;

import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.log4j.Logger;

import com.google.common.io.Resources;

public final class UiUtil {

	private static Logger LOG = Logger.getLogger(UiUtil.class);

	private static ImageIcon APP_ICON = new ImageIcon(Resources.getResource("acu.png"));
	private static String TITLE = ResourceUtil.getI18Message("application.dialog.name");

	public static boolean SILENT_MODE = false;

	public static final int YES = JOptionPane.YES_OPTION;
	public static final int NO = JOptionPane.NO_OPTION;

	public static void setLookAndFeel() {
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			try {
				UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (InstantiationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IllegalAccessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (UnsupportedLookAndFeelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}

	public static int showConfirm(String title, String message) {
		LOG.info(title + ", " + message);
		if (SILENT_MODE) {
			return YES;
		}
		return JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, APP_ICON);
	}

	public static File showFolderChooser(String title, String message, String defaultPath) {
		return showChooser(title, message, defaultPath, JFileChooser.DIRECTORIES_ONLY);
	}

	public static File showFileChooser(String title, String message, String defaultPath) {
		return showChooser(title, message, defaultPath, JFileChooser.FILES_ONLY);
	}

	public static File showChooser(String title, String message, String defaultPath, int chooserType) {
		File outputFile = null;
		final JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(chooserType);
		// fileChooser.setApproveButtonText("Export");
		int returnVal = fileChooser.showSaveDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			outputFile = fileChooser.getSelectedFile();
		}

		return outputFile;
	}

	public static String showInput(String title, String message, Object[] selectionValues, Object initialSelectionValue) {
		LOG.info(title + ", " + message);
		if (SILENT_MODE) {
			return null;
		}
		return (String) JOptionPane.showInputDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, APP_ICON, selectionValues,
				initialSelectionValue);
	}

	public static void showError(String title, String message) {
		LOG.error(title + ", " + message);
		if (SILENT_MODE) {
			return;
		}

		JOptionPane.showMessageDialog(null, message, title, JOptionPane.ERROR_MESSAGE, APP_ICON);

	}

	public static void showInformation(String title, String message) {
		LOG.info(title + ", " + message);
		if (SILENT_MODE) {
			return;
		}
		JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE, APP_ICON);

	}

	public static int showConfirmI18n(String message) {
		return showConfirm(TITLE, message);
	}

	public static File showFolderChooserI18n(String message, String defaultPath) {
		return showFolderChooser(TITLE, message, defaultPath);
	}

	public static File showFileChooserI18n(String message, String defaultPath) {
		return showFileChooser(TITLE, message, defaultPath);
	}

	public static File showChooserI18n(String message, String defaultPath, int chooserType) {
		return showChooser(TITLE, message, defaultPath, chooserType);
	}

	public static String showInputI18n(String message, Object[] selectionValues, Object initialSelectionValue) {
		return showInput(TITLE, message, selectionValues, initialSelectionValue);
	}

	public static void showErrorI18n(String message) {
		showError(TITLE, message);
	}

	public static void showInformationI18n(String message) {
		showInformation(TITLE, message);
	}

}
