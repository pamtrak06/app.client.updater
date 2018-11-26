package org.app.client.updater.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

public class FileSearcher {

	private static Logger LOG = Logger.getLogger(FileSearcher.class);

	private final static List<String> FOLDER_FILTER = PropertiesUtil.getOsPropertyList("FileSearcher",
			"folder-black-list");
	private final static List<String> FILE_FILTER = PropertiesUtil.getOsPropertyList("FileSearcher", "file-black-list");

	private String fileNameToSearch;
	private List<String> result = new ArrayList<String>();

	private String getFileNameToSearch() {
		return fileNameToSearch;
	}

	private void setFileNameToSearch(final String fileNameToSearch) {
		this.fileNameToSearch = fileNameToSearch;
	}

	private List<String> getResult() {
		return result;
	}

	private void searchDirectory(final File directory, final String fileNameToSearch) {

		setFileNameToSearch(fileNameToSearch);

		if (!directory.isFile()) {
			search(directory);
		} else {
			LOG.warn("[" + directory.getAbsoluteFile() + "] is not a directory!");
		}

	}

	private void search(final File file) {

		if (!filter(file)) {
			return;
		}

		if (!file.isFile()) {
			LOG.debug("Searching in directory ... " + file.getAbsoluteFile());

			if (file.canRead()) {
				File[] listFiles = file.listFiles();
				if (listFiles != null) {
					for (File temp : listFiles) {
						if (temp.isDirectory()) {
							search(temp);
						} else {
							if (temp.getName().matches(getFileNameToSearch())) {
								result.add(temp.getAbsoluteFile().toString());
							}

						}
					}
				}

			} else {
				LOG.debug("file system: " + file.getAbsoluteFile() + ": permission Denied");
			}
		}

	}

	private boolean filter(final File file) {
		if (file.isDirectory()) {
			for (String folder : FOLDER_FILTER) {
				if (file.getName().equals(folder)) {
					return false;
				}
			}
		} else {
			for (String filename : FILE_FILTER) {
				if (file.getName().equals(filename)) {
					return false;
				}
			}
		}
		return true;
	}

	private static List<File> retrieveSearchingPaths(final String diskFilter, final String pathFilter) {
		List<File> searchPathList = new ArrayList<File>();
		List<File> pathList = new ArrayList<File>();
		List<String> diskList = new ArrayList<String>();
		String[] disks = null;
		String[] paths = null;

		LOG.debug("Retrieve path from disk filter: [" + diskFilter + "] and from path filter: [" + pathFilter + "]");

		// Get all disks drive letter
		if ((diskFilter == null) && pathFilter == null) {
			searchPathList = FileUtil.getDriveList();
		}

		// Get all specific drive letter
		if (diskFilter != null) {
			disks = diskFilter.split(";");
			for (String d : disks) {
				if (FileUtil.getDrive(d) != null) {
					diskList.add(d);
				}
			}
		}

		// Get all specific path
		if (pathFilter != null) {
			paths = pathFilter.split(";");
			for (String p : paths) {
				File fs = new File(p);
				if (fs.canRead()) {
					if (fs.isFile()) {
						fs = fs.getParentFile();
					}
					LOG.debug("Add directory: [" + fs.getAbsolutePath() + "] to path");
					pathList.add(fs);
				} else {
					LOG.warn("Not added directory: [" + fs.getAbsolutePath() + "] to path, could not read it");
				}
			}
		}

		if (!pathList.isEmpty()) {
			searchPathList.addAll(pathList);
		} else {
			LOG.debug("No path correctly defined, add all drive disk to path");
			searchPathList = FileUtil.getDriveList();
		}

		// Remove disk where path is defined
		if (diskFilter != null && "".equals(diskFilter) && pathFilter != null && "".equals(pathFilter)) {
			for (String p : paths) {
				File pf = new File(p);
				for (String d : disks) {
					if (pf.getAbsolutePath().toLowerCase().startsWith(d.toLowerCase())) {
						diskList.remove(d);
					}
				}
			}
			for (String d : diskList) {
				LOG.debug("Add drive disk: [" + d + "] to path");
				searchPathList.add(FileUtil.getDrive(d));
			}
		}

		return searchPathList;
	}

	public static List<File> searchFile(final String regexFilename, final String diskFilter, final String pathFilter) {
		List<File> fileFounded = new ArrayList<File>();
		FileSearcher fileSearch = new FileSearcher();
		String path = null;
		String disk = null;

		if (regexFilename == null || regexFilename.trim().isEmpty()) {
			throw new IllegalArgumentException("Regex filename must not be empty");
		}

		if (diskFilter != null
				&& ("".equals(diskFilter.trim()) || "*".equals(diskFilter.trim()) || "all".equals(diskFilter
						.toLowerCase()))) {
			disk = null;
		}
		if (pathFilter != null && "".equals(pathFilter.trim().replace("\t", ""))) {
			path = null;
		}

		path = (pathFilter != null) ? new String(pathFilter) : null;
		disk = (diskFilter != null) ? new String(diskFilter) : null;

		List<File> searchingPaths = retrieveSearchingPaths(disk, path);

		for (File searchPath : searchingPaths) {

			fileSearch.searchDirectory(searchPath, regexFilename);

			int count = fileSearch.getResult().size();
			if (count == 0) {
				LOG.debug("No result found for regex: " + regexFilename + ", disk filter: [" + diskFilter
						+ "], path filter: [" + pathFilter + "] !");
			} else {
				LOG.debug("Found " + count + " result corresponding !");
				for (String matched : fileSearch.getResult()) {
					LOG.debug("Found : " + matched);
					fileFounded.add(new File(matched));
				}
			}
		}

		return fileFounded;
	}

}
