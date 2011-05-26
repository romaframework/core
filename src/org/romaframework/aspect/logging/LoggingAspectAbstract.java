/*
 * Copyright 2006-2007 Luca Garulli      (luca.garulli--at--assetdata.it)
 *                     Giordano Maestro  (giordano.maestro--at--assetdata.it)
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

package org.romaframework.aspect.logging;

import java.lang.annotation.Annotation;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

/**
 * Abstract implementation for Logging Aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class LoggingAspectAbstract extends SelfRegistrantConfigurableModule<String> implements LoggingAspect {

	public static final String	ASPECT_NAME	= "logging";

	protected Log								log					= LogFactory.getLog(this.getClass());

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iNode) {
	}

	public void configField(SchemaField iField, Annotation iAnnotation, Annotation iGenericAnnotation, Annotation getterAnnotation, XmlFieldAnnotation iXmlNode) {
	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation, XmlActionAnnotation iXmlNode) {
	}

	public void configEvent(SchemaEvent iEvent, Annotation iEventAnnotation, Annotation iGenericAnnotation, XmlEventAnnotation iXmlNode) {
		configAction(iEvent, iEventAnnotation, iGenericAnnotation, iXmlNode);
	}

	public String aspectName() {
		return ASPECT_NAME;
	}
}
