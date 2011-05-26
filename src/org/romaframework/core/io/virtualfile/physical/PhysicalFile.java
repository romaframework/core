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
package org.romaframework.core.io.virtualfile.physical;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.romaframework.core.io.virtualfile.VirtualFile;

public class PhysicalFile implements VirtualFile {
	public static final String	PREFIX	= "file:";

	protected File							file;

	public PhysicalFile(File iFile) {
		file = iFile;
	}

	public PhysicalFile(URL url) {
		file = new File(url.getFile());
	}

	public boolean exists() {
		return file.exists();
	}

	public VirtualFile[] listFiles() {
		File[] pFiles = file.listFiles();
		VirtualFile[] files = new VirtualFile[pFiles.length];
		for (int i = 0; i < pFiles.length; ++i)
			files[i] = new PhysicalFile(pFiles[i]);
		return files;
	}

	public boolean isDirectory() {
		return file.isDirectory();
	}

	public String getName() {
		return file.getName();
	}

	public InputStream getInputStream() {
		try {
			if (file.exists())
				return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		return null;
	}

	public OutputStream getOutputStream() {
		try {
			if (file.exists())
				return new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		return null;
	}

	public long length() {
		return file.length();
	}

	@Override
	public String toString() {
		return file.getName();
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}
}
