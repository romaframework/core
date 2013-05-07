package org.romaframework.aspect.serialization;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import javax.xml.bind.annotation.XmlRootElement;

import org.romaframework.aspect.serialization.feature.SerializationClassFeatures;
import org.romaframework.aspect.serialization.feature.SerializationFieldFeatures;
import org.romaframework.core.aspect.AspectConfigurator;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.reflection.SchemaClassReflection;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;

public class SerializationAspectConfigurator implements AspectConfigurator {

	public void beginConfigClass(SchemaClassDefinition iClass) {

	}

	public void configClass(SchemaClassDefinition iClass) {
		if (iClass instanceof SchemaClassReflection) {
			String name;
			Class<?> clazz = ((SchemaClassReflection) iClass).getLanguageType();
			XmlRootElement xmlRootElement = clazz.getAnnotation(XmlRootElement.class);
			if (xmlRootElement != null)
				name = xmlRootElement.name();
			else
				name = clazz.getSimpleName();
			iClass.setFeature(SerializationClassFeatures.ROOT_ELEMENT_NAME, name);
		}
	}

	public void configField(SchemaField iField) {

		if (iField instanceof SchemaFieldReflection) {
			Field field = ((SchemaFieldReflection) iField).getField();
			if (field != null) {
				Boolean trans = Modifier.isTransient(field.getModifiers());
				iField.setFeature(SerializationFieldFeatures.TRANSIENT, trans);
			}
		}
	}

	public void configAction(SchemaAction iAction) {

	}

	public void configEvent(SchemaEvent iEvent) {

	}

	public void endConfigClass(SchemaClassDefinition iClass) {

	}

}
