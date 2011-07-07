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
package org.romaframework.core.flow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.romaframework.core.schema.SchemaClass;

/**
 * Data class containing the user context to be saved in session. It implements Serializable interface to allow clustering.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class ControllerContext implements Serializable {

	private static final long						serialVersionUID	= 1L;

	protected String										activeArea;
	protected Map<SchemaClass, Object>	objects						= new HashMap<SchemaClass, Object>();

	public String getActiveArea() {
		return activeArea;
	}

	public void setActiveArea(String lastArea) {
		this.activeArea = lastArea;
	}

	public Map<SchemaClass, Object> getObjects() {
		return objects;
	}

	public void removeObject(SchemaClass iKey) {
		objects.remove(iKey);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("LastArea: ");
		buffer.append(activeArea);
		buffer.append(" Objects: ");
		buffer.append(objects.size());
		return buffer.toString();
	}
}
