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

package org.romaframework.aspect.monitoring;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.monitoring.annotation.MonitoringAction;
import org.romaframework.aspect.monitoring.annotation.MonitoringClass;
import org.romaframework.aspect.monitoring.annotation.MonitoringField;
import org.romaframework.aspect.monitoring.feature.MonitoringFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaFeatures;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

/**
 * Extend this if you want to write your own Aspect implementation.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public abstract class MonitoringAspectAbstract extends SelfRegistrantConfigurableModule<String> implements MonitoringAspect {

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {
		DynaBean features = iClass.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new MonitoringFeatures();
			iClass.setFeatures(ASPECT_NAME, features);
		}

		readClassAnnotation(iAnnotation, features);
		readClassXml(iClass, iXmlNode);
		setDefaults(iClass);
	}

	private void readClassAnnotation(Annotation iAnnotation, DynaBean features) {
		MonitoringClass annotation = (MonitoringClass) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			if (annotation.enabled() != AnnotationConstants.UNSETTED)
				features.setAttribute(MonitoringFeatures.ENABLED, annotation.enabled() == AnnotationConstants.TRUE);
		}
	}

	private void readClassXml(SchemaClassDefinition iClass, XmlClassAnnotation iXmlNode) {
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iClass.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation featureDescriptor = iXmlNode.aspect(ASPECT_NAME);

		if (featureDescriptor != null) {
			// PROCESS DESCRIPTOR CFG
			if (featureDescriptor != null) {
				String enabled = featureDescriptor.getAttribute(MonitoringFeatures.ENABLED);
				if (enabled != null)
					features.setAttribute(MonitoringFeatures.ENABLED, new Boolean(enabled));
			}
		}
	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation,
			Annotation iGetterAnnotation, XmlFieldAnnotation iXmlNode) {
		DynaBean features = iField.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new MonitoringFeatures();
			iField.setFeatures(ASPECT_NAME, features);
		}

		readFieldAnnotation(iField, iFieldAnnotation, features);
		readFieldAnnotation(iField, iGetterAnnotation, features);
		readFieldXml(iField, iXmlNode);
		setDefaults(iField);
	}

	private void readFieldAnnotation(SchemaField iField, Annotation iAnnotation, DynaBean features) {
		MonitoringField annotation = (MonitoringField) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
			if (annotation != null) {
				if (annotation.enabled() != AnnotationConstants.UNSETTED)
					features.setAttribute(MonitoringFeatures.ENABLED, annotation.enabled() == AnnotationConstants.TRUE);
			}
		}
	}

	private void readFieldXml(SchemaField iField, XmlFieldAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iField.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			String enabled = descriptor.getAttribute(MonitoringFeatures.ENABLED);
			if (enabled != null)
				features.setAttribute(MonitoringFeatures.ENABLED, new Boolean(enabled));
		}
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iXmlNode) {
		DynaBean features = iAction.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new MonitoringFeatures();
			iAction.setFeatures(ASPECT_NAME, features);
		}

		readActionAnnotation(iAction, iActionAnnotation, features);
		readActionXml(iAction, iXmlNode);
		setDefaults(iAction);
	}

	private void readActionAnnotation(SchemaClassElement iAction, Annotation iAnnotation, DynaBean features) {
		MonitoringAction annotation = (MonitoringAction) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
			if (annotation != null) {
				if (annotation.enabled() != AnnotationConstants.UNSETTED)
					features.setAttribute(MonitoringFeatures.ENABLED, annotation.enabled() == AnnotationConstants.TRUE);
			}
		}
	}

	private void readActionXml(SchemaClassElement iAction, XmlActionAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iAction.getFeatures(ASPECT_NAME);

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			String enabled = descriptor.getAttribute(MonitoringFeatures.ENABLED);
			if (enabled != null)
				features.setAttribute(MonitoringFeatures.ENABLED, new Boolean(enabled));
		}
	}

	private void setDefaults(SchemaFeatures iElement) {
		DynaBean features = iElement.getFeatures(ASPECT_NAME);

		if (features.getAttribute(MonitoringFeatures.ENABLED) == null)
			features.setAttribute(MonitoringFeatures.ENABLED, true);
	}

	public String aspectName() {
		return ASPECT_NAME;
	}
}
