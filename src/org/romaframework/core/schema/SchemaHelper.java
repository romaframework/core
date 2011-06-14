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

package org.romaframework.core.schema;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.lang.reflect.WildcardType;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.config.ContextException;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.entity.EntityHelper;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.exception.UserException;
import org.romaframework.core.factory.GenericFactory;
import org.romaframework.core.handler.RomaObjectHandler;
import org.romaframework.core.schema.reflection.SchemaClassReflection;
import org.romaframework.core.schema.reflection.SchemaFieldReflection;
import org.romaframework.core.schema.virtual.VirtualObject;

public class SchemaHelper {

	private static Log					log									= LogFactory.getLog(SchemaHelper.class);
	public static final Object	FAILED_EVENT_INVOKE	= new Object();

	/**
	 * Determine the embedded type of a field. If the field is annoted with USE_RUNTIME_TYPE=TRUE, then the embedded type will be
	 * determined using the first one object, if any,
	 * 
	 * @param iContent
	 *          Run-time object
	 * @param iField
	 *          Field to get the embedded type
	 * @return
	 * @throws ConfigurationException
	 */
	public static SchemaClass getEmbeddedType(Object iContent, SchemaField iField) throws ConfigurationException {
		if (iField.getFeature(CoreFieldFeatures.USE_RUNTIME_TYPE)) {
			Object value = SchemaHelper.getFieldValue(iField, iContent);
			if (value != null)
				if (SchemaHelper.isMultiValueObject(value)) {
					// MULTI VALUE OBJECT: GET THE CLASS OF THE FIRST ELEMENT
					Object[] items = SchemaHelper.getObjectArrayForMultiValueObject(value);

					if (items != null && items.length > 0)
						return Roma.schema().getSchemaClass(items[0]);
				} else
					// GET THE CLASS OF THE VALUE
					return Roma.schema().getSchemaClass(value);
		}

		// USE THE STANDARD WAYs
		return iField.getEmbeddedType();
	}

	/**
	 * Return the embedded type of a field. Use the ViewHelper if you're using the front-end since it makes some checks moreover.
	 * 
	 * @param iField
	 * @return
	 * @throws ConfigurationException
	 */
	public static Type getEmbeddedType(SchemaField iField) throws ConfigurationException {
		return iField.getEmbeddedLanguageType();
	}

	/**
	 * Get the field name by a field path.
	 * 
	 * @param iClassDef
	 * @param iFieldName
	 * @return SchemaField instance if found
	 * @throws BindingException
	 */
	public static SchemaField getFieldName(SchemaClassDefinition iClassDef, String iFieldName) throws BindingException {
		int sepPos = iFieldName.indexOf(Utility.PACKAGE_SEPARATOR);

		if (sepPos == -1) {
			return iClassDef.getField(iFieldName);
		}

		String fieldName = iFieldName.substring(0, sepPos);

		SchemaField field = iClassDef.getField(fieldName);

		return getFieldName(field.getType(), iFieldName.substring(sepPos + 1));
	}

	public static Object getFieldValue(Object iInstance, String iFieldName) throws BindingException {
		return getFieldValue(Roma.schema().getSchemaClass(iInstance), iFieldName, iInstance);
	}

	/**
	 * Get the field value reading a field path.
	 * 
	 * @param iClassDef
	 * @param iFieldName
	 * @param iInstance
	 * @param iValue
	 * @throws BindingException
	 */
	public static Object getFieldValue(SchemaClassDefinition iClassDef, String iFieldName, Object iInstance) throws BindingException {
		int sepPos = iFieldName.indexOf(Utility.PACKAGE_SEPARATOR);

		if (sepPos == -1) {
			SchemaField field = iClassDef.getField(iFieldName);

			if (field == null) {
				throw new UserException(iInstance, "Cannot find field or getter method '" + iFieldName + "' in object of class " + iInstance.getClass().getName());
			}

			return getFieldValue(field, iInstance);
		}

		String fieldName = iFieldName.substring(0, sepPos);

		SchemaField field = iClassDef.getField(fieldName);

		Object embeddedObject = getFieldValue(field, iInstance);

		if (embeddedObject == null) {
			return null;
		}

		return getFieldValue(field.getType(), iFieldName.substring(sepPos + 1), embeddedObject);
	}

	public static Class<?> getFieldType(SchemaField iField, Object iInstance) {
		Class<?> type = (Class<?>) iField.getLanguageType();
		if (iInstance != null && iField.getFeature(CoreFieldFeatures.USE_RUNTIME_TYPE)) {
			Object runtimeType = SchemaHelper.getFieldValue(iField, iInstance);
			if (runtimeType != null) {
				type = runtimeType.getClass();
			}
		}
		return type;

	}

	public static SchemaClassDefinition getFieldDefinition(SchemaField iField, Object iInstance) {
		if (iInstance != null && iField.getFeature(CoreFieldFeatures.USE_RUNTIME_TYPE)) {
			Object runtimeType = SchemaHelper.getFieldValue(iField, iInstance);
			if (runtimeType != null) {
				return Roma.schema().getSchemaClass(runtimeType);
			}
		}
		return iField.getType();
	}

	public static Object getFieldValue(SchemaField iField, Object iInstance) throws BindingException {
		if (iInstance == null || iField == null)
			return null;

		while (iInstance instanceof VirtualObject && iField instanceof SchemaFieldReflection)
			iInstance = ((VirtualObject) iInstance).getSuperClassObject();

		return iField.getValue(iInstance);
	}

	public static void setFieldValue(Object iInstance, String iFieldName, Object iValue) throws BindingException {
		setFieldValue(Roma.schema().getSchemaClass(iInstance.getClass()), iFieldName, iInstance, iValue);
	}

	/**
	 * Set the field value reading a field path.
	 * 
	 * @param iClassDef
	 * @param iFieldName
	 * @param iInstance
	 * @param iValue
	 * @throws BindingException
	 */
	public static void setFieldValue(SchemaClassDefinition iClassDef, String iFieldName, Object iInstance, Object iValue) throws BindingException {
		int sepPos = iFieldName.indexOf(Utility.PACKAGE_SEPARATOR);

		if (sepPos == -1) {
			setFieldValue(iClassDef.getField(iFieldName), iInstance, iValue);
			return;
		}

		String fieldName = iFieldName.substring(0, sepPos);

		SchemaField field = iClassDef.getField(fieldName);

		Object embeddedObject = getFieldValue(field, iInstance);

		setFieldValue(field.getType(), iFieldName.substring(sepPos + 1), embeddedObject, iValue);
	}

	public static void setFieldValue(SchemaField iField, Object iInstance, Object iFieldValue) throws BindingException {
		if (iInstance == null || iField == null)
			return;

		while (iInstance instanceof VirtualObject && iField instanceof SchemaFieldReflection)
			iInstance = ((VirtualObject) iInstance).getSuperClassObject();

		iField.setValue(iInstance, iFieldValue);
	}

	public static Object assignDefaultValueToLiteral(SchemaClass type) {
		Object value;
		if (type.isOfType(Integer.TYPE)) {
			value = Integer.valueOf(0);
		} else if (type.isOfType(Long.TYPE)) {
			value = Long.valueOf(0);
		} else if (type.isOfType(Short.TYPE)) {
			value = Short.valueOf((short) 0);
		} else if (type.isOfType(Byte.TYPE)) {
			value = Byte.valueOf((byte) 0);
		} else if (type.isOfType(Float.TYPE)) {
			value = Float.valueOf(0);
		} else if (type.isOfType(Double.TYPE)) {
			value = Double.valueOf(0);
		} else {
			value = null;
		}
		return value;
	}

	public static Object assignValueToLiteral(String v, Class<?> type) {
		Object value;
		if (v.length() == 0) {
			value = null;
		} else {
			if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
				value = Integer.parseInt(v);
			} else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
				value = Long.parseLong(v);
			} else if (type.equals(Short.class) || type.equals(Short.TYPE)) {
				value = Short.parseShort(v);
			} else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
				value = Boolean.parseBoolean(v);
			} else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
				value = Float.parseFloat(v);
			} else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
				value = Double.parseDouble(v);
			} else if (type.equals(Byte.class) || type.equals(Byte.TYPE)) {
				value = Byte.parseByte(v);
			} else if (type.equals(Character.class) || type.equals(Character.TYPE)) {
				value = v.charAt(0);
			} else {
				value = v;
			}
		}
		return value;
	}

	public static Field getField(Class<?> iClass, String iFieldName) {
		Field field = null;
		try {
			field = iClass.getDeclaredField(iFieldName);
		} catch (Exception e) {
			// NOT FOUND: TRY TO SEARCH RECURSIVELY IN TO INHERITANCE TREE
			Class<?> superClass = iClass.getSuperclass();
			if (superClass != null) {
				field = getField(superClass, iFieldName);
			}
		}
		return field;
	}

	/**
	 * Get all fields of a class. This method differs from Class.getFields() since it returns also non public fields.
	 * 
	 * @param iClass
	 *          Class<?> to introspect
	 * @return Fields array
	 */
	public static Field[] getFields(Class<?> iClass) {
		HashMap<String, Field> fieldSum = new HashMap<String, Field>();
		Class<?> currentClass = iClass;
		Field[] fields;

		// BROWSE CURRENT CLASS AND GO UP
		while (currentClass != null && currentClass != Object.class) {
			fields = currentClass.getDeclaredFields();
			for (int i = 0; i < fields.length; ++i) {
				if (!fieldSum.containsKey(fields[i].getName())) {
					// IF NOT PRESENT INSERT IT
					fieldSum.put(fields[i].getName(), fields[i]);
				}
			}
			currentClass = currentClass.getSuperclass();
		}

		Field[] result = new Field[fieldSum.size()];
		fieldSum.values().toArray(result);
		return result;
	}

	/**
	 * Get all methods of a class. This method differs from Class.getMethods() since it reads the class and go up. if a method is
	 * declared also in a base class will not be inserted, since the presumption is that the lower-defined method is more relevant
	 * than higher one.
	 * 
	 * @param iClass
	 *          Class<?> to introspect
	 * @return Methods array
	 */
	public static List<Method> getMethods(Class<?> iClass) {
		List<Method> methodSum = new ArrayList<Method>();
		Class<?> currentClass = iClass;
		Method[] methods;
		// BROWSE CURRENT CLASS AND GO UP

		while (currentClass != null && currentClass != Object.class) {
			methods = currentClass.getDeclaredMethods();
			for (int i = 0; i < methods.length; ++i) {
				if (!methods[i].isBridge())
					methodSum.add(0, methods[i]);
			}

			currentClass = currentClass.getSuperclass();
		}
		return methodSum;
	}

	public static Object invokeEvent(RomaObjectHandler iComponent, String eventName) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		// BROWSE UP UNTIL ROOT CONTENT COMPONENT SEARCHING THE EVENT
		SchemaObject currentSchemaInstance = iComponent.getSchemaObject();
		RomaObjectHandler currentComponent = iComponent;
		SchemaEvent event = null;
		Object currentContent;

		Object lastGoodContent = null;
		SchemaEvent lastGoodEvent = null;

		while (currentComponent != null) {
			event = currentSchemaInstance.getEvent(eventName);

			currentContent = currentComponent.getContent();

			if (event != null) {
				lastGoodEvent = event;
				lastGoodContent = currentContent;
			}

			if (currentComponent.getContainerComponent() != null) {
				currentComponent = currentComponent.getContainerComponent();
				if (!(currentComponent.getContent() instanceof ComposedEntity<?>)) {
					break;
				}
			} else {
				break;
			}

			currentSchemaInstance = currentComponent.getSchemaObject();
		}

		// INVOKE THE FIELD EVENT IF ANY
		if (lastGoodEvent != null) {
			return lastGoodEvent.invoke(lastGoodContent);
		}
		return FAILED_EVENT_INVOKE;
	}

	public static Collection<SchemaEvent> getEvents(Object iObject, String iFieldName) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {

		// BROWSE UP UNTIL ROOT CONTENT COMPONENT SEARCHING THE EVENT

		Object currentContent = iObject;
		Collection<SchemaEvent> result = new ArrayList<SchemaEvent>();

		SchemaClass cls;

		while (currentContent != null) {
			cls = Roma.schema().getSchemaClass(currentContent.getClass());

			Collection<SchemaEvent> events = cls.getEvents();
			result.addAll(events);

			if (!(currentContent instanceof ComposedEntity<?>)) {
				break;
			}

			currentContent = ((ComposedEntity<?>) currentContent).getEntity();
		}

		return result;

	}

	public static Object invokeEvent(Object iObject, String eventName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {

		// BROWSE UP UNTIL ROOT CONTENT COMPONENT SEARCHING THE EVENT
		SchemaEvent event = null;
		Object currentContent = iObject;

		Object lastGoodContent = null;
		SchemaEvent lastGoodEvent = null;

		SchemaClass cls;

		while (currentContent != null) {
			cls = Roma.schema().getSchemaClass(currentContent);

			event = cls.getEvent(eventName);
			if (event != null) {
				lastGoodEvent = event;
				lastGoodContent = currentContent;
				// FOUND: BREAK SEARCH
				break;
			}

			if (!(currentContent instanceof ComposedEntity<?>)) {
				break;
			}

			currentContent = ((ComposedEntity<?>) currentContent).getEntity();
		}

		// INVOKE THE FIELD EVENT IF ANY
		if (lastGoodEvent != null) {
			return lastGoodEvent.invoke(lastGoodContent);
		}
		return FAILED_EVENT_INVOKE;
	}

	public static Object invokeEvent(RomaObjectHandler romaObjectHandler, String fieldName, String eventName, Object... params) throws IllegalAccessException,
			InvocationTargetException {

		SchemaClass cls = romaObjectHandler.getSchemaObject().getSchemaClass();
		SchemaField field = cls.getField(fieldName);
		if (field != null) {

			if (eventName == null) {
				eventName = SchemaEvent.DEFAULT_EVENT_NAME;
			}
			SchemaEvent event = field.getEvent(eventName);
			if (event == null)
				return FAILED_EVENT_INVOKE;
			Object content = romaObjectHandler.getContent();

			while (!cls.isAssignableAs(event.getEventOwner().getSchemaClass())) {
				if (romaObjectHandler.getContainerComponent() != null) {
					romaObjectHandler = romaObjectHandler.getContainerComponent();
					cls = romaObjectHandler.getSchemaObject().getSchemaClass();
					content = romaObjectHandler.getContent();
				} else
					return FAILED_EVENT_INVOKE;
			}
			if (content != null) {
				return event.invoke(content, params);
			}
		}
		return FAILED_EVENT_INVOKE;
	}

	public static Object invokeEvent(Object object, String fieldName, String eventName, Object... params) throws IllegalAccessException,
			InvocationTargetException {
		if (object != null) {
			SchemaClass cls = Roma.schema().getSchemaClass(object.getClass());
			SchemaField field = cls.getField(fieldName);
			if (field != null) {

				if (eventName == null) {
					eventName = SchemaEvent.DEFAULT_EVENT_NAME;
				}
				SchemaEvent event = field.getEvent(eventName);
				if (event != null) {
					return event.invoke(object, params);
				}
			}
		}
		return FAILED_EVENT_INVOKE;
	}

	/**
	 * Check if a schemaClass is assignable to a specified class
	 * 
	 * @param schemaClass
	 *          to check
	 * @param dest
	 *          destination type.
	 * @return true if assignable otherwise false.
	 */
	public static boolean isAssignableAs(SchemaClassDefinition schemaClass, Class<?> dest) {
		return schemaClass.getSchemaClass().isAssignableAs(Roma.schema().getSchemaClass(dest));
	}

	public static void insertElements(SchemaField iField, Object iContent, Object[] iSelection) {
		insertElements(iField, iContent, iSelection, false);
	}

	public static void insertElements(SchemaField iField, Object iContent, Object[] iSelection, boolean overrideContent) {
		if (iField == null) {
			log.warn("[SchemaHelper.insertElements] Field is null");
			return;
		}
		if (iContent == null) {
			log.warn("[SchemaHelper.insertElements] target object is null. Cannot to value field " + iField);
			return;
		}
		boolean simpleSet = false;
		if (iSelection != null && iSelection.length > 0 && iSelection[0] != null)
			simpleSet = Roma.schema().getSchemaClass(iSelection[0]).equals(iField.getType().getSchemaClass());
		// TODO: REVIEW BIND MODE WITH NESTED FIELDS EXPRESSION
		Object currentValue = SchemaHelper.getFieldValue(iField, iContent);

		if (currentValue == null && SchemaHelper.isMultiValueObject(iField) && !simpleSet)
			// CHECK IF IT'S A COLLECTION: IN THIS CASE THROW AN EXCEPTION SINCE
			// IT MUST BE INITIALIZED BEFORE TO USE IT
			if (isAssignableAs(iField.getType(), Collection.class))
				throw new IllegalArgumentException("The collection in field '" + iField.getEntity().getSchemaClass().getName() + "." + iField.getName()
						+ "' is null: cannot add elements. Remember to initialize it.");
			else if (isAssignableAs(iField.getType(), Map.class))
				throw new IllegalArgumentException("The map in field '" + iField.getEntity().getSchemaClass().getName() + "." + iField.getName()
						+ "' is null: cannot add elements. Remember to initialize it.");

		if (currentValue instanceof Collection<?> && !simpleSet) {
			// INSERT EACH ELEMENT OF SELECTION IN THE COLLECTION
			Collection<Object> coll = (Collection<Object>) currentValue;
			if (overrideContent)
				coll.clear();
			if (iSelection != null) {
				for (Object o : iSelection) {
					coll.add(EntityHelper.getEntityObjectIfNeeded(o, iField.getEmbeddedType()));
				}
			}
		} else if (currentValue instanceof Map && !simpleSet) {
			// INSERT EACH ELEMENT OF SELECTION IN THE MAP (KEY = SELECTION
			// OBJ.toString()
			if (overrideContent)
				((Map<String, Object>) currentValue).clear();
			if (iSelection != null) {
				for (Object o : iSelection) {
					((Map<String, Object>) currentValue).put(EntityHelper.getEntityObject(o).toString(), EntityHelper.getEntityObject(o));
				}
			}
		} else if (((Class<?>) iField.getLanguageType()).isArray() && !simpleSet) {
			Object array = null;
			if (iField.getLanguageType().equals(Object[].class)) {
				// OBJECT[]: NO CONVERSION REQUIRED
				if (overrideContent) {
					array = iSelection;
				} else {
					Object[] oldValue = (Object[]) SchemaHelper.getFieldValue(iField, iContent);
					int oldLength = 0;
					if (oldValue != null)
						oldLength = oldValue.length;
					array = new Object[oldLength + iSelection.length];
					if (oldValue != null)
						System.arraycopy(oldValue, 0, array, 0, oldLength);
					System.arraycopy(iSelection, 0, array, oldLength, iSelection.length);
				}
			} else if (iSelection != null) {
				// COPY THE ARRAY TO USE REAL CLASS ARRAY, IF ANY
				int i = 0;
				Object oldValue = SchemaHelper.getFieldValue(iField, iContent);
				if (overrideContent || oldValue == null) {
					array = Array.newInstance(((Class<?>) iField.getLanguageType()).getComponentType(), iSelection.length);
				} else {
					int oldSize = Array.getLength(oldValue);
					array = Array.newInstance(((Class<?>) iField.getLanguageType()).getComponentType(), iSelection.length + oldSize);
					for (; i < oldSize; ++i) {
						Array.set(array, i, Array.get(oldValue, i));
					}
				}

				for (int sourcePos = 0; sourcePos < iSelection.length; ++sourcePos, ++i) {
					Array.set(array, i, iSelection[sourcePos]);
				}
			}
			SchemaHelper.setFieldValue(iField, iContent, array);
		} else {
			Object firstSelection = iSelection != null && iSelection.length > 0 ? iSelection[0] : null;
			SchemaHelper.setFieldValue(iField, iContent, EntityHelper.getEntityObjectIfNeeded(firstSelection, iField.getType()));
		}

		// REFRESH THE FIELD
		Roma.fieldChanged(iContent, iField.getName());
	}

	public static Object removeElements(Object iContent, Object[] iSelection) {
		if (iContent == null) {
			return null;
		}

		Object result = null;

		if (iContent instanceof Collection<?>) {
			Collection<?> coll = (Collection<?>) iContent;
			for (int i = 0; i < iSelection.length; ++i) {
				coll.remove(iSelection[i]);
			}
		} else if (iContent instanceof Map<?, ?>) {
			Map<?, ?> map = (Map<?, ?>) iContent;
			Object toRemove;
			for (int i = 0; i < iSelection.length; ++i) {
				toRemove = iSelection[i] instanceof Map.Entry<?, ?> ? ((Map.Entry<?, ?>) iSelection[i]).getValue() : iSelection[i];
				map.values().remove(toRemove);
			}
		} else if (iContent instanceof Set<?>) {
			Set<?> set = (Set<?>) iContent;
			for (int i = 0; i < iSelection.length; ++i) {
				set.remove(iSelection[i]);
			}
		} else if (iContent.getClass().isArray()) {
			Object[] array = (Object[]) iContent;
			List<Object> tempResult = new ArrayList<Object>(array.length);

			boolean found;
			for (int i = 0; i < array.length; ++i) {
				found = false;
				for (int sel = 0; sel < iSelection.length; ++sel) {
					if (array[i].equals(iSelection[sel])) {
						found = true;
						break;
					}
				}

				if (!found) {
					// OK, NOT TO BE REMOVED
					tempResult.add(array[i]);
				}
			}

			result = tempResult.toArray();
		}
		return result;
	}

	/**
	 * Move an object up or down in a List or array objects.
	 * 
	 * @param iContent
	 *          List or array object
	 * @param iSelection
	 *          The object to move.
	 * @param iDirection
	 *          use positive integer to move the item forward or negative for backward
	 * @return new element position
	 */
	public static int moveElement(Object iContent, Object iSelection, int iDirection) {
		if (iContent == null) {
			return -1;
		}

		int newPos = 1;
		if (iContent instanceof List<?>) {
			List<Object> list = (List<Object>) iContent;
			// SEARCH CURRENT POSITION
			int currPos = list.indexOf(iSelection);

			if (currPos > -1) {
				newPos = currPos + iDirection;
				if (newPos >= 0 && newPos < list.size()) {
					// OK, VALID RANGE: SWAP IT
					Object tmp = list.get(newPos);
					list.set(newPos, iSelection);
					list.set(currPos, tmp);
				}
			}
		} else if (iContent.getClass().isArray()) {
			Object[] array = (Object[]) iContent;

			// SEARCH CURRENT POSITION
			int currPos = -1;
			for (int i = 0; i < array.length; ++i) {
				if (array[i].equals(iSelection)) {
					currPos = i;
					break;
				}
			}

			newPos = currPos + iDirection;
			if (newPos >= 0 && newPos < array.length) {
				// OK, VALID RANGE: SWAP IT
				Object tmp = array[newPos];
				array[newPos] = iSelection;
				array[currPos] = tmp;
			}
		}
		return newPos;
	}

	/**
	 * Return the type of generic of superclass.
	 * 
	 * @param clazz
	 *          where search.
	 * @return the generic SchemaClass or null.
	 */
	public static SchemaClass getSuperclassGenericType(Class<?> clazz) {
		return getSuperclassGenericType(Roma.schema().getSchemaClass(clazz));
	}

	/**
	 * Return the type of generic of superclass.
	 * 
	 * @param schemaClassDefinition
	 *          where search.
	 * @return the generic SchemaClass or null.
	 */
	public static SchemaClass getSuperclassGenericType(SchemaClassDefinition schemaClassDefinition) {
		if (!(schemaClassDefinition.getSchemaClass() instanceof SchemaClassReflection))
			return null;

		SchemaClassReflection schemaClassReflection = (SchemaClassReflection) schemaClassDefinition.getSchemaClass();
		Type type = schemaClassReflection.getLanguageType();

		while (!(type instanceof ParameterizedType) && type != null) {
			if (type instanceof Class<?>) {
				type = ((Class<?>) type).getGenericSuperclass();
			} else
				return null;
		}
		if (type == null)
			return null;
		Type[] params = ((ParameterizedType) type).getActualTypeArguments();
		if (params == null || params.length == 0)
			return null;
		type = params[0];
		Class<?> cl = resolveClassFromType(type);
		if (cl == null)
			return null;
		return Roma.schema().getSchemaClass(cl);
	}

	public static SchemaClass getSuperInterfaceGenericType(Class<?> clazz) {
		return getSuperInterfaceGenericType((Type) clazz);
	}

	public static SchemaClass getSuperInterfaceGenericType(Type clazz) {
		Type type = clazz;
		if (type != null) {
			if (type instanceof ParameterizedType) {
				Type[] params = ((ParameterizedType) type).getActualTypeArguments();
				if (params == null || params.length == 0)
					return null;
				Class<?> cl = resolveClassFromType(params[0]);
				if (cl != null)
					return Roma.schema().getSchemaClass(cl);
			} else {
				Type[] implementedInterfaces = ((Class<?>) type).getGenericInterfaces();
				if (implementedInterfaces != null && implementedInterfaces.length > 0) {
					for (int i = 0; i < implementedInterfaces.length; i++) {
						SchemaClass genericType = getSuperInterfaceGenericType(implementedInterfaces[i]);
						if (genericType != null)
							return genericType;
					}
				} else
					return null;
			}
		}
		return null;
	}

	public static SchemaClass getSuperInterfaceGenericType(SchemaClassDefinition schemaClassDefinition) {
		if (!(schemaClassDefinition.getSchemaClass() instanceof SchemaClassReflection))
			return null;

		SchemaClassReflection schemaClassReflection = (SchemaClassReflection) schemaClassDefinition.getSchemaClass();
		return getSuperInterfaceGenericType(schemaClassReflection.getLanguageType());
	}

	/**
	 * Return the type of generic of superclass.
	 * 
	 * @param schemaClassDefinition
	 *          where search.
	 * @return the generic SchemaClass or null.
	 */
	public static List<SchemaClass> getSuperclassGenericTypes(SchemaClassDefinition schemaClassDefinition) {

		if (!(schemaClassDefinition.getSchemaClass() instanceof SchemaClassReflection))
			return null;

		SchemaClassReflection schemaClassReflection = (SchemaClassReflection) schemaClassDefinition.getSchemaClass();
		Type type = schemaClassReflection.getLanguageType();

		while (!(type instanceof ParameterizedType) && type != null) {
			if (type instanceof Class<?>) {
				type = ((Class<?>) type).getGenericSuperclass();
			} else
				return null;
		}
		if (type == null)
			return null;

		Type[] params = ((ParameterizedType) type).getActualTypeArguments();
		if (params == null || params.length == 0)
			return null;

		List<SchemaClass> result = new ArrayList<SchemaClass>();
		for (Type curType : params) {
			if (curType instanceof Class<?>)
				result.add(Roma.schema().getSchemaClass((Class<?>) curType));
		}
		return result;
	}

	/**
	 * Return the generic type if iType uses Java5+ Generics.
	 * 
	 * @param iType
	 *          Type with generics
	 * @return Generic Class<?> if any
	 */
	public static Class<?> getGenericClass(Type iType) {
		// CHECK IF IT'S A PARAMETIZERED TYPE
		if (!(iType instanceof ParameterizedType)) {
			return null;
		}

		Class<?> returnClass = null;

		// list the raw type information
		ParameterizedType ptype = (ParameterizedType) iType;
		Type rtype = ptype.getRawType();

		if (!(rtype instanceof Class<?>)) {
			// NO CLASS: RETURN NULL
			return null;
		}

		Type[] targs = ptype.getActualTypeArguments();

		if (targs == null || targs.length == 0) {
			return null;
		}

		Class<?> classType = (Class<?>) rtype;

		try {
			if (classType.isArray()) {
				returnClass = (Class<?>) targs[0];
			} else if (java.util.Collection.class.isAssignableFrom((Class<?>) rtype)) {
				returnClass = resolveClassFromType(targs[0]);
				if (returnClass == null)
					returnClass = Object.class;

			} else if (java.util.Map.class.isAssignableFrom((Class<?>) rtype)) {
				returnClass = classType.getDeclaredClasses()[0];
			} else if (targs[0] instanceof Class<?>) {
				return (Class<?>) targs[0];
			}
		} catch (ClassCastException e) {
			throw new ConfigurationException("Cannot determine embedded type for " + iType + ". Embedded class not found", e);
		}

		return returnClass;
	}

	/**
	 * Returns the object of field in expression.
	 * 
	 * @param iStartingObject
	 *          Starting object to navigate
	 * @param iExpression
	 *          Path of field
	 * @return
	 */
	public static Object getFieldObject(Object iStartingObject, String iExpression) {
		int lastSep = iExpression.lastIndexOf('.');
		if (lastSep == -1) {
			return iStartingObject;
		}

		String exp = iExpression.substring(0, lastSep);
		exp = exp.replace('.', '/');

		JXPathContext context = JXPathContext.newContext(iStartingObject);
		return context.getValue(exp);
	}

	public static boolean isMultiValueType(Type embType) {
		if (embType instanceof Class<?> && ((Class<?>) embType).isArray()) {
			return true;
		}

		if (embType instanceof GenericArrayType) {
			return true;
		}

		if (embType instanceof Class<?>) {
			Class<? extends Object> embClass = (Class<? extends Object>) embType;
			if (Array.class.isAssignableFrom(embClass)) {
				return true;
			}
			if (java.util.Collection.class.isAssignableFrom(embClass)) {
				return true;
			}
			if (java.util.Map.class.isAssignableFrom(embClass)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isMultiValueObject(Object iObject) {
		if (iObject == null)
			return false;

		if (iObject instanceof VirtualObject)
			return false;
		else
			return isMultiValueType((Type) iObject.getClass());
	}

	public static boolean isMultiValueType(SchemaClass schemaClass) {
		if (schemaClass == null)
			return false;
		Object languageType = schemaClass.getLanguageType();
		if (languageType == null)
			return false;
		return isMultiValueType((Type) languageType);
	}

	public static Object[] getObjectArrayForMultiValueObject(Object iObject) {
		if (iObject.getClass().isArray()) {
			if (iObject.getClass().getComponentType().isPrimitive()) {
				int oldSize = Array.getLength(iObject);
				Object[] result = new Object[oldSize];
				for (int i = 0; i < oldSize; ++i) {
					result[i] = Array.get(iObject, i);
				}
				return result;
			} else {
				return (Object[]) iObject;
			}
		}
		if (java.util.Collection.class.isAssignableFrom(iObject.getClass())) {
			return ((Collection<?>) iObject).toArray();
		}
		if (java.util.Map.class.isAssignableFrom(iObject.getClass())) {
			return ((Map<?, ?>) iObject).values().toArray();
		}
		return null;
	}

	public static int getSizeForMultiValueObject(Object iObject) {
		if (iObject.getClass().isArray())
			return ((Object[]) iObject).length;
		if (java.util.Collection.class.isAssignableFrom(iObject.getClass()))
			return ((Collection<?>) iObject).size();
		if (java.util.Map.class.isAssignableFrom(iObject.getClass()))
			return ((Map<?, ?>) iObject).size();
		return 0;
	}

	public static boolean isJavaType(String iEntityName) {
		return getClassForJavaTypes(iEntityName) != null;
	}

	public static boolean isJavaType(Class<?> iClass) {
		return getClassForJavaTypes(iClass.getSimpleName()) != null;
	}

	/**
	 * Resolve class object for java types.
	 * 
	 * @param iEntityName
	 *          Java type name
	 * @return Class object if found, otherwise null
	 */
	public static Class<?> getClassForJavaTypes(String iEntityName) {
		if (iEntityName.equals("String"))
			return String.class;
		else if (iEntityName.equals("Integer"))
			return Integer.class;
		else if (iEntityName.equals("int"))
			return Integer.TYPE;
		else if (iEntityName.equals("Long"))
			return Long.class;
		else if (iEntityName.equals("long"))
			return Long.TYPE;
		else if (iEntityName.equals("Short"))
			return Short.class;
		else if (iEntityName.equals("short"))
			return Short.TYPE;
		else if (iEntityName.equals("Boolean"))
			return Boolean.class;
		else if (iEntityName.equals("boolean"))
			return Boolean.TYPE;
		else if (iEntityName.equals("BigDecimal"))
			return BigDecimal.class;
		else if (iEntityName.equals("Float"))
			return Float.class;
		else if (iEntityName.equals("float"))
			return Float.TYPE;
		else if (iEntityName.equals("Double"))
			return Double.class;
		else if (iEntityName.equals("Number"))
			return Number.class;
		else if (iEntityName.equals("double"))
			return Double.TYPE;
		else if (iEntityName.equals("Character"))
			return Character.class;
		else if (iEntityName.equals("char"))
			return Character.TYPE;
		else if (iEntityName.equals("Byte"))
			return Byte.class;
		else if (iEntityName.equals("byte"))
			return Byte.TYPE;
		else if (iEntityName.equals("Object"))
			return Object.class;
		else if (iEntityName.equals("Collection"))
			return Collection.class;
		else if (iEntityName.equals("List"))
			return List.class;
		else if (iEntityName.equals("Set"))
			return Set.class;
		else if (iEntityName.equals("Map"))
			return Map.class;
		else if (iEntityName.equals("Date"))
			return Date.class;
		else if (iEntityName.equals("Map$Entry"))
			return Map.Entry.class;
		else if (iEntityName.equals("HashMap$Entry"))
			return Map.Entry.class;
		else if (iEntityName.equals("LinkedHashMap$Entry"))
			return Map.Entry.class;
		return null;
	}

	/**
	 * Return the SchemaEvent requested.
	 * 
	 * @param iClassName
	 *          Name of the class
	 * @param iEventName
	 *          Name of the event to search
	 * @return SchemaEvent object if found, otherwise null
	 */
	public static SchemaEvent getSchemaEvent(String iClassName, String iEventName) {
		SchemaClass cls = Roma.schema().getSchemaClass(iClassName);
		if (cls == null)
			return null;

		return cls.getEvent(iEventName);
	}

	/**
	 * Return the SchemaAction requested.
	 * 
	 * @param iClassName
	 *          Name of the class
	 * @param iActionName
	 *          Name of the action to search
	 * @return SchemaAction object if found, otherwise null
	 */
	public static SchemaClassElement getSchemaAction(String iClassName, String iActionName) {
		SchemaClass cls = Roma.schema().getSchemaClass(iClassName);
		if (cls == null)
			return null;

		return cls.getAction(iActionName);
	}

	/**
	 * Return the SchemaField requested.
	 * 
	 * @param iClassName
	 *          Name of the class
	 * @param iFieldName
	 *          Name of the field to search
	 * @return SchemaField object if found, otherwise null
	 */
	public static SchemaField getSchemaField(String iClassName, String iFieldName) {
		SchemaClass cls = Roma.schema().getSchemaClass(iClassName);
		if (cls == null)
			return null;

		return cls.getField(iFieldName);
	}

	/**
	 * Create a new Object using the factory if any otherwise by SchemaClass's object construction.
	 * 
	 * @param iClass
	 *          SchemaClass instance
	 * @param iArgs
	 *          Optional var args
	 * @return The new object created
	 */
	public static Object createObject(SchemaClass iClass, Object... iArgs) throws IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException, SecurityException, NoSuchMethodException {
		Object newInstance = null;

		try {
			// TRY TO USE THE FACTORY IF ANY
			GenericFactory<?> factory = Roma.component(iClass.getName() + GenericFactory.DEF_SUFFIX);

			if (iArgs == null || iArgs.length == 0)
				// INVOKE THE EMPTY CONSTRUCTOR
				newInstance = factory.create();
			else
				newInstance = factory.create(iArgs);
		} catch (ContextException ex) {
			newInstance = iClass.newInstance(iArgs);
		}
		return newInstance;
	}

	public static boolean isAssignableAs(SchemaClass iRootClass, SchemaClass iClassInterface) {
		if (iRootClass.equals(iClassInterface))
			return true;

		if (implementsInterface(iRootClass, iClassInterface))
			return true;

		SchemaClass cls = iRootClass.getSuperClass();
		while (cls != null) {
			if (cls.equals(iClassInterface))
				return true;

			if (implementsInterface(cls, iClassInterface))
				return true;

			cls = cls.getSuperClass();
		}
		return false;
	}

	public static boolean extendsClass(SchemaClass iRootClass, SchemaClass iClass) {
		SchemaClass cls = iRootClass;
		while (cls != null) {
			if (cls.equals(iClass))
				return true;

			cls = cls.getSuperClass();
		}
		return false;
	}

	public static boolean implementsInterface(SchemaClass iRootClass, SchemaClass iInterface) {
		if (iRootClass.equals(iInterface))
			return true;

		if (iRootClass.getImplementedInterfaces() != null)
			for (SchemaClass ifc : iRootClass.getImplementedInterfaces()) {
				if (implementsInterface(ifc, iInterface))
					return true;
			}

		return false;
	}

	public static boolean isMultiValueObject(SchemaField iSchemaField) {
		if (iSchemaField == null)
			return false;

		return isMultiValueType((Type) iSchemaField.getLanguageType());
	}

	public static Object invokeAction(Object target, String action, Object... params) throws IllegalArgumentException, IllegalAccessException,
			InvocationTargetException {
		SchemaClass schemaClass = Roma.schema().getSchemaClass(target);

		SchemaAction schemaAction = schemaClass.getAction(action, params);

		if (schemaAction == null)
			throw new IllegalArgumentException("Action " + schemaClass.getName() + "." + action + "(" + Utility.array2String(params) + ") was not found");

		return schemaAction.invoke(target, params);
	}

	public static Class<?> resolveClassFromType(Type type) {
		if (type instanceof Class<?>)
			return (Class<?>) type;
		if (type instanceof ParameterizedType) {
			return resolveClassFromType(((ParameterizedType) type).getRawType());
		}
		if (type instanceof GenericArrayType) {
			GenericArrayType gat = (GenericArrayType) type;
			Class<?> arrItemp = resolveClassFromType(gat.getGenericComponentType());
			return Array.newInstance(arrItemp, 0).getClass();
		}
		if (type instanceof TypeVariable<?>) {
			TypeVariable<?> t = (TypeVariable<?>) type;
			Type[] bounds = t.getBounds();
			if (bounds.length == 1)
				return resolveClassFromType(bounds[0]);
		}
		if(type instanceof  WildcardType){
			//TODO:
		}
		return null;
	}

}
