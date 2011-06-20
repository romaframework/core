package org.romaframework.core.schema.virtual;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;

import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.SchemaFieldListener;
import org.romaframework.core.schema.FeatureLoader;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.config.EmbeddedSchemaConfiguration;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

public class SchemaFieldVirtual extends SchemaField {
	protected SchemaField	inheritedField;

	public SchemaFieldVirtual(final SchemaClassDefinition entity, final String iName) {
		super(entity, iName);
	}

	public SchemaFieldVirtual(final SchemaClassVirtual schemaClassVirtual, final String iName, final SchemaField fieldInfo) {
		this(schemaClassVirtual, iName);
		inheritedField = fieldInfo;
	}

	public SchemaFieldVirtual(SchemaClassDefinition entity, String iName, SchemaClass iType) {
		super(entity, iName);
		type = iType;
	}

	public SchemaFieldVirtual(final SchemaClassDefinition entity, final String iName, String iFieldType) {
		super(entity, iName);
		String schemaClass;

		if (iFieldType.equals("string"))
			schemaClass = "String";
		else if (iFieldType.equals("boolean"))
			schemaClass = "Boolean";
		else if (iFieldType.equals("integer") || iFieldType.equals("int"))
			schemaClass = "Integer";
		else if (iFieldType.equals("date"))
			schemaClass = "Date";
		else if (iFieldType.equals("long"))
			schemaClass = "Long";
		else if (iFieldType.equals("byte"))
			schemaClass = "byte";
		else if (iFieldType.equals("char") || iFieldType.equals("character"))
			schemaClass = "Character";
		else if (iFieldType.equals("float") || iFieldType.equals("number"))
			schemaClass = "Float";
		else if (iFieldType.equals("double"))
			schemaClass = "double";
		else
			schemaClass = iFieldType;

		type = Roma.schema().getSchemaClass(schemaClass);
	}

	@Override
	public boolean isArray() {
		return getType().getSchemaClass().isArray();
	}

	public void configure(SchemaClass iFieldType) {
		type = iFieldType;
		SchemaConfiguration classDescriptor = entity.getSchemaClass().getDescriptor();

		XmlFieldAnnotation parentDescriptor = null;

		FeatureLoader.loadFieldFeatures(this, parentDescriptor);
		if (classDescriptor != null && classDescriptor.getType() != null) {
			// SEARCH FORM DEFINITION IN DESCRIPTOR
			parentDescriptor = classDescriptor.getType().getField(name);
		}

		// BROWSE ALL ASPECTS
		for (Aspect aspect : Roma.aspects()) {
			// COMPOSE ANNOTATION NAME BY ASPECT
			parentDescriptor = null;

			// CONFIGURE THE SCHEMA OBJECT WITH CURRENT ASPECT
			aspect.configField(this);
		}

		if (parentDescriptor != null) {
			// CONFIGURE EMBEDDED CLASS IF ANY
			XmlClassAnnotation subClass = parentDescriptor.getClassAnnotation();
			SchemaConfiguration fieldSchemaDescr = null;
			if (subClass != null) {
				// INLINE DESCRIPTOR
				SchemaConfiguration sourceDescr = getEntity().getSchemaClass().getDescriptor();
				if (sourceDescr != null) {
					fieldSchemaDescr = new EmbeddedSchemaConfiguration(subClass);

					setType(Roma.schema().createSchemaClass(getEntity().getSchemaClass().getName() + "." + getName(), getType().getSchemaClass(), fieldSchemaDescr));
				}
			}

			if (parentDescriptor.getEvents() != null) {
				Set<XmlEventAnnotation> events = parentDescriptor.getEvents();
				for (XmlEventAnnotation xmlConfigEventType : events) {

					SchemaEventVirtual eventInfo = (SchemaEventVirtual) getEvent(xmlConfigEventType.getName());

					if (eventInfo == null) {
						// EVENT NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
						eventInfo = new SchemaEventVirtual(this, xmlConfigEventType.getName());
						setEvent(xmlConfigEventType.getName(), eventInfo);
					}
					eventInfo.configure();
				}
			}
		}
	}

	@Override
	public Object getValue(Object iObject) throws BindingException {
		try {
			// CREATE THE CONTEXT BEFORE TO CALL THE ACTION
			Roma.context().create();

			if (inheritedField != null)
				// DELEGATE TO INHERITED GETTER
				return inheritedField.getValue(((VirtualObject) iObject).getSuperClassObject());

			VirtualObject pojo = getVPojo(iObject);

			List<SchemaFieldListener> listeners = Controller.getInstance().getListeners(SchemaFieldListener.class);

			// CALL ALL LISTENERS BEFORE TO RETURN THE VALUE
			Object value = invokeCallbackBeforeFieldRead(listeners, iObject);

			if (value != SchemaFieldListener.IGNORED)
				return value;

			value = pojo.getValue(name);

			// CALL ALL LISTENERS BEFORE TO RETURN THE VALUE
			invokeCallbackAfterFieldRead(listeners, iObject, value);

			return value;
		} finally {

			// ASSURE TO DESTROY THE CONTEXT
			Roma.context().destroy();
		}
	}

	@Override
	protected void setValueFinal(Object iObject, Object iValue) throws IllegalAccessException, InvocationTargetException {
		if (inheritedField != null)
			// DELEGATE TO INHERITED SETTER
			inheritedField.setValue(((VirtualObject) iObject).getSuperClassObject(), iValue);
		else
			((VirtualObject) iObject).getValues().put(name, iValue);
	}

	protected VirtualObject getVPojo(Object iObject) {
		if (iObject == null)
			return null;

		if (!(iObject instanceof VirtualObject))
			throw new IllegalArgumentException("Object " + iObject + " is not of type VirtualObject");

		return (VirtualObject) iObject;
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public Object getLanguageType() {
		return getType().getSchemaClass().getLanguageType();
	}

	@Override
	protected SchemaClass getSchemaClassFromLanguageType() {
		return getType().getSchemaClass();
	}

	@Override
	public SchemaClassDefinition getType() {
		return super.getType() != null ? super.getType() : inheritedField.getType();
	}
}
