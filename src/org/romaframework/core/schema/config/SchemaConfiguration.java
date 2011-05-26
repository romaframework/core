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

package org.romaframework.core.schema.config;

import java.io.File;
import java.io.IOException;

import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;

/**
 * Base class representing a schema class configuration.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class SchemaConfiguration {

	protected XmlClassAnnotation	type;

	public SchemaConfiguration() {
	}

	public SchemaConfiguration(XmlClassAnnotation iType) {
		setType(iType);
	}

	public void load() throws IOException {
	}

	public File getFile() {
		return null;
	}

	public String getFilePath() {
		return null;
	}

	public void setType(XmlClassAnnotation type) {
		this.type = type;
	}

	public XmlClassAnnotation getType() {
		return type;
	}
}
