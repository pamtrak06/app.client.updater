package org.app.client.updater.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.log4j.Logger;

import com.google.common.io.Files;

public final class FileUtil {

	private static Logger LOG = Logger.getLogger(FileUtil.class);

	public static String getChecksumMd5(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String md5 = DigestUtils.md5Hex(fis);
		return md5;
	}

	public static String getChecksumSha1(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		String sha1 = DigestUtils.sha1Hex(fis);
		return sha1;
	}

	public static String endPath(final String path) {
		String pathFilled = path;
		if (!path.endsWith(File.separator) && !path.endsWith("/")) {
			pathFilled += File.separator;
		}
		return pathFilled;
	}

	public static String endRemotePath(final String path) {
		String pathFilled = path;
		if (!path.endsWith("/")) {
			pathFilled += "/";
		}
		return pathFilled;
	}

	public static List<File> getActiveDriveList(String driveList[]) {
		List<File> listDrive = new ArrayList<File>();

		final File[] roots = File.listRoots();
		for (File file : roots) {
			for (String driveLetter : driveList) {
				if (file.getAbsolutePath().toLowerCase().startsWith(driveLetter.toLowerCase())) {
					listDrive.add(file);
				}
			}
		}
		return listDrive;
	}

	public static List<File> getDriveList() {
		List<File> listDrive = new ArrayList<File>();
		final File[] roots = File.listRoots();
		for (File file : roots) {
			listDrive.add(file);
		}
		return listDrive;
	}

	public static boolean driveExist(String driveLetter) {
		final File[] roots = File.listRoots();
		for (File file : roots) {
			if (file.getName().startsWith(driveLetter)) {
				return true;
			}
		}
		return false;
	}

	public static String getDrive(File file) {
		final File[] roots = File.listRoots();
		for (File root : roots) {
			if (file.getAbsolutePath().startsWith(root.getPath())) {
				return root.getPath();
			}
		}
		return null;
	}

	public static File getDrive(String driveLetter) {
		final File[] roots = File.listRoots();
		if (driveLetter != null && !"".equals(driveLetter.trim())) {
			for (File root : roots) {
				if (root.toString().toLowerCase().startsWith(driveLetter.toLowerCase())) {
					return root;
				}
			}
		}

		// TODO throw exception
		return null;
	}

	public static boolean exists(final String path) {
		return new File(path).exists();
	}

	public static File toFile(final String path) {
		return new File(path);
	}

	public static String getFileName(final String path) {
		return new File(path).getName();
	}

	public static boolean mkdirs(final String path) {
		boolean mkdirs = true;
		if (!exists(path)) {
			mkdirs = new File(path).mkdirs();
			if (!mkdirs) {
				LOG.error("Creating folder: " + path + " failed");
			}
		}
		return mkdirs;
	}

	public static boolean cleanDirectory(final String path) {
		return rmDirectory(path, false);
	}

	public static boolean rmDirectory(final String path, boolean deleteRootPath) {
		boolean mkdirs = true;

		LOG.debug("Deleting files from path: " + path);
		if (exists(path)) {
			File fs = new File(path);
			File[] listFiles = fs.listFiles();
			for (File file : listFiles) {
				if (file.isFile()) {
					if (!file.delete()) {
						LOG.error("File: " + file.getPath() + "was not deleted");
					}
				} else {
					rmDirectory(file.getPath(), true);
				}
			}
			if (deleteRootPath) {
				LOG.debug("Deleting folder: " + fs.getPath());
				if (!fs.delete()) {
					LOG.error("Folder: " + fs.getPath() + "was not deleted");
				}
			}
		}
		return mkdirs;
	}

	public static boolean copyFile(final String pathFrom, final String pathTo) {
		File to = new File(pathTo);
		File from = new File(pathFrom);
		if (!from.exists() || !to.exists()) {
			if (!from.isFile()) {
				LOG.error("File from: " + from.getPath() + " doesn't exist");
			}
			if (!to.isFile()) {
				LOG.error("File to: " + to.getPath() + " doesn't exist");
			}
			return false;
		}
		if (!from.isFile() || !to.isDirectory()) {
			if (!from.isFile()) {
				LOG.error("File from: " + from.getPath() + " is not a file");
			}
			if (!to.isDirectory()) {
				LOG.error("Folder to: " + to.getPath() + " is not a directory");
			}
			return false;
		}
		try {
			Files.copy(from, new File(endPath(to.getPath()) + from.getName()));
		} catch (IOException e) {
			LOG.error("Copy file: " + from.getPath() + " to: " + to.getPath() + " failed");
			return false;
		}
		return true;
	}

	public static boolean copyFolder(final String srcDir, final String destDir) {
		File src = new File(srcDir);
		File dst = new File(destDir);
		if (!src.exists() || !dst.exists()) {
			if (!src.isFile()) {
				LOG.error("File from: " + src.getPath() + " doesn't exist");
			}
			if (!dst.isFile()) {
				LOG.error("File to: " + dst.getPath() + " doesn't exist");
			}
			return false;
		}
		dst = new File(endPath(destDir) + src.getName());
		try {
			FileUtils.copyDirectory(src, dst);
		} catch (IOException e) {
			LOG.error("Copy folder: " + src.getPath() + " to: " + dst.getPath() + " failed");
			return false;
		}
		return true;
	}

	public static String addHttp(final String hostName) {
		String hostAddress = hostName;
		if (!hostName.startsWith("http://")) {
			hostAddress = "http://" + hostName;
		}
		return hostAddress;
	}

	public static String createFolderTemporary(final String outputPath) {
		String tmpName = String.format("%s.%s", "AcuTmp", RandomStringUtils.randomAlphanumeric(8));
		String tempPath = new String(endPath(outputPath) + tmpName);
		if (exists(LocalEnv.userDir) && toFile(LocalEnv.userDir).canRead()) {
			tempPath = endPath(LocalEnv.userDir) + tmpName;
			boolean mkdirs = mkdirs(tempPath);
			if (!mkdirs) {
				tempPath = new String(endPath(outputPath) + tmpName);
			}
			boolean cleanDirectory = cleanDirectory(tempPath);
			if (!cleanDirectory) {
				tempPath = new String(endPath(outputPath) + tmpName);
			}
		}
		return endPath(tempPath);
	}

}
