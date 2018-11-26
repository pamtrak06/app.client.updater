package org.app.client.updater.utils;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;

public final class ZipUtil {

	public static ZipFile unZip(String zipFilename, String outputFolder) throws ZipException {
		ZipFile zipFile = new ZipFile(zipFilename);
		if (!zipFile.isValidZipFile()) {
			throw new ZipException("Zip file: " + zipFilename + " is not valid");
		}
		if (zipFile.isEncrypted()) {
			// TODO monitoring password for zip
			// zipFile.setPassword(password);
			throw new ZipException("Password not handled");
		}
		zipFile.extractAll(outputFolder);
		return zipFile;
	}

	public static File zip(String inputPath, String outputPath) throws ZipException {
		// TODO add date to filename
		String zipFilename = FileUtil.getFileName(inputPath) + "_" + DateUtil.formatNewDate() + ".zip";
		ZipFile zipFile = new ZipFile(FileUtil.endPath(outputPath) + zipFilename);
		ZipParameters parameters = new ZipParameters();
		parameters.setCompressionMethod(Zip4jConstants.DEFLATE_LEVEL_MAXIMUM);
		zipFile.createZipFile(new File(inputPath), parameters);
		return zipFile.getFile();
	}

}
