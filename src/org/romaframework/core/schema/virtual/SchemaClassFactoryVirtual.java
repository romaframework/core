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

package org.romaframework.core.schema.virtual;

import org.romaframework.core.Roma;
import org.romaframework.core.config.Configurable;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassFactory;
import org.romaframework.core.schema.SchemaConfigurationLoader;
import org.romaframework.core.schema.config.SchemaConfiguration;

/**
 * Manage the Entities and cache them.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaClassFactoryVirtual extends Configurable<String> implements SchemaClassFactory {
	public SchemaClass createSchemaClass(String iEntityName, SchemaClass iBaseClass, SchemaConfiguration iDescriptor) {
		if (iDescriptor == null) {
			iDescriptor = Roma.component(SchemaConfigurationLoader.class).getSaxSchemaConfiguration(iEntityName);
			if (iDescriptor == null)
				return null;
		}

		// CREATE ENTITY
		SchemaClassVirtual cls = new SchemaClassVirtual(iEntityName, iBaseClass, iDescriptor);
		return cls;
	}
}
