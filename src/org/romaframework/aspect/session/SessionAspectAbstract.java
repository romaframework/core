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

package org.romaframework.aspect.session;

import java.util.Map;

import org.romaframework.core.Roma;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaObject;

public abstract class SessionAspectAbstract extends SelfRegistrantConfigurableModule<String> implements SessionAspect {

	private static final String	SESSION_SCHEMA_OBJECT	= "$$_SESSION_SCHEMA_OBJECT_$$";

	@Override
	public SchemaObject getSchemaObject(Object object) {
		Map<Object, SchemaObject> so = getProperty(SESSION_SCHEMA_OBJECT);
		if (so == null) {
			so = new IdentityWeakHashMap<Object, SchemaObject>();
			setProperty(SESSION_SCHEMA_OBJECT, so);
		}
		SchemaObject schemaObject = so.get(object);
		if (schemaObject == null) {
			if (object instanceof SchemaClassDefinition) {
				object = ((SchemaClassDefinition) object).getSchemaClass();
				schemaObject = new SchemaObject(((SchemaClassDefinition) object).getSchemaClass(), null);
			} else
				schemaObject = new SchemaObject(Roma.schema().getSchemaClass(object), object);
			so.put(object, schemaObject);
		}
		return schemaObject;
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void configAction(SchemaAction action) {
	}

	public void configClass(SchemaClassDefinition class1) {
	}

	public void configEvent(SchemaEvent event) {
	}

	public void configField(SchemaField field) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	@Deprecated
	public void shutdown(Object iSystemSession) {
		destroyCurrentSession(iSystemSession);
	}
}
