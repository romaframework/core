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

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.CoreAspect;
import org.romaframework.aspect.core.feature.CoreClassFeatures;
import org.romaframework.core.GlobalConstants;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.exception.ConfigurationNotFoundException;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaConfigurationLoader;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaManager;
import org.romaframework.core.schema.SchemaParameter;
import org.romaframework.core.schema.SchemaReloader;
import org.romaframework.core.schema.config.SaxSchemaConfiguration;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;

/**
 * Represent a class. It's not necessary that a Java class exist in the Classpath since you can define a SchemaClassReflection that
 * inherits another Java Class and use XML descriptor to customize it. This feature avoid the writing of empty class that simply
 * inherit real domain class.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaClassReflection extends SchemaClass {

	private Class<?>							javaClass;
	private SchemaClass						baseClass;

	public static final String		GET_METHOD					= "get";
	public static final String		IS_METHOD						= "is";
	public static final String		SET_METHOD					= "set";
	public static final String[]	IGNORE_METHOD_NAMES	= { "equals", "toString", "hashCode", "validate", "getClass", "on^*", "clone" };

	private static Log						log									= LogFactory.getLog(SchemaClassReflection.class);

	public SchemaClassReflection(Class<?> iClass) {
		super(Utility.getClassName(iClass));
		javaClass = iClass;
		// USED CLASS EQUALS FOR CROSS CLASSLOADER COMPARE
		baseClass = iClass.getSuperclass() != null && iClass.getSuperclass().equals(Object.class) ? Roma.schema().getSchemaClass(
				iClass.getSuperclass()) : null;
		config();
	}

	public SchemaClassReflection(String iEntityName, Class<?> iClass, SchemaClass iBaseClass, SchemaConfiguration iDescriptor)
			throws ConfigurationNotFoundException {
		super(iEntityName);

		if (iDescriptor == null)
			iDescriptor = Roma.component(SchemaConfigurationLoader.class).getSaxSchemaConfiguration(iEntityName);

		if (iClass == null && iBaseClass == null)
			// ERROR: CANNOT ASSOCIATE A JAVA CLASS
			throw new ConfigurationNotFoundException("Class " + iEntityName);

		javaClass = iClass;
		baseClass = iBaseClass;

		if (iDescriptor != null) {
			descriptor = iDescriptor;
			if (descriptor instanceof SaxSchemaConfiguration)
				Roma.component(SchemaReloader.class).addResourceForReloading(((SaxSchemaConfiguration) descriptor).getFile(), iEntityName);
		}

		File classFile = Roma.component(SchemaClassResolver.class).getFileOwner(name + SchemaClassResolver.CLASS_SUFFIX);

		if (classFile != null)
			Roma.component(SchemaReloader.class).addResourceForReloading(classFile, iEntityName);
	}

	@Override
	public Object newInstanceFinal(Object... iArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		Class<?> currClass = (Class<?>) (javaClass != null ? javaClass : baseClass.getLanguageType());

		if (iArgs == null || iArgs.length == 0)
			return currClass.newInstance();

		// TODO: SUPPORT SEARCH OF THE RIGHT CONSTRUCTOR
		Class<?>[] parameterTypes = new Class[iArgs.length];
		for (int i = 0; i < iArgs.length; ++i)
			if (iArgs[i] != null)
				parameterTypes[i] = iArgs[i].getClass();

		// TRY TO ASSIGN THE ENTITY IN THE CONSTRUCTOR
		try {
			Constructor<?> constructor = currClass.getConstructor(parameterTypes);
			return constructor.newInstance(iArgs);
		} catch (NoSuchMethodException e) {
			if (parameterTypes.length == 1) {
				Class<?> pType = parameterTypes[0].getSuperclass();
				while (!pType.equals(Object.class)) {
					try {
						Constructor<?> constructor = currClass.getConstructor(new Class[] { pType });
						return constructor.newInstance(iArgs);
					} catch (NoSuchMethodException xe) {
						pType = pType.getSuperclass();
					}
				}
			}
		}

		return null;
	}

	@Override
	public boolean isOfType(Class<?> iLanguageClass) {
		return javaClass != null ? javaClass.equals(iLanguageClass) : false;
	}

	@Override
	public boolean isArray() {
		return javaClass != null ? javaClass.isArray() : false;
	}

	@Override
	public void config() {
		beginConfig();
		inspectInheritance();
		readAllAnnotations();

		readFields();
		readActions();
		readEvents();
		endConfig();
	}

	public Class<?> getLanguageType() {
		return (Class<?>) (javaClass != null ? javaClass : baseClass.getLanguageType());
	}

	@Override
	public String toString() {
		return name + (javaClass != null ? " (class:" + javaClass.getName() + ")" : "");
	}

	public SchemaClass getBaseClass() {
		return baseClass;
	}

	protected void inspectInheritance() {
		// REGISTER THE SUPER CLASS
		Class<?> javaSuperClass = null;

		if (javaClass != null)
			// GET SUPER CLASS IF ANY
			javaSuperClass = javaClass.getSuperclass();
		else
			// COPY FROM BASE CLASS
			javaSuperClass = (Class<?>) baseClass.getLanguageType();

		superClass = baseClass;
		if (superClass == null && javaSuperClass != null) {
			if (Roma.existComponent(SchemaManager.class)) {
				superClass = searchForInheritedClassOrInterface(javaSuperClass);
			} else
				superClass = new SchemaClassReflection(javaSuperClass);

			if (javaSuperClass.equals(this))
				// EXTENSION BY CONVENTION NAME: REMOVE PARENT TO AVOID RECURSION
				javaSuperClass = null;
		}

		if (superClass == null && !name.equals(GlobalConstants.ROOT_CLASS))
			// FORCE ALL THE CLASS TO EXTEND THE ROOT 'OBJECT'
			superClass = Roma.schema().getSchemaClass(Object.class);

		// REGISTER ALL IMPLEMENTED INTERFACES
		if (javaClass != null) {
			Class<?>[] ifcs = javaClass.getInterfaces();
			if (ifcs != null) {
				SchemaClass schemaIfc;
				for (Class<?> ifc : ifcs) {
					schemaIfc = searchForInheritedClassOrInterface(ifc);
					addImplementsInterface(schemaIfc);
				}
			}
		}

		if (superClass != null && !superClass.getLanguageType().equals(Object.class))
			// LAST REAL INHERITANCE WINS
			makeDependency(superClass);
	}

	@SuppressWarnings("unchecked")
	protected void readAllAnnotations() {
		String annotationName;
		Class<? extends Annotation> annotationClass;
		Annotation annotation;

		XmlClassAnnotation parentDescriptor = null;

		if (descriptor != null)
			parentDescriptor = descriptor.getType();

		// BROWSE ALL ASPECTS
		for (Aspect aspect : Roma.aspects()) {
			// READ CLASS ANNOTATIONS

			if (javaClass != null) {
				// COMPOSE ANNOTATION NAME BY ASPECT
				annotationName = aspect.aspectName();
				annotationName = Character.toUpperCase(annotationName.charAt(0)) + annotationName.substring(1) + "Class";

				// CHECK FOR ANNOTATION PRESENCE
				try {
					annotationClass = (Class<? extends Annotation>) Class.forName(Utility.ROMA_PACKAGE + ".aspect." + aspect.aspectName()
							+ ".annotation." + annotationName);
					annotation = javaClass.getAnnotation(annotationClass);
				} catch (ClassNotFoundException e) {
					// ANNOTATION CLASS NOT EXIST FOR CURRENT ASPECT
					annotation = null;
				}
			} else
				annotation = null;

			// READ XML ANNOTATIONS
			aspect.configClass(this, annotation, parentDescriptor);
		}
	}

	protected void readFields() {
		readFields((Class<?>) (javaClass != null ? javaClass : baseClass.getLanguageType()));
	}

	protected void readFields(Class<?> iClass) {
		Field field;
		SchemaFieldReflection fieldInfo;
		Method setterMethod;
		String fieldName;
		SchemaClass fieldType = null;
		Class<?> javaFieldType;
		for (Method getterMethod : SchemaHelper.getMethods(iClass)) {
			if (Modifier.isStatic(getterMethod.getModifiers()))
				// JUMP STATIC FIELDS
				continue;

			if (!Modifier.isPublic(getterMethod.getModifiers()))
				// JUMP NOT PUBLIC FIELDS
				continue;

			fieldName = getterMethod.getName();

			int prefixLength;
			if (fieldName.startsWith(GET_METHOD))
				prefixLength = GET_METHOD.length();
			else if (fieldName.startsWith(IS_METHOD) && Character.isUpperCase(fieldName.charAt(IS_METHOD.length())))
				prefixLength = IS_METHOD.length();
			else
				continue;

			if (getterMethod.getParameterTypes() != null && getterMethod.getParameterTypes().length > 0)
				continue;

			if (isToIgnoreMethod(getterMethod))
				// IGNORE THE METHOD SINCE IT'S IN IGNORE_METHOD_NAMES
				continue;

			if (fieldName.length() <= prefixLength)
				// GET METHOD ONLY: JUMP IT
				continue;

			log.debug("[SchemaClassReflection] Class " + getName() + " found field: " + fieldName);

			// GET FIELD NAME
			fieldName = Character.toLowerCase(fieldName.charAt(prefixLength)) + fieldName.substring(prefixLength + 1);

			// GET FIELD TYPE
			javaFieldType = getterMethod.getReturnType();

			try {
				// TRY TO FIND SETTER METHOD IF ANY
				setterMethod = iClass.getMethod(SET_METHOD + getterMethod.getName().substring(prefixLength), new Class[] { javaFieldType });
			} catch (Exception e) {
				setterMethod = null;
			}

			// TRY TO FIND FIELD IF ANY
			field = SchemaHelper.getField(iClass, fieldName);
			if (field != null && field.getType() != Object.class) {
				// OVERRIDE GETTER METHOD'S TYPE WITH THE FIELD TYPE
				javaFieldType = field.getType();
			}

			fieldInfo = (SchemaFieldReflection) getField(fieldName);
			fieldType = null;

			if (getterMethod.getName().equals("getEntity") && ComposedEntity.class.isAssignableFrom(getterMethod.getDeclaringClass())
					&& getFeatures(CoreAspect.ASPECT_NAME) != null) {
				if (fieldInfo != null && fieldInfo.getType() != null && !fieldInfo.getType().getName().equals("Object")) {
					// RE-USE THE TYPE INHERITED BY CLONING
					fieldType = (SchemaClass) fieldInfo.getType();
					javaFieldType = fieldInfo.getLanguageType();
				} else {
					// ENTITY FIELD: CHECK FOR SPECIAL ENTITY TYPE
					// TODO: REMOVE THIS WIRED CONCEPT
					fieldType = (SchemaClass) getFeatures(CoreAspect.ASPECT_NAME).getAttribute(CoreClassFeatures.ENTITY);

					if (fieldType != null)
						javaFieldType = ((SchemaClassReflection) fieldType).getLanguageType();
					else if (!Modifier.isAbstract(iClass.getModifiers()) && !Modifier.isInterface(iClass.getModifiers())) {
						log.warn("[SchemaClassReflection.readFields] Cannot find the annotation XML and/or Java annotation @CoreClass(entity=X.class) for class '"
								+ iClass
								+ "'. Since it's a ComposedEntity implementation an annotation is required to expand the entity correctly.");
					}
				}
			}

			if (fieldInfo == null) {
				// FIELD NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
				fieldInfo = new SchemaFieldReflection(this, fieldName);
				setField(fieldName, fieldInfo);
			}

			// GENERATE OR OVERWRITE (IN CASE OF INHERITANCE) FIELD CONFIGURATION
			fieldInfo.configure(fieldType, javaFieldType, field, getterMethod, setterMethod);

			fieldInfo.setOrder(getFieldOrder(fieldInfo));
		}

		Collections.sort(orderedFields);
	}

	protected void readActions() {
		if (javaClass == null)
			// NO CONCRETE JAVA CLASS FOUND
			return;

		SchemaActionReflection actionInfo;
		String methodName;

		for (Method currentMethod : SchemaHelper.getMethods(javaClass)) {
			methodName = currentMethod.getName();

			log.debug("[SchemaClassReflection] TEMP Class " + getName() + " found method: " + currentMethod);

			if (Modifier.isStatic(currentMethod.getModifiers()))
				// JUMP STATIC METHODS
				continue;

			if (!Modifier.isPublic(currentMethod.getModifiers()))
				// IGNORE NON PUBLIC METHODS
				continue;

			if (isToIgnoreMethod(currentMethod))
				// IGNORE METHOD
				continue;

			if (isSetter(currentMethod) || isGetter(currentMethod))
				// GETTER OR SETTER: IGNORE IT (ARE TREATED AS FIELDS)
				continue;

			// FILL METHOD SIGNATURE
			String methodSignature = getMethodSignature(currentMethod);

			log.debug("[SchemaClassReflection] Class " + getName() + " found method: " + methodSignature);

			actionInfo = (SchemaActionReflection) getAction(methodSignature);

			if (actionInfo == null) {
				List<SchemaParameter> orderedParameters = new ArrayList<SchemaParameter>();
				for (int i = 0; i < currentMethod.getParameterTypes().length; ++i) {
					orderedParameters.add(new SchemaParameter("param" + i, Roma.schema().getSchemaClassIfExist(
							currentMethod.getParameterTypes()[i])));
				}

				// ACTION NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
				actionInfo = new SchemaActionReflection(this, methodName, orderedParameters);

				setAction(methodSignature, actionInfo);
			}

			actionInfo.setReturnType(Roma.schema().getSchemaClassIfExist(currentMethod.getReturnType()));

			// GENERATE OR OVERWRITE (IN CASE OF INHERITANCE) ACTION CONFIGURATION
			actionInfo.configure(currentMethod);

			actionInfo.setOrder(getActionOrder(actionInfo));
		}

		Collections.sort(orderedActions);
	}

	private boolean isGetter(Method currentMethod) {
		boolean result = !currentMethod.getReturnType().equals(Void.TYPE) && currentMethod.getParameterTypes().length == 0
				&& (currentMethod.getName().startsWith(GET_METHOD) || currentMethod.getName().startsWith(IS_METHOD));
		if (result) {
			String methodName = currentMethod.getName();
			String prefix = GET_METHOD;
			if (methodName.startsWith(IS_METHOD)) {
				prefix = IS_METHOD;
			}
			result = checkIfFirstCharAfterPrefixIsUpperCase(methodName, prefix);
		}
		return result;
	}

	private boolean isSetter(Method currentMethod) {
		boolean result = currentMethod.getParameterTypes().length == 1 && currentMethod.getName().startsWith(SET_METHOD);
		if (result) {
			result = checkIfFirstCharAfterPrefixIsUpperCase(currentMethod.getName(), SET_METHOD);
		}
		return result;
	}

	private boolean checkIfFirstCharAfterPrefixIsUpperCase(String methodName, String prefix) {
		return methodName.length() > prefix.length() ? Character.isUpperCase(methodName.charAt(prefix.length())) : false;
	}

	public static String getMethodSignature(Method currentMethod) {
		Class<?> parTypes[] = currentMethod.getParameterTypes();
		String[] params = new String[parTypes.length];
		for (int i = 0; i < parTypes.length; ++i) {
			params[i] = parTypes[i].getSimpleName();
		}
		return SchemaAction.getSignature(currentMethod.getName(), params);
	}

	private void addEvent(String eventName, SchemaField field, Method eventMethod) {

		SchemaEvent eventInfoBase = null;
		if (field == null)
			eventInfoBase = getEvent(eventName);
		else
			eventInfoBase = field.getEvent(eventName);

		SchemaEventReflection eventInfo = null;
		if (eventInfoBase instanceof SchemaEventReflection)
			eventInfo = (SchemaEventReflection) eventInfoBase;

		if (eventInfo == null) {
			// EVENT NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
			List<SchemaParameter> orderedParameters = new ArrayList<SchemaParameter>();
			for (int i = 0; i < eventMethod.getParameterTypes().length; ++i) {
				orderedParameters.add(new SchemaParameter("param" + i, Roma.schema().getSchemaClassIfExist(
						eventMethod.getParameterTypes()[i])));
			}
			if (field == null) {
				eventInfo = new SchemaEventReflection(this, eventName, orderedParameters);
				setEvent(eventName, eventInfo);
			} else {
				eventInfo = new SchemaEventReflection(field, eventName, orderedParameters);
				field.setEvent(eventName, eventInfo);
			}

		}
		eventInfo.configure(eventMethod);
	}

	private void readEvents() {
		if (javaClass == null)
			// NO CONCRETE JAVA CLASS FOUND
			return;

		String eventMethodName;
		for (Method eventMethod : SchemaHelper.getMethods(javaClass)) {

			if (Modifier.isStatic(eventMethod.getModifiers()))
				// JUMP STATIC FIELDS
				continue;

			if (!Modifier.isPublic(eventMethod.getModifiers()))
				// JUMP NOT PUBLIC FIELDS
				continue;

			if (!eventMethod.getName().startsWith(SchemaEvent.ON_METHOD))
				continue;

			// GET FIELD NAME
			eventMethodName = eventMethod.getName().substring(SchemaEvent.ON_METHOD.length());

			if (Character.isLowerCase(eventMethodName.charAt(0)))
				continue;

			eventMethodName = firstToLower(eventMethodName);
			SchemaField fieldEvent = getFieldComposedEntity(eventMethodName);
			String eventName = lastCapitalWords(eventMethodName);
			eventName = firstToLower(eventName);

			if (fieldEvent != null) {
				if (!eventMethodName.equals(eventName)) {
					String fieldName = eventMethodName.substring(0, eventMethodName.length() - eventName.length());
					fieldName = firstToLower(fieldName);
					SchemaField field = getFieldComposedEntity(fieldName);
					if (field != null) {
						if (log.isWarnEnabled())
							log.warn("The method '" + eventMethod + "' will be associated as default event for the field '"
									+ fieldEvent.getEntity().getSchemaClass().getName() + "." + fieldEvent.getName() + "' instead of '" + eventName
									+ "' event for the field '" + field.getEntity().getSchemaClass().getName() + "." + field.getName() + "' ");
					}
				}
				addEvent(SchemaEvent.DEFAULT_EVENT_NAME, fieldEvent, eventMethod);
				continue;
			}

			if (eventMethodName.equals(eventName)) {
				addEvent(eventName, null, eventMethod);
			} else {
				// EVENT IS A FIELD EVENT
				String fieldName = eventMethodName.substring(0, eventMethodName.length() - eventName.length());
				fieldName = firstToLower(fieldName);
				SchemaField field = getFieldComposedEntity(fieldName);

				if (field == null) {
					if (log.isWarnEnabled())
						log.warn("[SchemaClassReflection] Cannot associate the event '" + getName() + "." + eventName + "' to the field '"
								+ getName() + "." + fieldName + "'. The event will be ignored.");
					continue;
				}
				addEvent(eventName, field, eventMethod);
			}
			// GENERATE OR OVERWRITE (IN CASE OF INHERITANCE) EVENT CONFIGURATION
		}
	}

	protected boolean isToIgnoreMethod(Method currentMethod) {
		String methodName = currentMethod.getName();
		// CHECK FOR FIXED NAMES TO IGNORE
		for (int ignoreId = 0; ignoreId < IGNORE_METHOD_NAMES.length; ++ignoreId) {
			if (SchemaAction.ignoreMethod(IGNORE_METHOD_NAMES[ignoreId], methodName))
				return true;
		}

		if (Roma.existComponent(SchemaManager.class))
			// CHECK FOR CUSTOM NAMES TO IGNORE, IF ANY
			for (Iterator<String> it = Roma.schema().getIgnoreActions().iterator(); it.hasNext();) {
				if (SchemaAction.ignoreMethod(it.next(), methodName))
					return true;
			}

		return false;
	}

	protected void beginConfig() {
		for (Aspect aspect : Roma.aspects()) {
			aspect.beginConfigClass(this);
		}
	}

	protected void endConfig() {
		for (Aspect aspect : Roma.aspects()) {
			aspect.endConfigClass(this);
		}
	}

	public boolean isInterface() {
		return javaClass != null ? javaClass.isInterface() : false;
	}

	@Override
	public boolean isPrimitive() {
		return javaClass != null ? javaClass.isPrimitive() : false;
	}

	@Override
	public boolean isAbstract() {
		return javaClass != null ? Modifier.isAbstract(javaClass.getModifiers()) : false;
	}

	@Override
	public String getFullName() {
		return javaClass.getName();
	}
}
