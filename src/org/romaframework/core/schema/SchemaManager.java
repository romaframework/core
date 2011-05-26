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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.config.Configurable;
import org.romaframework.core.exception.ConfigurationNotFoundException;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.reflection.SchemaClassFactoryReflection;
import org.romaframework.core.schema.reflection.SchemaClassReflection;
import org.romaframework.core.schema.virtual.SchemaClassFactoryVirtual;
import org.romaframework.core.schema.virtual.VirtualObject;

/**
 * Manage the Entities and cache them.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaManager extends Configurable<String> {
	protected ArrayList<String>							ignoreFields									= new ArrayList<String>();
	protected ArrayList<String>							ignoreActions									= new ArrayList<String>();
	protected ArrayList<String>							ignoreEvents									= new ArrayList<String>();

	protected static final String						PAR_IGNORE_FIELDS							= "ignore-fields";
	protected static final String						PAR_IGNORE_ACTIONS						= "ignore-actions";
	protected static final String						PAR_IGNORE_EVENTS							= "ignore-events";

	protected HashMap<String, SchemaClass>	registeredClassesNoPackage		= new HashMap<String, SchemaClass>();
	protected HashMap<String, SchemaClass>	registeredClassesFullPackage	= new HashMap<String, SchemaClass>();
	protected Set<String>										notFountEntities							= new HashSet<String>();

	protected List<SchemaClassFactory>			factories;

	public SchemaManager() {
	}

	/**
	 * Return the SchemaClass instance by searching with the full class package name.
	 */
	public SchemaClass getSchemaClassFullPackage(Class<?> javaSuperClass) {
		return registeredClassesFullPackage.get(javaSuperClass.getName());
	}

	public SchemaClass getSchemaClassIfExist(Class<?> iJavaClass) {
		if (iJavaClass == null || iJavaClass.equals(Void.TYPE))
			return null;
		try {
			return getSchemaClass(iJavaClass);
		} catch (ConfigurationNotFoundException e) {
			return null;
		}
	}

	public SchemaClass getSchemaClass(Object iObject) {
		if (iObject == null)
			return null;

		return iObject instanceof VirtualObject ? ((VirtualObject) iObject).getClazz() : getSchemaClass(iObject.getClass());
	}

	public SchemaClass getSchemaClass(Class<?> iJavaClass) {
		try {
			return getSchemaClass(Utility.getClassName(iJavaClass));
		} catch (Exception e) {
			return createSchemaClass(iJavaClass, null);
		}
	}

	public SchemaClass getSchemaClass(String iEntityName) {
		if (iEntityName == null)
			return null;

		return getSchemaClass(iEntityName, null, null);
	}

	public SchemaClass getSchemaClass(Class<?> iEntityClass, SchemaConfiguration iDescriptor) {
		return getSchemaClass(Utility.getClassName(iEntityClass), null, iDescriptor);
	}

	/**
	 * Virtual class: implementation by concrete java class but extended by xml descriptor.
	 * 
	 * @param iEntityName
	 * @param iBaseClass
	 * @param iDescriptor
	 * @return
	 */
	public SchemaClass getSchemaClass(String iEntityName, SchemaClass iBaseClass, SchemaConfiguration iDescriptor) {
		if (notFountEntities.contains(iEntityName))
			// AVOID TO RE-TRY TO CREATE IT: IT'S MISSED!
			throw new ConfigurationNotFoundException("Class " + iEntityName);

		SchemaClass entityInfo = registeredClassesNoPackage.get(iEntityName);
		if (entityInfo == null)
			entityInfo = createSchemaClass(iEntityName, iBaseClass, iDescriptor);

		return entityInfo;
	}
	
	/**
	 * checks if a schema class exists or can be created 
	 * @param iEntityName the entity class
	 * @return true if a schema class exists or can be created, false otherwise
	 */
	public boolean existsSchemaClass(Class<?> iEntity) {
		return existsSchemaClass(Utility.getClassName(iEntity));
	}
	/**
	 * checks if a schema class exists or can be created 
	 * @param iEntityName the entity name
	 * @return true if a schema class exists or can be created, false otherwise
	 */
	public boolean existsSchemaClass(String iEntityName) {
		if (iEntityName == null)
			return false;
		if (notFountEntities.contains(iEntityName)){
			return false;
		}
		try{
			SchemaClass sc = getSchemaClass(iEntityName, null, null);
			if(sc==null){
				return false;
			}
		}catch(Exception e){
			return false;
		}
		return true;
	}

	public SchemaClass createSchemaClass(Class<?> iEntityClass, SchemaConfiguration iDescriptor) {
		// CREATE ENTITY
		SchemaClass superClass = null;
		if (iEntityClass.getSuperclass() != null)
			superClass = Roma.schema().getSchemaClass(iEntityClass.getSuperclass());
		return registerSchemaClass(Utility.getClassName(iEntityClass), iEntityClass, superClass, iDescriptor);
	}

	/**
	 * Create and register a class info. Call this if you want to register virtual class built at run-time.
	 * 
	 * @param iEntityName
	 *          Class name, unique name
	 * @param iClass
	 *          Class instance
	 * @param iBaseClass
	 *          Base class where to extend
	 * @param iDescriptor
	 *          Optional XML descriptor
	 * @return Registered ClassInfo instance.
	 * @throws ConfigurationNotFoundException
	 */
	public SchemaClass registerSchemaClass(String iEntityName, Class<?> iClass, SchemaClass iBaseClass,
			SchemaConfiguration iDescriptor) throws ConfigurationNotFoundException {
		// CREATE THE SCHEMA INFO INSTANCE
		SchemaClassReflection cls = new SchemaClassReflection(iEntityName, iClass, iBaseClass, iDescriptor);

		// REGISTER IT (MUST PRECEDE CONFIGURATION FOR SELF-REFERENCED PROPERTIES)
		createEntry(iEntityName, cls);

		cls.config();

		return cls;
	}

	public SchemaClass createSchemaClass(String iEntityName, SchemaClass iBaseClass, SchemaConfiguration iDescriptor) {
		if (!notFountEntities.contains(iEntityName)) {
			SchemaClass cls = null;
			for (SchemaClassFactory factory : factories) {
				cls = factory.createSchemaClass(iEntityName, iBaseClass, iDescriptor);
				if (cls != null) {
					createEntry(iEntityName, cls);

					cls.config();
					// ASSURE TO WRITE AS DEFAULT CLASS THE CURRENT ONE

					createEntry(iEntityName, cls);
					return cls;
				}
			}

			// CLASS NOT FOUND
			notFountEntities.add(iEntityName);
		}

		// ERROR: CANNOT ASSOCIATE A JAVA CLASS
		throw new ConfigurationNotFoundException("Class " + iEntityName);
	}

	public List<SchemaClass> getSchemaClassesByPackage(String packageName) {

		Map<String, String> classes = Roma.component(SchemaClassResolver.class).getClassLocations();

		List<SchemaClass> result = new ArrayList<SchemaClass>();
		if (classes == null)
			return result;

		for (Map.Entry<String, String> entry : classes.entrySet()) {
			try {
				String pkg = entry.getValue();
				if (pkg == null)
					continue;
				if (pkg.startsWith(packageName)) {
					SchemaClass class1 = getSchemaClass(entry.getKey());
					result.add(class1);
				}
			} catch (Exception e) {
			}
		}
		return result;
	}

	/**
	 * Invoked by IoC container at startup.
	 */
	public void config() {
		String cfgIgnore = getConfiguration(PAR_IGNORE_FIELDS);
		if (cfgIgnore != null) {
			StringTokenizer tokenizer = new StringTokenizer(cfgIgnore, ",");
			while (tokenizer.hasMoreTokens())
				ignoreFields.add(tokenizer.nextToken());
		}

		cfgIgnore = getConfiguration(PAR_IGNORE_ACTIONS);
		if (cfgIgnore != null) {
			StringTokenizer tokenizer = new StringTokenizer(cfgIgnore, ",");
			while (tokenizer.hasMoreTokens())
				ignoreActions.add(tokenizer.nextToken());
		}

		cfgIgnore = getConfiguration(PAR_IGNORE_EVENTS);
		if (cfgIgnore != null) {
			StringTokenizer tokenizer = new StringTokenizer(cfgIgnore, ",");
			while (tokenizer.hasMoreTokens())
				ignoreEvents.add(tokenizer.nextToken());
		}

		if (factories == null) {
			// PUT THE REFLECTION AND VIRTUAL FACTORIES BY DEFAULT
			factories = new ArrayList<SchemaClassFactory>();
			factories.add(new SchemaClassFactoryReflection());
			factories.add(new SchemaClassFactoryVirtual());
		}
	}

	public Collection<SchemaClass> getAllClassesInfo() {
		return registeredClassesNoPackage.values();
	}

	public List<SchemaClass> getSchemaClassesByInheritance(String iBaseClass) {
		return getSchemaClassesByInheritance(getSchemaClass(iBaseClass));
	}

	public List<SchemaClass> getSchemaClassesByInheritance(Class<?> iBaseClass) {
		return getSchemaClassesByInheritance(getSchemaClass(iBaseClass));
	}

	/**
	 * Retrieve all SchemaClass that extend or implement iBaseClass.
	 * 
	 * @param iBaseClass
	 *          the base SchemaClass inherited.
	 * @return the list of SchemaClass that inherit iBaseClass.
	 */
	public List<SchemaClass> getSchemaClassesByInheritance(SchemaClass iBaseClass) {
		List<SchemaClass> result = new ArrayList<SchemaClass>();
		for (SchemaClass schema : registeredClassesNoPackage.values()) {
			if (schema.isAssignableAs(iBaseClass))
				result.add(schema);
		}
		return result;
	}

	public boolean isAvailableSchemaClass(String typeName) {
		return registeredClassesNoPackage.containsKey(typeName);
	}

	public List<String> getIgnoreFields() {
		return ignoreFields;
	}

	public List<String> getIgnoreActions() {
		return ignoreActions;
	}

	public List<String> getIgnoreEvents() {
		return ignoreEvents;
	}

	public List<SchemaClassFactory> getFactories() {
		return factories;
	}

	public void setFactories(List<SchemaClassFactory> factories) {
		this.factories = factories;
	}

	private void createEntry(String iEntityName, SchemaClass cls) {
		registeredClassesNoPackage.put(iEntityName, cls);
		if (cls instanceof SchemaClassReflection)
			registeredClassesFullPackage.put(((SchemaClassReflection) cls).getLanguageType().getName(), cls);

		if (notFountEntities.contains(iEntityName))
			// REMOVE THE ENTRY SINCE IT WAS JUST CREATED
			notFountEntities.remove(iEntityName);
	}
}
