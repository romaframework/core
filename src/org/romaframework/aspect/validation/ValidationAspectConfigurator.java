package org.romaframework.aspect.validation;

import org.romaframework.aspect.validation.feature.ValidationFieldFeatures;
import org.romaframework.core.aspect.AspectConfigurator;
import org.romaframework.core.schema.Feature;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;

public class ValidationAspectConfigurator implements AspectConfigurator {

	public void beginConfigClass(SchemaClassDefinition iClass) {

	}

	public void configClass(SchemaClassDefinition iClass) {

	}

	public void configField(SchemaField iField) {

		if (checkFeature(iField, ValidationFieldFeatures.REQUIRED) || checkFeature(iField, ValidationFieldFeatures.MAX) || checkFeature(iField, ValidationFieldFeatures.MIN)
				|| checkFeature(iField, ValidationFieldFeatures.MATCH))
			iField.setFeature(ValidationFieldFeatures.ENABLED, true);
		else if (!checkFeature(iField, ValidationFieldFeatures.ENABLED))
			iField.setFeature(ValidationFieldFeatures.ENABLED, false);
	}

	private boolean checkFeature(SchemaField field, Feature<?> feature) {
		return field.isSetFeature(feature);
	}

	public void configAction(SchemaAction iAction) {

	}

	public void configEvent(SchemaEvent iEvent) {

	}

	public void endConfigClass(SchemaClassDefinition iClass) {
		// TODO Auto-generated method stub

	}

}
