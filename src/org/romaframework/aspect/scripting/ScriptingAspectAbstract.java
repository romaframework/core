/*
 * Copyright 2008 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
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

package org.romaframework.aspect.scripting;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.scripting.feature.ScriptingFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaFeatures;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

public abstract class ScriptingAspectAbstract extends SelfRegistrantConfigurableModule<String> implements ScriptingAspect {

	public String aspectName() {
		return ASPECT_NAME;
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	/**
	 * Used to define the constructor.
	 */
	public void configClass(SchemaClassDefinition iClass, Annotation annotation, XmlClassAnnotation iXmlNode) {
		DynaBean features = iClass.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new ScriptingFeatures();
			iClass.setFeatures(ASPECT_NAME, features);
		}

		readXml(iClass, iXmlNode);
	}

	public void configAction(SchemaClassElement action, Annotation actionAnnotation, Annotation genericAnnotation,
			XmlActionAnnotation iXmlNode) {
		DynaBean features = action.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new ScriptingFeatures();
			action.setFeatures(ASPECT_NAME, features);
		}

		readXml(action, iXmlNode);
	}

	public void configEvent(SchemaEvent event, Annotation eventAnnotation, Annotation genericAnnotation, XmlEventAnnotation iXmlNode) {
		DynaBean features = event.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new ScriptingFeatures();
			event.setFeatures(ASPECT_NAME, features);
		}

		readXml(event, iXmlNode);
	}

	public void configField(SchemaField field, Annotation fieldAnnotation, Annotation genericAnnotation, Annotation getterAnnotation,
			XmlFieldAnnotation node) {
	}

	protected void readXml(SchemaFeatures iElement, XmlAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iElement.getFeatures(ASPECT_NAME);
		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			String language = descriptor.getAttribute(ScriptingFeatures.LANGUAGE);
			if (language != null)
				features.setAttribute(ScriptingFeatures.LANGUAGE, language);

			String code = descriptor.getText();
			if (code != null)
				features.setAttribute(ScriptingFeatures.CODE, code);
		}
	}
}
