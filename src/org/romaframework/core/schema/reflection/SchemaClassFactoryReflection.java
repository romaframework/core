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

package org.romaframework.core.schema.reflection;

import java.lang.reflect.Array;

import org.romaframework.core.Roma;
import org.romaframework.core.config.Configurable;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassFactory;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.config.SchemaConfiguration;

/**
 * Manage the Entities and cache them.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaClassFactoryReflection extends Configurable<String> implements SchemaClassFactory {
	public SchemaClass createSchemaClass(String iEntityName, SchemaClass iBaseClass, SchemaConfiguration iDescriptor) {

		Class<?> clazz = null;
		if (iEntityName.endsWith("[]")) {
			clazz = getArrayType(iEntityName);
		} else {
			clazz = Roma.component(SchemaClassResolver.class).getLanguageClass(iEntityName);
		}
		if (clazz == null && iBaseClass == null) {
			clazz = SchemaHelper.getClassForJavaTypes(iEntityName);
			if (clazz == null)
				return null;
		}

		// CREATE ENTITY
		SchemaClassReflection cls = new SchemaClassReflection(iEntityName, clazz, iBaseClass, iDescriptor);

		return cls;
	}

	private Class<?> getArrayType(String arrayName) {
		if (arrayName.endsWith("[]")) {
			Class<?> arrayType = getArrayType(arrayName.substring(0, arrayName.length() - 2));
			return Array.newInstance(arrayType, 0).getClass();
		} else {
			Class<?> clazz = Roma.component(SchemaClassResolver.class).getLanguageClass(arrayName);
			if (clazz == null) {
				clazz = SchemaHelper.getClassForJavaTypes(arrayName);
			}
			if (clazz == null) {
				clazz = SchemaHelper.getClassForJavaTypes(Object.class.getSimpleName());
			}
			return clazz;
		}

	}

}
