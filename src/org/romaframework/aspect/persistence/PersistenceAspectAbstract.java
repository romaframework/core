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


import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.persistence.feature.PersistenceFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.reflection.SchemaActionReflection;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;

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

	public void configField(SchemaField iField) {
		configCommonAnnotations(iField);
	}

	public void configAction(SchemaAction iAction) {
		configCommonAnnotations(iAction);
	}

	public void configClass(SchemaClassDefinition class1) {
	}

	private void configCommonAnnotations(SchemaClassElement iElement) {
		Persistence annotation = null;
		if (iElement instanceof SchemaFieldReflection && ((SchemaFieldReflection) iElement).getGetterMethod() != null) {
			annotation = (Persistence) ((SchemaFieldReflection) iElement).getGetterMethod().getAnnotation(Persistence.class);
			if (annotation == null && ((SchemaFieldReflection) iElement).getField() != null)
				annotation = (Persistence) ((SchemaFieldReflection) iElement).getField().getAnnotation(Persistence.class);
		}
		if (iElement instanceof SchemaActionReflection) {
			annotation = (Persistence) ((SchemaActionReflection) iElement).getMethod().getAnnotation(Persistence.class);
		}
		if (annotation != null) {
			// PROCESS ANNOTATIONS
			// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
			if (annotation != null) {
				if (!annotation.mode().equals(PersistenceConstants.MODE_NOTHING))
					iElement.setFeature(PersistenceFeatures.MODE, annotation.mode());
			}
		}

	}

	public void configEvent(SchemaEvent iEvent) {
		configAction(iEvent);
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
