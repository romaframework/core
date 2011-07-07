/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.io.virtualfile;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.romaframework.core.Utility;
import org.romaframework.core.io.virtualfile.classpath.ClassPathFile;
import org.romaframework.core.io.virtualfile.classpath.ClassPathURLConnection;
import org.romaframework.core.io.virtualfile.physical.PhysicalFile;
import org.romaframework.core.io.virtualfile.remote.HttpFile;
import org.romaframework.core.io.virtualfile.zip.ZipArchiveFile;
import org.romaframework.core.io.virtualfile.zip.ZipEntryArchiveFile;

public class VirtualFileFactory {
	private Map<String, Class<? extends VirtualFile>>	urlTypes	= new HashMap<String, Class<? extends VirtualFile>>();

	/**
	 * Singleton instance
	 */
	public static VirtualFileFactory									instance	= new VirtualFileFactory();

	public VirtualFileFactory() {
		urlTypes.put(PhysicalFile.PREFIX, PhysicalFile.class);
		urlTypes.put(ZipArchiveFile.PREFIX, ZipArchiveFile.class);
		urlTypes.put(HttpFile.PREFIX, HttpFile.class);

		// REGISTER THE URL STREAM HANDLER TO BE RECOGNIZED BY JAVA IO CLASSES
		// URL.setURLStreamHandlerFactory(new ClassPathURLStreamHandlerFactory()); Line commented because this static method can be
		// called only once, and on tomcat it seems to has already been called.
		urlTypes.put("classpath:", ClassPathFile.class);
	}

	public VirtualFile getFile(String url) throws MalformedURLException {
		if (url == null)
			return null;

		url = resolveURL(url);

		return getFile(new URL(url));
	}

	private String resolveURL(String url) {
		for (Map.Entry<String, Class<? extends VirtualFile>> entry : urlTypes.entrySet()) {
			if (url.startsWith(entry.getKey()))
				return url;
		}

		return Utility.FILE_PREFIX + url;
	}

	public VirtualFile getFile(URL url) {
		URLConnection conn;
		try {
			if (url == null) {
				return null;
			}

			conn = url.openConnection();

			if (conn == null)
				return null;

			if (conn instanceof JarURLConnection) {
				JarURLConnection jarConn = (JarURLConnection) conn;

				if (jarConn.getJarEntry() != null)
					return new ZipEntryArchiveFile(jarConn.getJarEntry(), jarConn.getJarFile());

				return new ZipArchiveFile(jarConn);
			} else if (conn instanceof ClassPathURLConnection) {
				return new ClassPathFile(url);
			} else if (conn instanceof HttpURLConnection) {
				return new HttpFile(url);
			} else {
				return new PhysicalFile(url);
			}
		} catch (IOException e) {
			return null;
		}
	}

	public static VirtualFileFactory getInstance() {
		return instance;
	}
}
