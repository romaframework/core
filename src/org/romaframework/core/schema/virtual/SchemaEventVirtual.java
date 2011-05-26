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

import java.lang.reflect.InvocationTargetException;

import org.romaframework.aspect.scripting.ScriptingAspect;
import org.romaframework.aspect.scripting.exception.ScriptingException;
import org.romaframework.aspect.scripting.feature.ScriptingFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;

/**
 * Represent an event of a class.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaEventVirtual extends SchemaEvent {
	private static final long	serialVersionUID	= 9194031418318628702L;
	
	protected SchemaEvent	inheritedEvent;

	public SchemaEventVirtual(SchemaField field, String iName) {
		super(field, iName,null);
	}

	public SchemaEventVirtual(SchemaClassDefinition iEntity, String iName) {
		super(iEntity, iName,null);
	}

	public SchemaEventVirtual(SchemaClassVirtual schemaClassVirtual, String iName, SchemaEvent iInheritedEvent) {
		this(schemaClassVirtual, iName);
		inheritedEvent = iInheritedEvent;
	}

	@Override
	public Object invokeFinal(Object iContent,Object [] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		try {
			if (getFeatures(ScriptingAspect.ASPECT_NAME).getAttribute(ScriptingFeatures.CODE) != null)
				// EXECUTE VIRTUAL CODE
				return VirtualObjectHelper.invoke((VirtualObject) iContent, getFeatures(ScriptingAspect.ASPECT_NAME));
			else if (inheritedEvent != null)
				// EXECUTE JAVA METHOD
				return inheritedEvent.invoke(((VirtualObject) iContent).getSuperClassObject());
		} catch (ScriptingException e) {
			throw new InvocationTargetException(e, "Error on invoking event " + name);
		}
		return null;
	}

	public void configure() {
		if (inheritedEvent != null) {
			// INHERITS ALL FEATURES FROM INHERITED EVENT
			try {
				allFeatures = ((SchemaEvent) inheritedEvent.clone()).getAllFeatures();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		// BROWSE ALL ASPECTS
		for (Aspect aspect : Roma.aspects()) {
			SchemaConfiguration classDescriptor = entity.getSchemaClass().getDescriptor();

			XmlEventAnnotation parentDescriptor = null;

			if (classDescriptor != null && classDescriptor.getType() != null && classDescriptor.getType().getEvents() != null) {
				// SEARCH FORM DEFINITION IN DESCRIPTOR
				for (XmlEventAnnotation tmpDescr : classDescriptor.getType().getEvents()) {
					if (tmpDescr.getName().equals(name)) {
						// FOUND XML NODE
						parentDescriptor = tmpDescr;
						break;
					}
				}
			}
			// CONFIGURE THE SCHEMA OBJECT WITH CURRENT ASPECT
			aspect.configEvent(this, null, null, parentDescriptor);
		}
	}
}
