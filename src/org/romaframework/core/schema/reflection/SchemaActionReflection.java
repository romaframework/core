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

package org.romaframework.core.schema.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.FeatureLoader;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaParameter;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.virtual.VirtualObject;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;

/**
 * Represent a method of a class.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaActionReflection extends SchemaAction {
	private static final long			serialVersionUID	= -6572077296198245321L;
	protected Method							method;
	final static public Object[]	NO_PARAMETERS			= new Object[] {};

	public SchemaActionReflection(SchemaClassDefinition iEntity, String iName, List<SchemaParameter> iOrderedParameters) {
		super(iEntity, iName, iOrderedParameters);
	}

	@Override
	public Object invokeFinal(Object iContent, Object[] params) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		while (iContent instanceof VirtualObject)
			iContent = ((VirtualObject) iContent).getSuperClassObject();

		return method.invoke(iContent, params);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SchemaActionReflection copy = (SchemaActionReflection) super.clone();
		copy.method = method;
		return copy;
	}

	public void configure() {

		SchemaConfiguration classDescriptor = entity.getSchemaClass().getDescriptor();

		XmlActionAnnotation parentDescriptor = null;

		if (classDescriptor != null && classDescriptor.getType() != null) {
			// SEARCH FORM DEFINITION IN DESCRIPTOR
			parentDescriptor = classDescriptor.getType().getAction(name);

		}
		FeatureLoader.loadActionFeatures(this, parentDescriptor);

		// BROWSE ALL ASPECTS
		for (Aspect aspect : Roma.aspects()) {
			// CONFIGURE THE SCHEMA OBJECT WITH CURRENT ASPECT
			aspect.configAction(this, null, null, parentDescriptor);
		}
	}

	public Method getMethod() {
		return method;
	}
}
