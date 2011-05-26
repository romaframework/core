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

package org.romaframework.aspect.validation;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.validation.annotation.ValidationAction;
import org.romaframework.aspect.validation.annotation.ValidationField;
import org.romaframework.aspect.validation.feature.ValidationActionFeatures;
import org.romaframework.aspect.validation.feature.ValidationFieldFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

public abstract class ValidationAspectAbstract extends SelfRegistrantConfigurableModule<String> implements ValidationAspect {

	public String aspectName() {
		return ASPECT_NAME;
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {
	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation,
			Annotation iGetterAnnotation, XmlFieldAnnotation iXmlNode) {
		DynaBean features = iField.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new ValidationFieldFeatures();
			iField.setFeatures(ASPECT_NAME, features);
		}

		readFieldAnnotation(iGenericAnnotation, features);
		readFieldAnnotation(iFieldAnnotation, features);
		readFieldAnnotation(iGetterAnnotation, features);
		readFieldXml(iField, iXmlNode);
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iXmlNode) {
		DynaBean features = iAction.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new ValidationActionFeatures();
			iAction.setFeatures(ASPECT_NAME, features);
		}

		readActionAnnotation(iAction, iActionAnnotation, features);
		readActionXml(iAction, iXmlNode);
	}

	private void readFieldAnnotation(Annotation iAnnotation, DynaBean iFeatures) {
		ValidationField annotation = (ValidationField) iAnnotation;

		if (annotation != null) {
			iFeatures.setAttribute(ValidationFieldFeatures.ENABLED, Boolean.TRUE);

			if (annotation.required() != AnnotationConstants.UNSETTED)
				iFeatures.setAttribute(ValidationFieldFeatures.REQUIRED, annotation.required() == AnnotationConstants.TRUE);
			if (!annotation.match().equals(AnnotationConstants.DEF_VALUE))
				iFeatures.setAttribute(ValidationFieldFeatures.MATCH, annotation.match());
			if (annotation.min() != ValidationField.DEF_MIN)
				iFeatures.setAttribute(ValidationFieldFeatures.MIN, annotation.min());
			if (annotation.max() != ValidationField.DEF_MAX)
				iFeatures.setAttribute(ValidationFieldFeatures.MAX, annotation.max());
		}
	}

	private void readFieldXml(SchemaField iField, XmlFieldAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null)
			return;

		DynaBean features = iField.getFeatures(ASPECT_NAME);

		if (iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			features.setAttribute(ValidationFieldFeatures.ENABLED, Boolean.TRUE);

			String required = descriptor.getAttribute(ValidationFieldFeatures.REQUIRED);
			if (required != null) {
				features.setAttribute(ValidationFieldFeatures.REQUIRED, new Boolean(required));
			}
			String match = descriptor.getAttribute(ValidationFieldFeatures.MATCH);
			if (match != null) {
				features.setAttribute(ValidationFieldFeatures.MATCH, match);
			}
			String min = descriptor.getAttribute(ValidationFieldFeatures.MIN);
			if (min != null) {
				features.setAttribute(ValidationFieldFeatures.MIN, new Integer(min));
			}
			String max = descriptor.getAttribute(ValidationFieldFeatures.MAX);
			if (max != null) {
				features.setAttribute(ValidationFieldFeatures.MAX, new Integer(max));
			}
		}
	}

	private void readActionAnnotation(SchemaClassElement iAction, Annotation iAnnotation, DynaBean features) {
		ValidationAction annotation = (ValidationAction) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
			if (annotation != null) {
				if (annotation.validation() != AnnotationConstants.UNSETTED)
					features.setAttribute(ValidationActionFeatures.ENABLED, annotation.validation() == AnnotationConstants.TRUE);
			}
		}
	}

	private void readActionXml(SchemaClassElement iAction, XmlActionAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null)
			return;

		DynaBean features = iAction.getFeatures(ASPECT_NAME);

		if (iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			String validation = descriptor.getAttribute(ValidationActionFeatures.ENABLED);
			if (validation != null)
				features.setAttribute(ValidationActionFeatures.ENABLED, new Boolean(validation));
		}
	}

	public void configEvent(SchemaEvent event, Annotation eventAnnotation, Annotation genericAnnotation, XmlEventAnnotation node) {
	}

	public Object getUnderlyingComponent() {
		return null;
	}

}
