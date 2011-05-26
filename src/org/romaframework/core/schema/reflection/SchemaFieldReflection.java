package org.romaframework.core.schema.reflection;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.UserObjectEventListener;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.config.EmbeddedSchemaConfiguration;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

public class SchemaFieldReflection extends SchemaField {

	private static final long	serialVersionUID	= 8401682154724053320L;

	protected Field						field;
	protected Class<?>				languageType;
	protected Method					getterMethod;
	protected Method					setterMethod;
	private static Log				log								= LogFactory.getLog(SchemaFieldReflection.class);

	public SchemaFieldReflection(SchemaClassDefinition entity, String iName) {
		super(entity, iName);
	}

	@Override
	public boolean isArray() {
		return languageType.isArray();
	}

	public void configure(SchemaClass iFieldType, Class<?> iLanguageFieldType, Field iField, Method iGetterMethod,
			Method iSetterMethod) {
		languageType = iLanguageFieldType;

		if (iFieldType == null)
			iFieldType = Roma.schema().getSchemaClassIfExist(languageType);

		field = iField;

		if (iGetterMethod != null)
			getterMethod = iGetterMethod;

		if (iSetterMethod != null)
			setterMethod = iSetterMethod;

		String annotationName;
		Annotation fieldAnnotation;
		Annotation genericAnnotation;
		Annotation getterAnnotation;

		SchemaConfiguration classDescriptor = entity.getSchemaClass().getDescriptor();

		XmlFieldAnnotation parentDescriptor = null;

		if (classDescriptor != null && classDescriptor.getType() != null && classDescriptor.getType().getFields() != null) {
			// SEARCH FORM DEFINITION IN DESCRIPTOR
			parentDescriptor = classDescriptor.getType().getField(name);
		}

		

		if (parentDescriptor != null && parentDescriptor.getClassAnnotation() != null) {
			// CONFIGURE EMBEDDED CLASS IF ANY
			XmlClassAnnotation subClass = parentDescriptor.getClassAnnotation();
			SchemaConfiguration fieldSchemaDescr = null;
			// INLINE DESCRIPTOR
			SchemaConfiguration sourceDescr = getEntity().getSchemaClass().getDescriptor();
			if (sourceDescr != null) {
				fieldSchemaDescr = new EmbeddedSchemaConfiguration(subClass);

				SchemaClass baseType = (SchemaClass) (Roma.schema().getSchemaClass(languageType));

				// CREATE A NEW INLINE CLASS
				setType(Roma.schema().createSchemaClass(getEntity().getSchemaClass().getName() + "." + getName(), baseType,
						fieldSchemaDescr));
			} else {
				setType(Roma.schema().getSchemaClassIfExist(getLanguageType()));
			}

		} else
			type = iFieldType;

		if (type == null)
			setType(Roma.schema().getSchemaClassIfExist(getLanguageType()));

		// BROWSE ALL ASPECTS
		for (Aspect aspect : Roma.aspects()) {
			// COMPOSE ANNOTATION NAME BY ASPECT
			annotationName = aspect.aspectName();
			annotationName = Character.toUpperCase(annotationName.charAt(0)) + annotationName.substring(1);

			if (field != null) {
				fieldAnnotation = searchForAnnotation(field, annotationName + "Field", aspect.aspectName());
				genericAnnotation = searchForAnnotation(field, annotationName, aspect.aspectName());
			} else {
				fieldAnnotation = null;
				genericAnnotation = null;
			}

			if (getterMethod != null)
				getterAnnotation = searchForAnnotation(getterMethod, annotationName + "Field", aspect.aspectName());
			else
				getterAnnotation = null;

			// CONFIGURE THE SCHEMA OBJECT WITH CURRENT ASPECT
			aspect.configField(this, fieldAnnotation, genericAnnotation, getterAnnotation, parentDescriptor);
		}
		
		// DISCOVER EMBEDDED TYPE
		Type embType;
		if ((embType = assignEmbeddedType(getterMethod.getGenericReturnType())) == null)
			if ((embType = assignEmbeddedType(getterMethod.getReturnType())) == null)
				if (field != null)
					embType = assignEmbeddedType(field.getGenericType());

		if (embType != null && embType instanceof ParameterizedType) {
			ParameterizedType pt = (ParameterizedType) embType;
			if (pt.getActualTypeArguments() != null) {
				embeddedTypeGenerics = new SchemaClass[pt.getActualTypeArguments().length];
				int i = 0;
				for (Type argType : pt.getActualTypeArguments())
					if (argType instanceof Class<?>)
						embeddedTypeGenerics[i++] = Roma.schema().getSchemaClassIfExist((Class<?>) argType);
			}
		}

		if (parentDescriptor != null && parentDescriptor.getEvents() != null) {
			Set<XmlEventAnnotation> events = parentDescriptor.getEvents();
			for (XmlEventAnnotation xmlConfigEventType : events) {

				SchemaEventReflection eventInfo = (SchemaEventReflection) getEvent(xmlConfigEventType.getName());

				if (eventInfo == null) {
					// EVENT NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
					eventInfo = new SchemaEventReflection(this, xmlConfigEventType.getName(), null);
					setEvent(xmlConfigEventType.getName(), eventInfo);
				}
				eventInfo.configure(null);
			}
		}
	}

	@Override
	public Object getValue(Object iObject) throws BindingException {
		if (iObject == null)
			return null;

		List<UserObjectEventListener> listeners = Controller.getInstance().getListeners(UserObjectEventListener.class);

		try {
			// CREATE THE CONTEXT BEFORE TO CALL THE ACTION
			Roma.context().create();

			// CALL ALL LISTENERS BEFORE TO RETURN THE VALUE
			Object value = invokeCallbackBeforeFieldRead(listeners, iObject);

			if (value != UserObjectEventListener.IGNORED)
				return value;

			// TRY TO INVOKE GETTER IF ANY
			if (getterMethod != null) {
				try {
					value = getterMethod.invoke(iObject, (Object[]) null);
				} catch (IllegalArgumentException e) {
					// PROBABLY AN ISSUE OF CLASS VERSION
					throw new BindingException(iObject, name,
							"Error on using different classes together. Maybe it's an issue due to class reloading. Assure to use objects of the same class.");
				} catch (InvocationTargetException e) {
					Throwable nested = e.getCause();
					if (nested == null) {
						nested = e.getTargetException();
					}

					// BYPASS REFLECTION EXCEPTION: THROW REAL NESTED EXCEPTION
					throw new BindingException(iObject, name, nested);
				}
			} else {
				// READ FROM FIELD
				if (!field.isAccessible()) {
					field.setAccessible(true);
				}

				value = field.get(iObject);
			}

			// CALL ALL LISTENERS BEFORE TO RETURN THE VALUE
			value = invokeCallbackAfterFieldRead(listeners, iObject, value);

			return value;

		} catch (Exception e) {
			throw new BindingException(iObject, name, e);
		} finally {

			// ASSURE TO DESTROY THE CONTEXT
			Roma.context().destroy();
		}
	}

	protected void setValueFinal(Object iObject, Object iValue) throws IllegalAccessException, InvocationTargetException {
		// TRY TO INVOKE SETTER IF ANY
		if (setterMethod != null) {
			setterMethod.invoke(iObject, new Object[] { iValue });
		} else {
			// WRITE THE FIELD
			if (field == null) {
				log.debug("[SchemaHelper.setFieldValue] Cannot set the value '" + iValue + "' for field '" + name + "' on object "
						+ iObject + " since it has neither setter neir field declared");
				return;
			}

			if (!field.isAccessible()) {
				field.setAccessible(true);
			}

			field.set(iObject, iValue);
		}
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SchemaFieldReflection copy = (SchemaFieldReflection) super.clone();
		copy.languageType = languageType;
		copy.field = field;
		copy.getterMethod = getterMethod;
		copy.setterMethod = setterMethod;
		return copy;
	}

	@Override
	public String toString() {
		return name + " (field:" + field + ")";
	}

	public Field getField() {
		return field;
	}

	public Method getGetterMethod() {
		return getterMethod;
	}

	public Method getSetterMethod() {
		return setterMethod;
	}

	public Class<?> getLanguageType() {
		return languageType;
	}

	public void setLanguageType(Class<?> clazz) {
		languageType = clazz;
	}

	@Override
	protected SchemaClass getSchemaClassFromLanguageType() {
		return Roma.schema().getSchemaClass(languageType);
	}

	/**
	 * Get embedded type using Generics Reflection.
	 * 
	 * @param iType
	 *          Type to check
	 * @return true if an embedded type was found, otherwise false
	 */
	private Type assignEmbeddedType(Type iType) {
		// CHECK FOR ARRAY
		if (iType instanceof Class<?>) {
			Class<?> cls = (Class<?>) iType;
			if (cls.isArray())
				setEmbeddedLanguageType(cls.getComponentType());
		}

		Class<?> embClass = SchemaHelper.getGenericClass(iType);
		if (embClass != null) {
			setEmbeddedLanguageType(embClass);
		}

		return embClass != null ? iType : null;
	}
}
