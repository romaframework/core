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

package org.romaframework.core.schema.virtual;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.scripting.exception.ScriptingException;
import org.romaframework.aspect.scripting.feature.ScriptingFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaField;

/**
 * Virtual Object default implementation.
 * 
 * @see VirtualObject
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class VObject implements VirtualObject {
	protected SchemaClass					clazz;
	protected Object							superClassObject;
	protected Map<String, Object>	values	= new LinkedHashMap<String, Object>();

	private static Log						log			= LogFactory.getLog(VObject.class);

	public VObject() {
	}

	public VObject(SchemaClass iClass) {
		this(iClass, null);
	}

	public VObject(String iClassName) {
		this(Roma.schema().getSchemaClass(iClassName));
	}

	public VObject(String iClassName, Object iObject) {
		this(Roma.schema().getSchemaClass(iClassName), iObject);
	}

	public VObject(SchemaClass iClass, Object iObject) {
		clazz = iClass;

		if (iClass.getSuperClass() != null) {
			try {
				if (iClass.getSuperClass().isAbstract() || iClass.getSuperClass().isInterface())
					throw new IllegalArgumentException("Can't create a virtual object with super class '" + iClass.getSuperClass()
							+ "' since it's abstract or it's an interface");

				superClassObject = iObject != null ? iObject : iClass.getSuperClass().newInstance();

			} catch (Exception e) {
				log.error("[VObject] Cannot instantiate super class: " + iClass.getSuperClass(), e);
			}
		}

		loadFields(iClass);

		try {
			invokeConstructor();
		} catch (ScriptingException e) {
			log.error("[VObject] Error on invoking constructor", e);
		}
	}

	public SchemaClass getClazz() {
		return clazz;
	}

	public Map<String, Object> getValues() {
		return values;
	}

	public Object getValue(String iName) {
		SchemaField field = clazz.getField(iName);
		if (field == null)
			throw new IllegalArgumentException("Field '" + iName + "' is not part of virtual class: " + clazz.getName());

		if (superClassObject != null && clazz.getSuperClass().getField(iName) != null)
			return field.getValue(this);

		return values.get(iName);
	}

	public VObject setValue(String iName, Object iValue) {
		SchemaField field = clazz.getField(iName);

		if (field == null)
			throw new IllegalArgumentException("Field '" + iName + "' is not part of virtual class: " + clazz.getName());

		field.setValue(this, iValue);
		return this;
	}

	public Object getSuperClassObject() {
		return superClassObject;
	}

	protected void loadFields(SchemaClass iClass) {
		if (iClass.getFields().size() > 0) {
			SchemaField field;
			String fieldName;
			Object fieldValue;
			for (Iterator<SchemaField> it = iClass.getFieldIterator(); it.hasNext();) {
				field = it.next();

				fieldName = field.getName();
				fieldValue = null;
				if (superClassObject != null && clazz.getSuperClass().getField(fieldName) != null)
					try {
						// GET THE VALUE FROM THE SUPER-CLASS OBJECT
						fieldValue = clazz.getSuperClass().getFieldValue(superClassObject, fieldName);
					} catch (Exception e) {
						// UNABLE TO GET THE FIELD VALUE, MAYBE THE VALUE IS LOADED LAZY?
					}

				values.put(fieldName, fieldValue);
			}
		}
	}

	protected void invokeConstructor() throws ScriptingException {
		String code = clazz.getFeature(ScriptingFeatures.CODE);
		if (code != null)
			VirtualObjectHelper.invoke(this, clazz);
	}
}
