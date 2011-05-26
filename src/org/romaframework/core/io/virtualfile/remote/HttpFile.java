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
package org.romaframework.core.io.virtualfile.remote;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.romaframework.core.io.virtualfile.VirtualFile;

public class HttpFile implements VirtualFile {
	public static final String	PREFIX	= "http:";

	protected URL								url;

	public HttpFile(URL iURL) {
		url = iURL;
	}

	public boolean exists() {
		try {
			InputStream in = url.openStream();
			in.close();
			return true;
		} catch (IOException e) {
		}
		return false;
	}

	public VirtualFile[] listFiles() {
		throw new NoSuchMethodError("Method not implemented");
	}

	public boolean isDirectory() {
		return false;
	}

	public String getName() {
		return url.toString();
	}

	public InputStream getInputStream() {
		try {
			return url.openStream();
		} catch (IOException e) {
		}
		return null;
	}

	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException("Method not implemented");
	}

	public long length() {
		throw new NoSuchMethodError("Method not implemented");
	}

	@Override
	public String toString() {
		return url.toString();
	}

	public String getAbsolutePath() {
		return toString();
	}
}
