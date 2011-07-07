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

package org.romaframework.core.schema;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.schema.config.FileSystemSchemaConfiguration;
import org.romaframework.core.schema.config.SaxSchemaConfiguration;

/**
 * Handle Entity schema descriptor loading.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaConfigurationLoader {

	/**
	 * Load XML descriptor using Apache XMLBeans. Entities are cached.
	 * 
	 * @param iEntityName
	 * @return Entity instance read from XML descriptor
	 */
	public FileSystemSchemaConfiguration getXmlFileSystemSchemaConfiguration(String iEntityName) {
		FileSystemSchemaConfiguration descr = null;
		String filePath = "<unsetted>";
		try {
			filePath = Roma.component(SchemaClassResolver.class).getClassDescriptorPath(iEntityName);
			if (filePath == null)
				return null;

			String containerFilePath = Utility.getContainerFile(filePath);

			// TRY TO LOAD RESOURCE BY CLASS LOADER
			if (log.isDebugEnabled())
				log.debug("[SchemaConfigurationLoader.getSchema] Loading schema from file: " + getClass().getResource(filePath).getFile());
			descr = new FileSystemSchemaConfiguration(new File(getClass().getResource(containerFilePath).getFile()), filePath);
		} catch (Exception e) {
			log.error("[SchemaConfigurationLoader.getSchema] Error on loading resource: " + filePath, e);
		}
		return descr;
	}

	public SaxSchemaConfiguration getSaxSchemaConfiguration(String iEntityName) {
		SaxSchemaConfiguration descr = null;
		String filePath = "<unsetted>";
		try {
			filePath = Roma.component(SchemaClassResolver.class).getClassDescriptorPath(iEntityName);
			if (filePath == null)
				return null;

			String containerFilePath = Utility.getContainerFile(filePath);

			// TRY TO LOAD RESOURCE BY CLASS LOADER
			if (log.isDebugEnabled())
				log.debug("[SchemaConfigurationLoader.getSchema] Loading schema from file: " + getClass().getResource(filePath).getFile());
			descr = new SaxSchemaConfiguration(new File(getClass().getResource(containerFilePath).getFile()), filePath);
		} catch (Exception e) {
			log.error("[SchemaConfigurationLoader.getSchema] Error on loading resource: " + filePath, e);
		}
		return descr;
	}

	private static Log	log	= LogFactory.getLog(SchemaConfigurationLoader.class);
}
