package org.app.client.updater.utils;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class CryptUtilTest {

	private static final String SECRET_WORD = "UserTest@";
	private static final String SECRET_MESSAGE = "Secret@Mess@ge";
	private static final String SECRET_MESSAGE_ENCODED = "pkhKTPtYBLcvWvOszU/1t9AcRIexnxgU";

	@BeforeClass
	public static void setUp() throws Exception {
		CryptUtil.definePassword(SECRET_WORD);
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	@Test
	public void testEncodePassword() {
		String encodePassword = CryptUtil.encodePassword(SECRET_WORD);
		Assert.assertNotNull(encodePassword);
	}

	@Test
	public void testCheckPassword() {
		String encodePassword = CryptUtil.encodePassword(SECRET_WORD);
		boolean checkPassword = CryptUtil.checkPassword(SECRET_WORD, encodePassword);
		Assert.assertTrue(checkPassword);
	}

	@Test
	public void testEncodMessage() {
		String encodMessage = CryptUtil.encodMessage(SECRET_MESSAGE);
		Assert.assertNotNull(encodMessage);
	}

	@Test
	public void testDecodeMessage() {
		// TODO to be implemented and used later
		//String decodeMessage = CryptUtil.decodeMessage(SECRET_MESSAGE_ENCODED);
		//Assert.assertEquals(decodeMessage, SECRET_MESSAGE);
	}

}
