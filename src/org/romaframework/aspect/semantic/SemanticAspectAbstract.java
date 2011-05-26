/*
 * Copyright 2008 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.aspect.semantic;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.semantic.annotation.SemanticClass;
import org.romaframework.aspect.semantic.annotation.SemanticField;
import org.romaframework.aspect.semantic.feature.SemanticClassFeatures;
import org.romaframework.aspect.semantic.feature.SemanticFieldFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

/**
 * IMPORTANT: PLEASE DO NOT RELY ON THIS RESOURCE, IT IS UNDER DEFINITION AND HEAVY DEVELOPMENT
 * 
 * @author Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 * 
 */
public abstract class SemanticAspectAbstract extends SelfRegistrantConfigurableModule<String> implements SemanticAspect {

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {

		DynaBean features = iClass.getFeatures(SemanticAspect.ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new SemanticClassFeatures();
			iClass.setFeatures(SemanticAspect.ASPECT_NAME, features);
		}

		readClassAnnotation(iAnnotation, features);
		readClassXml(iClass, iXmlNode);
	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation,
			Annotation iGetterAnnotation, XmlFieldAnnotation iXmlNode) {
		DynaBean features = iField.getFeatures(SemanticAspect.ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new SemanticFieldFeatures();
			iField.setFeatures(SemanticAspect.ASPECT_NAME, features);
		}

		readFieldAnnotation(iFieldAnnotation, features);
		readFieldAnnotation(iGetterAnnotation, features);
		readFieldXml(iField, iXmlNode);
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iNode) {
		// Semantic cannot be used on actions
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	private void readClassAnnotation(Annotation iAnnotation, DynaBean features) {
		SemanticClass annotation = (SemanticClass) iAnnotation;
		if (annotation != null) {
			// PROCESS ANNOTATIONS
			features.setAttribute(SemanticClassFeatures.SUBJECT_ID, annotation.subjectId());
			features.setAttribute(SemanticClassFeatures.SUBJECT_PREFIX, annotation.subjectPrefix());
			features.setAttribute(SemanticClassFeatures.CLASS_URI, annotation.classUri());
		}
	}

	private void readClassXml(SchemaClassDefinition iClass, XmlClassAnnotation iXmlNode) {
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iClass.getFeatures(SemanticAspect.ASPECT_NAME);

		XmlAspectAnnotation featureDescriptor = iXmlNode.aspect(ASPECT_NAME);

		if (featureDescriptor != null) {

			// PROCESS DESCRIPTOR CFG
			if (featureDescriptor != null) {
				String subjectId = featureDescriptor.getAttribute(SemanticClassFeatures.SUBJECT_ID);
				if (subjectId != null) {
					features.setAttribute(SemanticClassFeatures.SUBJECT_ID, subjectId);
				}
				String subjectPrefix = featureDescriptor.getAttribute(SemanticClassFeatures.SUBJECT_PREFIX);
				if (subjectPrefix != null) {
					features.setAttribute(SemanticClassFeatures.SUBJECT_PREFIX, subjectPrefix);
				}
				String classUri = featureDescriptor.getAttribute(SemanticClassFeatures.CLASS_URI);
				if (classUri != null) {
					features.setAttribute(SemanticClassFeatures.CLASS_URI, classUri);
				}
			}
		}
	}

	private void readFieldAnnotation(Annotation iAnnotation, DynaBean iFeatures) {
		SemanticField annotation = (SemanticField) iAnnotation;

		if (annotation != null) {
			if (annotation.predicate() != null)
				iFeatures.setAttribute(SemanticFieldFeatures.PREDICATE, annotation.predicate());

			if (annotation.resources() != null)
				iFeatures.setAttribute(SemanticFieldFeatures.RESOURCES, annotation.resources());
		}

	}

	private void readFieldXml(SchemaField iField, XmlFieldAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION VALUES
		if (iXmlNode == null)
			return;

		DynaBean features = iField.getFeatures(ASPECT_NAME);

		if (iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			String predicate = descriptor.getAttribute(SemanticFieldFeatures.PREDICATE);
			if (predicate != null)
				features.setAttribute(SemanticFieldFeatures.PREDICATE, predicate);

			String resources = descriptor.getAttribute(SemanticFieldFeatures.RESOURCES);
			if (resources != null) {
				String[] items = resources.split(",");
				features.setAttribute(SemanticFieldFeatures.RESOURCES, items);
			}
		}
	}
}
