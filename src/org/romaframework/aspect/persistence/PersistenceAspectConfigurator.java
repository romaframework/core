package org.romaframework.aspect.persistence;

import org.romaframework.aspect.persistence.annotation.Persistence;
import org.romaframework.aspect.persistence.feature.PersistenceFeatures;
import org.romaframework.core.aspect.AspectConfigurator;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.reflection.SchemaActionReflection;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;

public class PersistenceAspectConfigurator implements AspectConfigurator {

	public void beginConfigClass(SchemaClassDefinition iClass) {
		// TODO Auto-generated method stub

	}

	public void configClass(SchemaClassDefinition iClass) {
		// TODO Auto-generated method stub

	}

	public void configField(SchemaField iField) {
		configCommonAnnotations(iField);
	}

	public void configAction(SchemaAction iAction) {
		configCommonAnnotations(iAction);
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
		// PROCESS ANNOTATIONS
		// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
		if (annotation != null) {
			if (!annotation.mode().equals(PersistenceConstants.MODE_NOTHING))
				iElement.setFeature(PersistenceFeatures.MODE, annotation.mode());
		}

	}

	public void configEvent(SchemaEvent iEvent) {
		configAction(iEvent);
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
		// TODO Auto-generated method stub

	}

}
