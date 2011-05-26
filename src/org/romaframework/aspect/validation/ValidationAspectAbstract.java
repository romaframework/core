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

import org.romaframework.aspect.validation.feature.ValidationFieldFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

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

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation, Annotation iGetterAnnotation,
			XmlFieldAnnotation iXmlNode) {

		if (checkFeature(iField, ValidationFieldFeatures.MATCH) && checkFeature(iField, ValidationFieldFeatures.ENABLED)
				&& checkFeature(iField, ValidationFieldFeatures.MAX) && checkFeature(iField, ValidationFieldFeatures.MIN)
				&& checkFeature(iField, ValidationFieldFeatures.REQUIRED))
			iField.setFeature(ValidationFieldFeatures.ENABLED, true);
		else
			iField.setFeature(ValidationFieldFeatures.ENABLED, false);
	}

	private boolean checkFeature(SchemaField field, Feature<?> feature) {
		return field.getFeature(feature) != feature.getDefaultValue();
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation, XmlActionAnnotation iXmlNode) {

	}

	public void configEvent(SchemaEvent event, Annotation eventAnnotation, Annotation genericAnnotation, XmlEventAnnotation node) {
	}

	public Object getUnderlyingComponent() {
		return null;
	}

}
