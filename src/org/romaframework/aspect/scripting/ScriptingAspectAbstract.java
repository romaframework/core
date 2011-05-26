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

import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

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

	}

	public void configAction(SchemaClassElement action, Annotation actionAnnotation, Annotation genericAnnotation, XmlActionAnnotation iXmlNode) {
	}

	public void configEvent(SchemaEvent event, Annotation eventAnnotation, Annotation genericAnnotation, XmlEventAnnotation iXmlNode) {
	}

	public void configField(SchemaField field, Annotation fieldAnnotation, Annotation genericAnnotation, Annotation getterAnnotation, XmlFieldAnnotation node) {
	}

}
