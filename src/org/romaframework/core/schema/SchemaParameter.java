/*
 * Copyright 2009 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.core.schema;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.romaframework.core.schema.xmlannotations.XmlParameterAnnotation;

public class SchemaParameter extends SchemaElement {

	private static final long	serialVersionUID	= -1471432131609926218L;

	private SchemaClass				type;
	private int								index;

	public SchemaParameter(String iName, int index, SchemaClass iType) {
		super(iName, FeatureType.PARAMETER);
		type = iType;
		this.index = index;
	}

	public SchemaParameter(String iName) {
		super(iName, FeatureType.PARAMETER);
	}

	public void configure(Method method, XmlParameterAnnotation xmlParam) {

		if (xmlParam != null && xmlParam.getName() != null && !xmlParam.getName().trim().isEmpty()) {
			// TODO: move on init of schema parameter.
			this.name = xmlParam.getName();
		}

		Annotation[][] annotations = null;
		if (method != null)
			annotations = method.getParameterAnnotations();
		Annotation[] paramAnnotations = null;
		if (annotations.length > this.index)
			paramAnnotations = annotations[this.index];
		FeatureLoader.loadParameterFeatures(this, paramAnnotations, xmlParam);
	}

	public SchemaClass getType() {
		return type;
	}

	public void setType(SchemaClass type) {
		this.type = type;
	}
}
