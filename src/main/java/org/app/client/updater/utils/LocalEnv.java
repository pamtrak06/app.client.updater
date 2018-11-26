package org.app.client.updater.utils;

// TODO read/write to json

public final class LocalEnv {
	// Operating system name
	public final static String osName = System.getProperty("os.name");
	// Operating system architecture
	public final static String osArch = System.getProperty("os.arch");
	// Operating system version
	public final static String osVersion = System.getProperty("os.version");
	// File separator ("/" on UNIX)
	public final static String fileSeparator = System.getProperty("file.separator");
	// Path separator (":" on UNIX)
	public final static String pathSeparator = System.getProperty("path.separator");
	// Line separator ("\n" on UNIX)
	public final static String lineSeparator = System.getProperty("line.separator");
	// User's account name
	public final static String userName = System.getProperty("user.name");
	// User's home directory
	public final static String userHome = System.getProperty("user.home");
	// User's current working directory
	public final static String userDir = System.getProperty("user.dir");


	// java.version Java Runtime Environment version
	// java.vendor Java Runtime Environment vendor
	// java.vendor.url Java vendor URL
	// java.home Java installation directory
	// java.vm.specification.version Java Virtual Machine specification version
	// java.vm.specification.vendor Java Virtual Machine specification vendor
	// java.vm.specification.name Java Virtual Machine specification name
	// java.vm.version Java Virtual Machine implementation version
	// java.vm.vendor Java Virtual Machine implementation vendor
	// java.vm.name Java Virtual Machine implementation name
	// java.specification.version Java Runtime Environment specification version
	// java.specification.vendor Java Runtime Environment specification vendor
	// java.specification.name Java Runtime Environment specification name
	// java.class.version Java class format version number
	// java.class.path Java class path
	// java.library.path List of paths to search when loading libraries
	// java.io.tmpdir Default temp file path
	public final static String javaIoTmpDir = System.getProperty("java.io.tmpdir");
	// java.compiler Name of JIT compiler to use
	// java.ext.dirs Path of extension directory or directories

}
