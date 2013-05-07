package org.romaframework.aspect.core;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.romaframework.aspect.core.feature.CoreClassFeatures;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.AspectConfigurator;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.reflection.SchemaActionDelegate;
import org.romaframework.core.schema.reflection.SchemaEventDelegate;
import org.romaframework.core.schema.reflection.SchemaFieldDelegate;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;

public class CoreAspectConfigurator implements AspectConfigurator {

	public void beginConfigClass(SchemaClassDefinition iClass) {
		// TODO Auto-generated method stub

	}

	public void configClass(SchemaClassDefinition iClass) {

		if (iClass.isSetFeature(CoreClassFeatures.ENTITY) && iClass.getSchemaClass().isComposedEntity() && iClass.getField(ComposedEntity.NAME) != null) {
			iClass.getField(ComposedEntity.NAME).setType(iClass.getFeature(CoreClassFeatures.ENTITY));
		}
	}

	public void configField(SchemaField iField) {

		if (iField.getFeature(CoreFieldFeatures.EXPAND)) {
			expandField(iField, iField.getEntity());
		} else if (iField.isSetFeature(CoreFieldFeatures.EXPAND) && !iField.getFeature(CoreFieldFeatures.EXPAND)) {
			unexpandField(iField);
		}
		if (iField instanceof SchemaFieldReflection) {

			SchemaFieldReflection ref = (SchemaFieldReflection) iField;
			SchemaClass[] embeddedTypeGenerics = null;
			Type fieldType = null;
			if (ref.getGetterMethod() != null) {
				if ((fieldType = ref.getGetterMethod().getGenericReturnType()) == null) {
					fieldType = ref.getGetterMethod().getReturnType();
				}
			}

			if (fieldType == null && ref.getField() != null) {
				if ((fieldType = ref.getField().getGenericType()) == null) {
					fieldType = ref.getField().getType();
				}
			}

			if (fieldType != null && fieldType instanceof ParameterizedType) {
				ParameterizedType ownerType = SchemaHelper.resolveParameterizedType((Type) ref.getEntity().getSchemaClass().getLanguageType());
				ParameterizedType pt = (ParameterizedType) fieldType;
				if (pt.getActualTypeArguments() != null) {
					embeddedTypeGenerics = new SchemaClass[pt.getActualTypeArguments().length];
					int i = 0;
					for (Type argType : pt.getActualTypeArguments())
						embeddedTypeGenerics[i++] = Roma.schema().getSchemaClassIfExist(SchemaHelper.resolveClassFromType(argType, ownerType));
				}
				ref.setEmbeddedTypeGenerics(embeddedTypeGenerics);
				if (embeddedTypeGenerics != null && embeddedTypeGenerics.length > 0)
					ref.setEmbeddedType(embeddedTypeGenerics[0]);
			} else if (fieldType instanceof Class<?>) {
				Class<?> cls = (Class<?>) fieldType;
				if (cls.isArray())
					ref.setEmbeddedLanguageType(cls.getComponentType());
			}
		}
	}

	public void expandField(SchemaField iField, SchemaClassDefinition dest) {
		SchemaClass cl = iField.getType().getSchemaClass();
		Iterator<SchemaField> fields = cl.getFieldIterator();
		while (fields.hasNext()) {
			SchemaField sf = fields.next();
			SchemaField parentField = iField.getEntity().getField(sf.getName());
			if (parentField == null || parentField.getType().getSchemaClass().isAssignableAs(sf.getType().getSchemaClass())) {
				SchemaFieldDelegate sfd = new SchemaFieldDelegate(iField.getEntity(), iField, sf);
				sfd.configure();
				sfd.setOrder(iField.getEntity().getSchemaClass().getFieldOrder(sfd));
				dest.setField(sf.getName(), sfd);
			}
		}
		Iterator<SchemaAction> actions = cl.getActionIterator();
		while (actions.hasNext()) {
			SchemaAction sa = actions.next();
			SchemaActionDelegate sad = new SchemaActionDelegate(iField.getEntity(), iField, sa);
			sad.configure();
			sad.setOrder(iField.getEntity().getSchemaClass().getActionOrder(sad));
			dest.setAction(sa.getName(), sad);
		}
		Iterator<SchemaEvent> events = cl.getEventIterator();
		while (events.hasNext()) {
			SchemaEvent se = events.next();
			SchemaEventDelegate sed = new SchemaEventDelegate(iField.getEntity(), iField, se);
			sed.configure();
			dest.setEvent(se.getName(), sed);
		}

	}

	private void unexpandField(SchemaField iField) {
		SchemaClass cl = iField.getEntity().getSchemaClass();
		Iterator<SchemaField> fields = cl.getFieldIterator();
		List<SchemaField> toRemoveFields = new ArrayList<SchemaField>();
		while (fields.hasNext()) {
			SchemaField sf = fields.next();
			if (sf instanceof SchemaFieldDelegate) {
				if (((SchemaFieldDelegate) sf).getFieldObject().getName().equals(iField.getName()))
					toRemoveFields.add(sf);
			}
		}
		for (SchemaField schemaField : toRemoveFields) {
			cl.getFields().remove(schemaField.getName());
		}

		Iterator<SchemaAction> actions = cl.getActionIterator();
		List<SchemaAction> toRemoveActions = new ArrayList<SchemaAction>();
		while (actions.hasNext()) {
			SchemaAction sf = actions.next();
			if (sf instanceof SchemaActionDelegate) {
				if (((SchemaActionDelegate) sf).getFieldObject().getName().equals(iField.getName()))
					toRemoveActions.add(sf);
			}
		}
		for (SchemaAction schemaField : toRemoveActions) {
			cl.getActions().remove(schemaField.getName());
		}

		Iterator<SchemaEvent> events = cl.getEventIterator();
		List<SchemaEvent> toRemoveEvents = new ArrayList<SchemaEvent>();
		while (actions.hasNext()) {
			SchemaEvent sf = events.next();
			if (sf instanceof SchemaEventDelegate) {
				if (((SchemaEventDelegate) sf).getFieldObject().getName().equals(iField.getName()))
					toRemoveEvents.add(sf);
			}
		}
		for (SchemaEvent schemaEvent : toRemoveEvents) {
			cl.removeEvent(schemaEvent);
		}

	}

	public void configAction(SchemaAction iAction) {
		// TODO Auto-generated method stub

	}

	public void configEvent(SchemaEvent iEvent) {
		// TODO Auto-generated method stub

	}

	public void endConfigClass(SchemaClassDefinition iClass) {
		// TODO Auto-generated method stub

	}

}
