/*
 *
 * Copyright 2007 Luca Molino (luca.molino--AT--@assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.romaframework.core.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.domain.type.Stream;

/**
 * @author l.molino
 * @author Luca Garulli
 * 
 *         Class that help on managing File attachments
 * 
 */
public class FileUtils {

	private static Log					log								= LogFactory.getLog(FileUtils.class);
	public static final int			BUFFER						= 4096;
	public static final String	SYSTEM_SESSION_ID	= "System_session";

	/**
	 * Method that returns a temp File from a Stream
	 * 
	 * @param stream
	 * @return The File form the Stream
	 * 
	 * @see Stream
	 * @see File
	 */
	public static File getTempFile(Stream stream) {
		if (stream != null) {
			String completeName = stream.getFileName();
			if (completeName.indexOf("\\") > -1) {
				completeName = completeName.split("\\\\")[(completeName.split("\\\\").length - 1)];
			}
			InputStream in = stream.getInputStream();
			return getTempFile(completeName, in);
		}
		return null;
	}

	/**
	 * Method returns a temp File with a given name from an InputStream
	 * 
	 * @param stream
	 * @return The File form the InputStream with given name
	 * 
	 * @see Stream
	 * @see File
	 */
	public static File getTempFile(String completeName, InputStream in) {
		File tempImage;
		String sessionId = getSessionId();

		try {
			tempImage = new File(sessionId + "_" + getFileName(completeName) + "." + getFileType(completeName));

			tempImage.delete();
			IOUtils.copy(in, new FileOutputStream(tempImage));
		} catch (IOException ioe) {
			log.error(ioe);
			tempImage = null;
		} catch (NullPointerException npe) {
			tempImage = null;
		}
		tempImage.deleteOnExit();
		return tempImage;
	}

	/**
	 * 
	 * Method that write data from a given <code>InputStream</code> to a given <code>OutputStream</code>
	 * 
	 * @param iInput
	 *          the origin InputStream
	 * @param iOutput
	 *          the destination OutputStream
	 * @return Number of bytes written
	 * @throws IOException
	 */
	public static long writeFile(InputStream iInput, OutputStream iOutput) throws IOException {
		return IOUtils.copy(iInput, iOutput);
	}

	public static void writeFile(String iContent, File iFile) throws FileNotFoundException, IOException {
		FileOutputStream out = new FileOutputStream(iFile);
		IOUtils.write(iContent, out);
		out.close();
	}

	public static String getFileName(String iCompleteName) {
		int pos = iCompleteName.lastIndexOf('.');
		if (pos > 0)
			iCompleteName = iCompleteName.substring(0, pos);
		return iCompleteName;
	}

	public static String getFileType(String iCompleteName) {
		int pos = iCompleteName.lastIndexOf('.');
		if (pos > 0)
			return iCompleteName.substring(pos + 1);
		else
			return "";
	}

	public static String getFileNameWithoutSessionId(String iName) {
		String sessionId = getSessionId();
		return getFileNameWithoutSessionId(iName, sessionId);
	}

	public static String getFileNameWithoutSessionId(String iName, String sessionId) {
		if (iName.startsWith(sessionId)) {
			int pos = iName.indexOf('_');
			if (pos > 0)
				return iName.substring(pos + 1);
			else
				return iName;
		} else
			return iName;

	}

	public static String getFileSize(long iSize) {
		StringBuilder size = new StringBuilder();
		boolean stop = (iSize < 1024);
		int i = 0;
		while (!stop) {
			iSize = iSize / 1024;
			stop = (iSize < 1024);
			i++;
		}
		size.append(iSize);
		switch (i) {
		case 0:
			size.append(" bytes");
			break;
		case 1:
			size.append(" Kb");
			break;

		case 2:
			size.append(" Mb");
			break;
		case 3:
			size.append(" Gb");
			break;
		case 4:
			size.append(" Tb");
			break;
		default:
			break;
		}
		return size.toString();
	}

	/**
	 * Method that create a compressed archive from a List of files.
	 * 
	 * @param attachments
	 *          - The list of file to compress
	 * @return the compressed File generated.
	 * @throws IOException
	 * @see File
	 */
	public static File compressAttachments(List<File> attachments) throws IOException {
		String sessionId = getSessionId();
		return compressAttachments(sessionId, attachments);
	}

	/**
	 * Method that create a compressed archive from a List of files.
	 * 
	 * @param attachments
	 *          - The list of file to compress
	 * @param iFileName
	 *          - The name of the compressed file
	 * @return the compressed File generated.
	 * @throws IOException
	 * @see File
	 */
	public static File compressAttachments(String iFileName, List<File> attachments) throws IOException {
		File fileToSave = new File(iFileName + ".zip");
		// definiamo l'output previsto che sarà un file in formato zip
		ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(fileToSave), BUFFER));
		try {
			for (File attachment : attachments) {

				// definiamo il buffer per lo stream di bytes
				byte[] data = new byte[1000];

				// indichiamo il nome del file che subirà la compressione
				BufferedInputStream in = new BufferedInputStream(new FileInputStream(attachment), BUFFER);
				int count;

				// processo di compressione
				out.putNextEntry(new ZipEntry(getFileNameWithoutSessionId(attachment.getName())));
				while ((count = in.read(data, 0, 1000)) != -1) {
					out.write(data, 0, count);
				}
				out.closeEntry();
				in.close();
			}
		} finally {
			out.flush();
			out.close();
		}
		fileToSave.deleteOnExit();
		return fileToSave;
	}

	/**
	 * Method that extracts a file, defined by his name, from a compressed file.
	 * 
	 * @param iCompressedFile
	 *          - The original compressed file
	 * @param iFileName
	 *          - The name of the file contained in the compressed file to extract
	 * @return the file extracted by the compressed file, or null if it doesn't exists
	 * @throws IOException
	 */
	public static File getDecompressedAttachment(File iCompressedFile, String iFileName) throws IOException {
		File attachment = null;
		String sessionId = getSessionId();
		ZipInputStream zis = new ZipInputStream(new FileInputStream(iCompressedFile));
		ZipEntry entry;
		while ((entry = zis.getNextEntry()) != null) {
			if (entry.getName().equals(iFileName)) {
				log.info("Decompressing file: " + entry.getName());
				attachment = new File(sessionId + "_" + entry.getName());
				// write the files to the disk
				IOUtils.copy(zis, new FileOutputStream(attachment));
			}
		}
		if (attachment != null)
			attachment.deleteOnExit();
		return attachment;
	}

	/**
	 * Method that extract all files from a compressed file.
	 * 
	 * @param compressedFile
	 *          - The original compressed file
	 * @return the list of decompressed file.
	 * @throws IOException
	 */
	public static List<File> decompressAttachments(File compressedFile) throws IOException {
		if (compressedFile == null)
			return null;
		String sessionId = getSessionId();
		return decompressAttachments(compressedFile, sessionId);
	}

	public static List<File> decompressAttachments(File compressedFile, String iFileName) throws IOException {
		List<File> attachments = new ArrayList<File>();
		ZipInputStream zis = new ZipInputStream(new FileInputStream(compressedFile));
		ZipEntry entry;
		try {
			while ((entry = zis.getNextEntry()) != null) {
				log.info("Decompressing file: " + entry.getName());
				File file = new File(iFileName + "_" + entry.getName());
				// write the files to the disk
				IOUtils.copy(zis, new FileOutputStream(file));
				file.deleteOnExit();
				attachments.add(file);
			}
		} finally {
			if (compressedFile != null)
				compressedFile.delete();
		}
		return attachments;
	}

	public static List<File> getFileAttachmentsWithoutSessionID(List<File> attachments) {
		List<File> newAttachments = new ArrayList<File>();
		String sessionId = getSessionId();
		for (File file : attachments) {
			if (file.getName().startsWith(sessionId)) {
				File directory = new File(sessionId);
				directory.mkdir();
				directory.deleteOnExit();
				File newFile = new File(sessionId + File.separatorChar + FileUtils.getFileNameWithoutSessionId(file.getName()));
				newFile = copyFile(file, newFile);
				newFile.deleteOnExit();
				newAttachments.add(newFile);
			} else
				newAttachments.add(file);
		}
		if (newAttachments.size() == attachments.size())
			return newAttachments;
		else {
			log.error("Error converting attachments names, see previous error logs for further details.");
			return null;
		}
	}

	private static String getSessionId() {
		String sessionId;
		if (Roma.session().getActiveSessionInfo() != null)
			sessionId = Roma.session().getActiveSessionInfo().getId().toString();
		else
			sessionId = SYSTEM_SESSION_ID;
		return sessionId;
	}

	public static File copyFile(File iOriginalFile, File iNewFile) {
		try {
			org.apache.commons.io.FileUtils.copyFile(iOriginalFile, iNewFile);
			return iNewFile;
		} catch (IOException ioe) {
			log.error("Unable to copy file \"" + iOriginalFile.getName() + "\" to \"" + iNewFile.getName() + " cause: " + ioe, ioe);
		}
		return null;
	}

	public static byte[] readFile(File iOriginalFile) {
		try {
			return org.apache.commons.io.FileUtils.readFileToByteArray(iOriginalFile);
		} catch (FileNotFoundException fnfe) {
			log.error("Unable to read file \"" + iOriginalFile.getName() + "\" cause: " + fnfe, fnfe);
		} catch (IOException ioe) {
			log.error("Unable to read file \"" + iOriginalFile.getName() + "\" cause: " + ioe, ioe);
		}
		return null;
	}

	public static StringBuilder readFileAsText(File iOriginalFile) throws FileNotFoundException {
		try {
			return new StringBuilder(org.apache.commons.io.FileUtils.readFileToString(iOriginalFile));
		} catch (FileNotFoundException fnfe) {
			log.error("Unable to read file \"" + iOriginalFile.getName() + "\" cause: " + fnfe, fnfe);
		} catch (IOException ioe) {
			log.error("Unable to read file \"" + iOriginalFile.getName() + "\" cause: " + ioe, ioe);
		}
		return null;
	}

	public static StringBuilder readStreamAsText(InputStream iStream) {
		if (iStream == null)
			return null;

		try {
			return new StringBuilder(IOUtils.toString(iStream));
		} catch (FileNotFoundException fnfe) {
			log.error("Unable to read stream cause: " + fnfe, fnfe);
		} catch (IOException ioe) {
			log.error("Unable to read file cause: " + ioe, ioe);
		} finally {
			try {
				iStream.close();
			} catch (IOException e) {
			}
		}
		return null;
	}

	/**
	 * Search (also recursively) all the files that satisfy the pattern requested.
	 * 
	 * @param iPath
	 *          Starting path
	 * @param iFileNamePattern
	 *          REGEXP pattern
	 * @param deep
	 *          true to go in deep, otherwise for a shallow search only in the current folder
	 * @return
	 */
	public static List<File> searchAllFiles(String iPath, String iFileNamePattern, boolean deep) {
		List<File> files = new ArrayList<File>();

		File path = new File(iPath);
		if (path.exists())
			searchAllFiles(files, path, iFileNamePattern, deep);

		return files;
	}

	protected static void searchAllFiles(List<File> iFoundFiles, File iPath, String iFileNamePattern, boolean deep) {
		File[] folderFiles = iPath.listFiles();
		if (folderFiles == null || folderFiles.length == 0)
			return;

		for (File f : folderFiles) {
			if (f.isDirectory())
				searchAllFiles(iFoundFiles, f, iFileNamePattern, deep);
			else if (f.getName().matches(iFileNamePattern))
				iFoundFiles.add(f);
		}
	}

	public static void zipDirectory(ZipOutputStream out, File iDirectory, boolean iRecursive) throws IOException {
		if (log.isDebugEnabled())
			log.debug("[FileUtils.zipDirectory] Compressing directory" + iDirectory.getAbsolutePath() + "...");

		zipDirectory(out, iDirectory, iDirectory.getName(), iRecursive);
	}

	public static void zipDirectory(ZipOutputStream out, File iDirectory, String iRelativePath, boolean iRecursive) throws IOException {

		if (iRelativePath == null)
			iRelativePath = "";
		else if (!iRelativePath.endsWith("/"))
			iRelativePath += "/";

		if (log.isDebugEnabled())
			log.debug("[FileUtils.zipDirectory] Compressing directory" + iRelativePath + iDirectory.getName() + "...");

		File[] files = iDirectory.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory() && iRecursive)
				zipDirectory(out, files[i], iRelativePath, iRecursive);
			else
				zipFile(out, files[i], iRelativePath);
		}
	}

	public static void zipFile(ZipOutputStream out, File iFile, String iRelativePath) throws FileNotFoundException, IOException {
		if (log.isDebugEnabled())
			log.debug("[FileUtils.zipFile] Compressing file" + iRelativePath + iFile.getName() + "...");

		FileInputStream in = new FileInputStream(iFile);
		zipStream(out, in, iRelativePath + iFile.getName());
	}

	public static void zipFile(ZipOutputStream out, File iFile) throws FileNotFoundException, IOException {
		if (log.isDebugEnabled())
			log.debug("[FileUtils.zipFile] Compressing file" + iFile.getAbsolutePath() + "...");

		FileInputStream in = new FileInputStream(iFile.getAbsolutePath());
		zipStream(out, in, iFile.getPath());
	}

	public static void zipFile(ZipOutputStream out, InputStream iStream, String iFileName) throws FileNotFoundException, IOException {
		if (log.isDebugEnabled())
			log.debug("[FileUtils.zipFile] Compressing stream as file: " + iFileName + "...");

		zipStream(out, iStream, iFileName);
	}

	private static void zipStream(ZipOutputStream out, InputStream iStream, String iFileName) throws IOException {
		out.putNextEntry(new ZipEntry(iFileName));

		IOUtils.copy(iStream, out);

		out.closeEntry();
		iStream.close();
	}

	public static int unzipArchive(File archive, File outputDir) {
		int i = 0;

		try {
			ZipFile zipfile = new ZipFile(archive);
			for (Enumeration<? extends ZipEntry> e = zipfile.entries(); e.hasMoreElements();) {
				ZipEntry entry = (ZipEntry) e.nextElement();
				unzipEntry(zipfile, entry, outputDir);
				i++;
			}
		} catch (Exception e) {
			log.error("Error while extracting file " + archive, e);
		}

		return i;
	}

	private static void unzipEntry(ZipFile zipfile, ZipEntry entry, File outputDir) throws IOException {

		if (entry.isDirectory()) {
			new File(outputDir, entry.getName()).mkdirs();
			return;
		}

		File outputFile = new File(outputDir, entry.getName());
		if (!outputFile.getParentFile().exists()) {
			outputFile.getParentFile().mkdirs();
		}

		log.debug("Extracting: " + entry);
		BufferedInputStream inputStream = new BufferedInputStream(zipfile.getInputStream(entry));
		BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outputFile));

		try {
			IOUtils.copy(inputStream, outputStream);
		} finally {
			outputStream.close();
			inputStream.close();
		}
	}
}
