package org.app.client.updater.utils;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class VersionUtilTest {

	@BeforeClass
	public static void setUp() throws Exception {
	}

	@AfterClass
	public static void tearDown() throws Exception {
	}

	@Test
	public void testCompareVersionEquals() {
		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12.450", "V2.5.12.450"));

		Assert.assertEquals(0, VersionUtil.compareVersion("2.5.12.450", "V2.5.12.450"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12.450", "2.5.12.450"));

		Assert.assertEquals(0, VersionUtil.compareVersion("v2.5.12.450", "v2.5.12.450"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12.450", "v2.5.12.450"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12", "V2.5.12"));

		Assert.assertEquals(0, VersionUtil.compareVersion("2.5.12", "V2.5.12"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12", "2.5.12"));

		Assert.assertEquals(0, VersionUtil.compareVersion("v2.5.12", "v2.5.12"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12", "v2.5.12"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5", "V2.5"));

		Assert.assertEquals(0, VersionUtil.compareVersion("2.5", "V2.5"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5", "2.5"));

		Assert.assertEquals(0, VersionUtil.compareVersion("v2.5", "v2.5"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5", "v2.5"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2", "V2"));

		Assert.assertEquals(0, VersionUtil.compareVersion("2", "V2"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2", "2"));

		Assert.assertEquals(0, VersionUtil.compareVersion("v2", "v2"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2", "v2"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12.045", "v2.5.12.45"));

		Assert.assertEquals(0, VersionUtil.compareVersion("V2.5.12", "v2.5.12.00"));
	}

	@Test
	public void testCompareVersionUpper() {
		Assert.assertEquals(1, VersionUtil.compareVersion("V2.5.12.512", "V2.5.12.450"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V2.5.14.450", "V2.5.12.450"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V2.6.12.450", "V2.5.12.450"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V3.5.12.450", "V2.5.12.450"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V3.5.12", "V2.5.12.450"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V3.5", "V2.5.12.450"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V3", "V2.5.12.450"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V2.5.12.450", "V2.5"));

		Assert.assertEquals(1, VersionUtil.compareVersion("V2.5.12.450", "V2.5.12"));
		
		Assert.assertEquals(1, VersionUtil.compareVersion("r02", "r01"));
		
		Assert.assertEquals(1, VersionUtil.compareVersion("RELEASE02", "RELEASE01"));
		
		Assert.assertEquals(1, VersionUtil.compareVersion("RELEASE02", "01"));

	}

	@Test
	public void testCompareVersionLower() {
		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12.450", "V2.5.12.512"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12.450", "V2.5.14.450"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12.450", "V2.6.12.450"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12.450", "V3.5.12.450"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12.450", "V3.5.12"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12.450", "V3.5"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12.450", "V3"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5", "V2.5.12.450"));

		Assert.assertEquals(-1, VersionUtil.compareVersion("V2.5.12", "V2.5.12.450"));
	}

}
