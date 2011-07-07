/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.resource;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Resource information needed by AutoReload classes.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class ResourceInfo {

	public File											file;
	public long											lastModified;
	public Set<AutoReloadListener>	listeners;

	public ResourceInfo(File iFile) {
		file = iFile;
		lastModified = iFile.lastModified();
		listeners = new HashSet<AutoReloadListener>();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("File: ");
		buffer.append(file);
		buffer.append("(lastModified on ");
		buffer.append(lastModified);
		buffer.append(")");
		return buffer.toString();
	}
}
