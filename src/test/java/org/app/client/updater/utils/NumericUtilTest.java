package org.app.client.updater.utils;


import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class NumericUtilTest {

	@BeforeClass
	public static void setUp() throws Exception {
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	@Test
	public void testIsNumeric() {
		Assert.assertTrue(NumericUtil.isNumeric("0"));
		
		Assert.assertTrue(NumericUtil.isNumeric("000"));
		
		Assert.assertTrue(NumericUtil.isNumeric(String.valueOf(Integer.MAX_VALUE)));
		
		Assert.assertTrue(NumericUtil.isNumeric(String.valueOf(Integer.MIN_VALUE)));
		
		Assert.assertTrue(NumericUtil.isNumeric(String.valueOf(Long.MAX_VALUE)));
		
		Assert.assertTrue(NumericUtil.isNumeric(String.valueOf(Long.MIN_VALUE)));
		
		Assert.assertTrue(NumericUtil.isNumeric(String.valueOf(Short.MAX_VALUE)));
		
		Assert.assertTrue(NumericUtil.isNumeric(String.valueOf(Short.MIN_VALUE)));
		
		Assert.assertFalse(NumericUtil.isNumeric(String.valueOf(Double.MAX_VALUE)));
		
		Assert.assertFalse(NumericUtil.isNumeric(String.valueOf(Double.MIN_VALUE)));
		
		Assert.assertFalse(NumericUtil.isNumeric(String.valueOf(Float.MAX_VALUE)));
		
		Assert.assertFalse(NumericUtil.isNumeric(String.valueOf(Float.MIN_VALUE)));
		
		Assert.assertFalse(NumericUtil.isNumeric("abc"));
	}

}
