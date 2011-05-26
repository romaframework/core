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

package org.romaframework.aspect.persistence;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.persistence.feature.PersistenceFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

/**
 * Persistence aspect. Manages application objects in datastore.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class PersistenceAspectAbstract implements PersistenceAspect {

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configField(SchemaField iField, Annotation iAnnotation, Annotation iGenericAnnotation, Annotation getterAnnotation,
			XmlFieldAnnotation iXmlNode) {
		configCommonAnnotations(iField, iAnnotation, iGenericAnnotation);
		readFieldXml(iField, iXmlNode);
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iXmlNode) {
		configCommonAnnotations(iAction, iActionAnnotation, iGenericAnnotation);
		readActionXml(iAction, iXmlNode);
	}

	public void configClass(SchemaClassDefinition class1, Annotation annotation, XmlClassAnnotation node) {
	}

	private void configCommonAnnotations(SchemaClassElement iElement, Annotation iAnnotation, Annotation iGenericAnnotation) {
		DynaBean features = iElement.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new PersistenceFeatures();
			iElement.setFeatures(ASPECT_NAME, features);
		}

		readAnnotation(iElement, iGenericAnnotation, features);
		readAnnotation(iElement, iAnnotation, features);
	}

	private void readAnnotation(SchemaClassElement iElement, Annotation iAnnotation, DynaBean features) {
		Persistence annotation = (Persistence) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
			if (annotation != null) {
				if (!annotation.mode().equals(PersistenceConstants.MODE_NOTHING))
					features.setAttribute(PersistenceFeatures.MODE, annotation.mode());
			}
		}
	}

	private void readFieldXml(SchemaClassElement iElement, XmlFieldAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iElement.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		// TODO check default!!!
		if (descriptor != null) {
			features.setAttribute(PersistenceFeatures.MODE, descriptor.getAttribute(PersistenceFeatures.MODE));
		}
	}

	private void readActionXml(SchemaClassElement iElement, XmlActionAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iElement.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			features.setAttribute(PersistenceFeatures.MODE, descriptor.getAttribute(PersistenceFeatures.MODE));
		}
	}

	public void configEvent(SchemaEvent iEvent, Annotation iEventAnnotation, Annotation iGenericAnnotation,
			XmlEventAnnotation iXmlNode) {
		configAction(iEvent, iEventAnnotation, iGenericAnnotation, iXmlNode);
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	public static PersistenceAspect getPersistenceComponent(String iMode) {
		if (iMode == null)
			return null;

		String componentName;
		if (iMode.equals(PersistenceConstants.MODE_TX)) {
			componentName = "TxPersistenceAspect";
		} else if (iMode.equals(PersistenceConstants.MODE_ATOMIC)) {
			componentName = "PersistenceAspect";
		} else if (iMode.equals(PersistenceConstants.MODE_NOTX)) {
			componentName = "NoTxPersistenceAspect";
		} else
			throw new IllegalArgumentException("Persistence mode not supported: " + iMode);

		return Roma.component(componentName);
	}
}
