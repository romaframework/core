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
import java.util.Collection;

import org.romaframework.aspect.scripting.ScriptingAspect;
import org.romaframework.aspect.scripting.exception.ScriptingException;
import org.romaframework.aspect.scripting.feature.ScriptingFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.util.DynaBean;

/**
 * Represent a method of a class.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaActionVirtual extends SchemaAction {
	protected SchemaAction			inheritedAction;

	public static final String	DEFAULT_LANGUAGE	= "JavaScript";

	public SchemaActionVirtual(SchemaClassDefinition iEntity, String iName) {
		super(iEntity, iName, null);
	}

	public SchemaActionVirtual(SchemaClassDefinition iEntity, String iName, SchemaAction iInherited) {
		super(iEntity, iName);
		inheritedAction = iInherited;
	}

	public SchemaActionVirtual(SchemaClassDefinition iEntity, String iName, String iLanguage, String iCode) {
		super(iEntity, iName, null);

		DynaBean features = getFeatures(ScriptingAspect.ASPECT_NAME);
		if (features == null) {
			features = new ScriptingFeatures();
			setFeatures(ScriptingAspect.ASPECT_NAME, features);
		}

		if (iLanguage != null)
			features.setAttribute(ScriptingFeatures.LANGUAGE, iLanguage);

		features.setAttribute(ScriptingFeatures.CODE, iCode);
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		if (Roma.scripting() == null)
			throw new ConfigurationException("No ScriptingAspect implementation found. Add it as module of the current project");

		try {
			if (getFeatures(ScriptingAspect.ASPECT_NAME).getAttribute(ScriptingFeatures.CODE) != null) {
				return VirtualObjectHelper.invoke((VirtualObject) iContent, getFeatures(ScriptingAspect.ASPECT_NAME));
			} else if (inheritedAction != null)
				// EXECUTE JAVA METHOD
				return inheritedAction.invoke(((VirtualObject) iContent).getSuperClassObject());
		} catch (ScriptingException e) {
			throw new InvocationTargetException(e, "Error on invoking action " + name);
		}
		return null;
	}

	public void configure() {
		if (inheritedAction != null) {
			// INHERITS ALL FEATURES FROM INHERITED ACTION
			try {
				allFeatures = ((SchemaAction) inheritedAction.clone()).getAllFeatures();
			} catch (CloneNotSupportedException e) {
				e.printStackTrace();
			}
		}

		// BROWSE ALL ASPECTS
		for (Aspect aspect : Roma.aspects()) {
			SchemaConfiguration classDescriptor = entity.getSchemaClass().getDescriptor();

			XmlActionAnnotation parentDescriptor = null;

			if (classDescriptor != null && classDescriptor.getType() != null && classDescriptor.getType().getActions() != null) {
				// SEARCH FORM DEFINITION IN DESCRIPTOR
				Collection<XmlActionAnnotation> allActions = classDescriptor.getType().getActions();

				for (XmlActionAnnotation tmpDescr : allActions) {
					if (tmpDescr.getName().equals(name)) {
						// FOUND XML NODE
						parentDescriptor = tmpDescr;
						break;
					}
				}
			}
			// CONFIGURE THE SCHEMA OBJECT WITH CURRENT ASPECT
			aspect.configAction(this, null, null, parentDescriptor);
		}
	}
}
