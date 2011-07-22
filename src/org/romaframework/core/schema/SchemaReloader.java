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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.resource.AutoReloadListener;
import org.romaframework.core.resource.AutoReloadManager;

/**
 * Resolve entity class and descriptors using the paths configured.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaReloader implements AutoReloadListener {

	public final static String						reloading_mutex				= "reloading_mutex";
	private final Map<File, List<String>>	fileReloadingMapping	= new HashMap<File, List<String>>();
	private static Log										log										= LogFactory.getLog(SchemaReloader.class);

	/**
	 * Map the file to the entity name to be reloaded when updated.
	 * 
	 * @param iFile
	 * @param iEntityName
	 */
	public void addResourceForReloading(File iFile, String iEntityName) {
		synchronized (fileReloadingMapping) {
			List<String> resources = fileReloadingMapping.get(iFile);
			if (resources == null) {
				resources = new ArrayList<String>();
				fileReloadingMapping.put(iFile, resources);

				// REGISTER DESCRIPTOR FILE TO BE WAKED UP ON RELOADING
				Roma.component(AutoReloadManager.class).addResource(iFile, this);
			}
			resources.add(iEntityName);
		}
	}

	public void signalUpdatedFile(File iFile) {
		synchronized (reloading_mutex) {
			List<String> resources = fileReloadingMapping.get(iFile);
			if (resources == null)
				return;

			log.debug("[SchemaReloader.signalUpdatedFile] Caught file changing: " + iFile + ". Propagating the event to all listeners");

			SchemaClass classInfo = null;
			for (String entity : resources) {
				classInfo = Roma.schema().getSchemaClass(entity);
				classInfo.signalUpdatedFile(iFile);

				// WAKE UP ALL LISTENERS
				List<SchemaReloadListener> listeners = Controller.getInstance().getListeners(SchemaReloadListener.class);
				if (listeners != null)
					for (SchemaReloadListener listener : listeners)
						listener.signalUpdatedClass(classInfo, iFile);
			}

		}
	}
}
