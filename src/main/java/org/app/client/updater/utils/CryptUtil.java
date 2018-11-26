package org.app.client.updater.utils;

import org.jasypt.util.password.BasicPasswordEncryptor;
import org.jasypt.util.text.BasicTextEncryptor;

public final class CryptUtil {
	public static BasicPasswordEncryptor PASSWORD_ENCRYPTOR = new BasicPasswordEncryptor();
	public static BasicTextEncryptor TEXT_ENCRYPTOR = new BasicTextEncryptor();

	public static String encodePassword(final String userPassword) {
		String encryptedPassword = PASSWORD_ENCRYPTOR.encryptPassword(userPassword);
		return encryptedPassword;
	}

	public static boolean checkPassword(final String inputPassword, final String encryptedPassword) {
		if (PASSWORD_ENCRYPTOR.checkPassword(inputPassword, encryptedPassword)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static void definePassword(final String password) {
		//TEXT_ENCRYPTOR = new BasicTextEncryptor();
		TEXT_ENCRYPTOR.setPassword(password);
	}

	public static String encodMessage(final String plainText) {
		String myEncryptedText = TEXT_ENCRYPTOR.encrypt(plainText);
		return myEncryptedText;
	}

	public static String decodeMessage(final String myEncryptedText) {
		String plainText = TEXT_ENCRYPTOR.decrypt(myEncryptedText);
		return plainText;
	}

}
