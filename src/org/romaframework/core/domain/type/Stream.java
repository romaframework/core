/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.domain.type;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;

import org.romaframework.core.config.Destroyable;

/**
 * Generic class to handle input stream. Used on upload of file via web.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class Stream implements Serializable, Destroyable {
	private static final long			serialVersionUID	= -7308809801631822983L;

	private byte[]								buffer;
	private transient InputStream	inputStream;
	private int										size;
	private String								fileName;
	private String								contentType;

	public Stream(InputStream inputStream, int size, String fileName, String contentType) {
		this.inputStream = inputStream;
		this.size = size;
		this.fileName = fileName;
		this.contentType = contentType;
	}

	/**
	 * Destroy children recursively
	 */
	public void destroy() {
		if (inputStream != null) {
			try {
				inputStream.close();
			} catch (IOException e) {
			}
			inputStream = null;
		}

		buffer = null;
	}

	/**
	 * Load the stream content and store it in the internal buffer. After completition the stream is closed.
	 * 
	 * @throws IOException
	 */
	public void load() throws IOException {
		if (buffer != null || inputStream == null)
			// ALREADY LOADED OR NOT INITIALIZED
			return;

		synchronized (inputStream) {
			buffer = new byte[size];
			inputStream.read(buffer, 0, size);
			inputStream.close();
		}
	}

	public byte[] getBuffer() {
		return buffer;
	}

	public String getContentType() {
		return contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public int getSize() {
		return size;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
