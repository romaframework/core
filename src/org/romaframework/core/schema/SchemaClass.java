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

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.feature.CoreClassFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.domain.entity.ComposedEntity;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

/**
 * Represent a class. It's not necessary that a Java class exist in the Classpath since you can define a SchemaClassReflection that
 * inherits another Java Class and use XML descriptor to customize it. This feature avoid the writing of empty class that simply
 * inherit real domain class.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class SchemaClass extends SchemaClassDefinition implements Comparable<SchemaClass> {

	private static final long			serialVersionUID	= 5613421165403906360L;

	protected String							name;

	protected SchemaClass					superClass;
	protected SchemaConfiguration	descriptor;
	protected boolean							reloadingStatus		= false;
	protected Set<SchemaClass>		dependentClasses;
	protected Set<SchemaClass>		implementedInterfaces;

	private static Log						log								= LogFactory.getLog(SchemaClass.class);

	public SchemaClass(String iName) {
		name = iName;
	}

	public abstract boolean isAbstract();

	public abstract boolean isInterface();

	public abstract boolean isArray();

	public abstract boolean isPrimitive();
	
	public abstract boolean isEnum();

	public abstract boolean isOfType(Class<?> iLanguageClass);

	public abstract Object getLanguageType();

	public abstract Object newInstanceFinal(Object... iArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException;

	public abstract void config();

	public abstract String getFullName();

	public Object newInstance(Object... iArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException, SecurityException,
			InvocationTargetException, NoSuchMethodException {
		try {
			// CREATE THE CONTEXT BEFORE TO CALL THE ACTION
			Roma.context().create();
			return newInstanceFinal(iArgs);

		} finally {
			// ASSURE TO DESTROY THE CONTEXT
			Roma.context().destroy();
		}
	}

	/**
	 * Copy the definition from class schema.
	 * 
	 * @param iSource
	 */
	public void copyDefinition(SchemaClassDefinition iSource) {
		if (iSource == null)
			return;
		this.features = null;
		this.parent = iSource;
		try {
			cloneFields(iSource, null);
			cloneActions(iSource, null);
			cloneEvents(iSource, null);
		} catch (CloneNotSupportedException e) {
			log.error("[SchemaClass.copyDefinition] Can't clone element", e);
		}
	}

	public Object getFieldValue(Object iObject, String iFieldName) {
		return SchemaHelper.getFieldValue(iObject, iFieldName);
	}

	public Object setFieldValue(Object iObject, String iFieldName, Object iFieldValue) {
		SchemaHelper.setFieldValue(iObject, iFieldName, iFieldValue);
		return this;
	}

	public boolean isComposedEntity() {
		return SchemaHelper.isAssignableAs(this, Roma.schema().getSchemaClass(ComposedEntity.class));
	}

	/**
	 * Check if the current schema class implements or extends iClass
	 * 
	 * @param iClass
	 *          Class to check for inheritance or interface to check if implements it
	 * @return true if is assignable, otherwise false
	 */
	public boolean isAssignableAs(Class<?> iClass) {
		return isAssignableAs(Roma.schema().getSchemaClass(iClass));
	}

	/**
	 * Check if the iClass implements or extends the current one.
	 * 
	 * @return true if is assignable, otherwise false
	 */
	public boolean isAssignableFrom(Class<?> iClass) {
		return SchemaHelper.isAssignableAs(Roma.schema().getSchemaClass(iClass), this);
	}

	/**
	 * Check if the iClassInterface implements or extends the current one.
	 * 
	 * @return true if is assignable, otherwise false
	 */
	public boolean isAssignableFrom(SchemaClass iClassInterface) {
		return SchemaHelper.isAssignableAs(iClassInterface, this);
	}

	/**
	 * Check if the current schema class implements or extends iSchema
	 * 
	 * @param iClassInterface
	 *          iSchema to check for inheritance or interface to check if implements it
	 * @return true if is assignable, otherwise false
	 */
	public boolean isAssignableAs(SchemaClass iClassInterface) {
		return SchemaHelper.isAssignableAs(this, iClassInterface);
	}

	public boolean extendsClass(SchemaClass iClass) {
		return SchemaHelper.extendsClass(this, iClass);
	}

	public boolean implementsInterface(SchemaClass iInterface) {
		return SchemaHelper.implementsInterface(this, iInterface);
	}

	/**
	 * Reload class configuration from file. This event is invoked when the file descriptor is changed.
	 */
	public void signalUpdatedFile(File iFile) {
		try {
			reset();

			reloadingStatus = true;
			if (iFile != null && iFile.getName().endsWith(SchemaClassResolver.DESCRIPTOR_SUFFIX))
				descriptor.load();

			config();
			reloadingStatus = false;
			log.warn("[SchemaClass.signalUpdatedFile] Reloading configuration for class:  '" + name + "' from file: " + iFile);

			if (dependentClasses != null) {
				// IF ANY, RELOAD ALL SUB-CLASSES IN CASCADING
				for (SchemaClass cls : dependentClasses)
					if (!cls.equals(this))
						cls.signalUpdatedFile(null);
			}
		} catch (Exception e) {
			log.error("[SchemaClass.signalUpdatedFile] Error", e);
		}
	}

	protected void reset() {
		fields.clear();
		orderedFields.clear();
		actions.clear();
		orderedActions.clear();
		events.clear();
	}

	public Set<SchemaClass> getDependentClasses() {
		return dependentClasses;
	}

	@Override
	public String getName() {
		return name;
	}

	public SchemaClass getSuperClass() {
		return superClass;
	}

	public SchemaConfiguration getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(SchemaConfiguration iDescriptor) {
		descriptor = iDescriptor;
	}

	@Override
	public SchemaClass getSchemaClass() {
		return this;
	}

	/**
	 * Add dependent class to determine inheritance three. Useful on reloading to refresh all entity in cascading.
	 * 
	 * @param iDependentClassEntity
	 *          Dependent Entity
	 */
	public void addDependentClass(SchemaClass iDependentClassEntity) {
		if (iDependentClassEntity == null)
			return;

		// CREATE THE SET THE FIRST TIME
		if (dependentClasses == null)
			synchronized (iDependentClassEntity) {
				if (dependentClasses == null)
					dependentClasses = new HashSet<SchemaClass>();
			}

		dependentClasses.add(iDependentClassEntity);
	}

	protected String lastCapitalWords(String name) {
		char[] chars = name.toCharArray();
		for (int i = name.length() - 1; i > 0; i--) {
			if (Character.isUpperCase(chars[i])) {
				return name.substring(i);
			}
		}
		return name;
	}

	@Override
	public String toString() {
		return name;
	}

	public int getFieldOrder(SchemaField iField) {
		String[] orderedValues = getFeature(CoreClassFeatures.ORDER_FIELDS);

		if (orderedValues != null) {
			for (int fieldNum = 0; fieldNum < orderedValues.length; ++fieldNum) {
				if (orderedValues[fieldNum].equals(iField.getName()))
					return fieldNum;
			}
		}

		if (descriptor != null && descriptor.getType() != null && descriptor.getType().getFields() != null) {
			Collection<XmlFieldAnnotation> allFields = descriptor.getType().getFields();
			int fieldNum = 0;
			for (XmlFieldAnnotation field : allFields) {
				if (field.getName().equals(iField.getName())) {
					return fieldNum;
				}
				fieldNum++;
			}
		}

		return iField.getOrder();
	}

	public int getActionOrder(SchemaClassElement iAction) {
		String orderedValues[] = getFeature(CoreClassFeatures.ORDER_ACTIONS);

		if (orderedValues != null) {
			for (int actionNum = 0; actionNum < orderedValues.length; ++actionNum) {
				if (orderedValues[actionNum].equals(iAction.getName()))
					return actionNum;
			}
		}

		if (descriptor != null && descriptor.getType() != null && descriptor.getType().getActions() != null) {
			Collection<XmlActionAnnotation> allActions = descriptor.getType().getActions();
			short actionNum = 0;
			for (XmlActionAnnotation action : allActions) {
				if (action.getName().equals(iAction.getName())) {
					return actionNum;
				}
				actionNum++;
			}
		}
		return iAction.getOrder();
	}

	public Collection<SchemaEvent> getEvents() {
		return events.values();
	}

	public Set<SchemaClass> getImplementedInterfaces() {
		return implementedInterfaces;
	}

	@Override
	public boolean equals(Object arg0) {
		if (arg0 == null || !(getClass().isAssignableFrom(arg0.getClass())))
			return false;

		if (arg0 == this)
			return true;

		SchemaClass other = (SchemaClass) arg0;

		if (name != null && name.equals(other.getName())) {
			if (getLanguageType() != null && other.getLanguageType() != null)
				return getLanguageType().equals(other.getLanguageType());

			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		if (name != null)
			return name.hashCode();
		return -1;
	}

	public int compareTo(SchemaClass o) {
		if (o == null)
			return 1;

		if (name == null)
			return 0;

		return name.compareTo(o.getName());
	}

	protected static String firstToLower(String str) {
		return Character.toLowerCase(str.charAt(0)) + str.substring(1);
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		SchemaClass cloned = (SchemaClass) super.clone();

		if (dependentClasses != null) {
			cloned.dependentClasses = new HashSet<SchemaClass>();
			cloned.dependentClasses.addAll(dependentClasses);
		}

		if (implementedInterfaces != null) {
			cloned.implementedInterfaces = new HashSet<SchemaClass>();
			cloned.implementedInterfaces.addAll(implementedInterfaces);
		}

		return cloned;
	}

	protected void makeDependency(SchemaClass iClass) {
		if (equals(iClass))
			// AVOID CIRCULAR DEPENDENCY
			return;

		if (!reloadingStatus)
			// ADD MYSELF AS SUB-CLASS OF THIS CLASS/INTERFACE
			iClass.addDependentClass(this);

		try {
			copyDefinition(iClass);

		} catch (Exception e) {
			log.error("Error on making dependency between class " + name + " and " + iClass.getName(), e);
		}
	}

	protected SchemaClass searchForInheritedClassOrInterface(Class<?> javaSuperClass) {
		try {
			SchemaClass cls = Roma.schema().getSchemaClassFullPackage(javaSuperClass);
			if (cls == null) {
				cls = Roma.schema().createSchemaClass(javaSuperClass, null);
			}
			return cls;
		} catch (Exception e) {
			if (log.isDebugEnabled())
				// CANNOT LOAD THE SUPER CLASS: IGNORE IT
				log.debug("[SchemaClass.inspectInheritance] Can't load the class '" + javaSuperClass + "' as super class of '" + name
						+ "'. Now it will be ignored but assure that is included between the packages explored by Roma");
		}
		return null;
	}

	protected void addImplementsInterface(SchemaClass schemaIfc) {
		if (schemaIfc == null)
			return;

		if (implementedInterfaces == null)
			// LAZY CREATION OF SET TO SAVE RESOURCES
			implementedInterfaces = new HashSet<SchemaClass>();

		implementedInterfaces.add(schemaIfc);

		makeDependency(schemaIfc);
	}
}
