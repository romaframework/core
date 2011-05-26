/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.schema.virtual;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.GlobalConstants;
import org.romaframework.core.Roma;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.exception.ConfigurationNotFoundException;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.SchemaHelper;
import org.romaframework.core.schema.SchemaReloader;
import org.romaframework.core.schema.config.SaxSchemaConfiguration;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

/**
 * Virtual representation of a class. It's created when real class is not available.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaClassVirtual extends SchemaClass {

	protected SchemaClassDefinition	baseClass;
	protected boolean								interfaceType	= false;
	protected boolean								arrayType			= false;
	protected boolean								abstractType	= false;
	protected String								fullName;

	private static Log							log						= LogFactory.getLog(SchemaClassVirtual.class);

	public SchemaClassVirtual(String iEntityName) {
		super(iEntityName);
	}

	public SchemaClassVirtual(String iEntityName, SchemaClass iSuperClass) {
		super(iEntityName);
		superClass = iSuperClass;
		makeDependency(superClass);
	}

	public SchemaClassVirtual(String iEntityName, SchemaClassDefinition iBaseClass, SchemaConfiguration iDescriptor)
			throws ConfigurationNotFoundException {
		super(iEntityName);

		baseClass = iBaseClass;

		// USE THE DESCRIPTOR AS PARAMETER (DEFINED AS INLINE)
		descriptor = iDescriptor;
		if (descriptor != null)
			fullName = descriptor.getFilePath();

		if (iBaseClass == null && descriptor instanceof SaxSchemaConfiguration)
			// NOT INLINE: REGISTER THE DESCRIPTOR FILE FOR AUTO-RELOADING
			Roma.component(SchemaReloader.class).addResourceForReloading(((SaxSchemaConfiguration) descriptor).getFile(), iEntityName);
	}

	@Override
	public Object newInstanceFinal(Object... iArgs) throws InstantiationException, IllegalAccessException, IllegalArgumentException,
			SecurityException, InvocationTargetException, NoSuchMethodException {
		return new VObject(this);
	}

	@Override
	public boolean isOfType(Class<?> iLanguageClass) {
		return name.equals(iLanguageClass.getSimpleName());
	}

	@Override
	public boolean isInterface() {
		return interfaceType;
	}

	@Override
	public boolean isArray() {
		return arrayType;
	}

	@Override
	public void config() {
		inspectInheritance();
		readAllAnnotations();

		if (descriptor == null)
			return;

		readFields();
		readActions();
		readEvents();
	}

	public Class<?> getLanguageType() {
		return null;
	}

	@Override
	public String toString() {
		return name;
	}

	public SchemaClassDefinition getBaseClass() {
		return baseClass;
	}

	protected void inspectInheritance() {
		if (descriptor != null) {
			// SEARCH FOR CLASS EXTENSION
			if (descriptor.getType().getExtendClass() != null)
				superClass = Roma.schema().getSchemaClass(descriptor.getType().getExtendClass());

			// BROWSE TO IMPLEMENTED INTERFACES
			if (descriptor.getType().getImplementsInterfaces() != null)
				for (String ifc : descriptor.getType().getImplementsInterfaces()) {
					addImplementsInterface(Roma.schema().getSchemaClass(ifc));
				}
		}

		if (superClass == null && !name.equals(GlobalConstants.ROOT_CLASS))
			// FORCE ALL THE CLASS TO EXTEND THE ROOT 'OBJECT'
			superClass = Roma.schema().getSchemaClass(Object.class);

		if (superClass != null && !Object.class.equals(superClass.getLanguageType()))
			// LAST REAL INHERITANCE WINS
			makeDependency(superClass);
	}

	protected void readAllAnnotations() {
		XmlClassAnnotation parentDescriptor = null;

		if (descriptor != null)
			parentDescriptor = descriptor.getType();

		// BROWSE ALL ASPECTS
		for (Aspect aspect : Roma.aspects()) {
			// READ XML ANNOTATIONS
			aspect.configClass(this, null, parentDescriptor);
		}
	}

	protected void readFields() {
		if (descriptor.getType().getFields() == null)
			return;

		SchemaField fieldInfo = null;
		SchemaClass fieldType;
		for (XmlFieldAnnotation field : descriptor.getType().getFields()) {
			log.debug("[SchemaClassVirtual] Class " + getName() + " found field: " + field.getName());

			fieldInfo = getField(field.getName());
			if (fieldInfo == null) {
				// FIELD NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
				fieldInfo = new SchemaFieldVirtual(this, field.getName(), field.getType());
				setField(field.getName(), fieldInfo);
			} else if (!(fieldInfo instanceof SchemaFieldVirtual)) {
				// CREATE A NEW VIRTUAL SCHEMA FIELD THAT EXTENDS THE OTHER
				fieldInfo = new SchemaFieldVirtual(this, field.getName(), fieldInfo);
				setField(field.getName(), fieldInfo);
			}

			if (fieldInfo.getType() == null)
				throw new ConfigurationException("Error on loading field definition '" + field.getName()
						+ "' for the virtual pojo of class: " + name);

			fieldType = (SchemaClass) fieldInfo.getType();

			// GENERATE OR OVERWRITE (IN CASE OF INHERITANCE) FIELD CONFIGURATION
			((SchemaFieldVirtual) fieldInfo).configure(fieldType);

			fieldInfo.setOrder(getFieldOrder(fieldInfo));
		}

		// CREATE A WRAPPER FOR ALL REMAINING FIELDS
		for (SchemaField field : fields.values()) {
			if (!(field instanceof SchemaFieldVirtual)) {
				// CREATE A WRAPPER FOR IT
				fieldInfo = new SchemaFieldVirtual(this, field.getName(), field);
				setField(field.getName(), fieldInfo);
				((SchemaFieldVirtual) fieldInfo).configure(field.getType() != null ? field.getType().getSchemaClass() : null);
			}
		}

		Collections.sort(orderedFields);
	}

	protected void readActions() {
		if (descriptor.getType().getActions() == null)
			return;

		SchemaAction actionInfo;
		for (XmlActionAnnotation action : descriptor.getType().getActions()) {
			log.debug("[SchemaClassVirtual] Class " + getName() + " found action: " + action.getName());

			actionInfo = getAction(action.getName());

			if (actionInfo == null) {
				// ACTION NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
				actionInfo = new SchemaActionVirtual(this, action.getName());
				setAction(action.getName(), actionInfo);
			} else if (!(actionInfo instanceof SchemaActionVirtual)) {
				// CREATE A NEW VIRTUAL SCHEMA ACTION THAT EXTENDS THE OTHER
				actionInfo = new SchemaActionVirtual(this, action.getName(), actionInfo);
				setAction(action.getName(), actionInfo);
			}

			// GENERATE OR OVERWRITE (IN CASE OF INHERITANCE) ACTION CONFIGURATION
			((SchemaActionVirtual) actionInfo).configure();

			actionInfo.setOrder(getActionOrder(actionInfo));
		}

		// CREATE A WRAPPER FOR ALL REMAINING FIELDS
		for (SchemaAction action : actions.values()) {
			if (!(action instanceof SchemaActionVirtual)) {
				// CREATE A WRAPPER FOR IT
				actionInfo = new SchemaActionVirtual(this, action.getName(), action);
				setAction(action.getName(), actionInfo);
				((SchemaActionVirtual) actionInfo).configure();
			}
		}

		Collections.sort(orderedActions);
	}

	private void readEvents() {
		if (descriptor.getType().getEvents() == null)
			return;

		for (XmlEventAnnotation event : descriptor.getType().getEvents()) {
			log.debug("[SchemaClassVirtual] Class " + getName() + " found event: " + event.getName());

			SchemaField fieldEvent = getFieldComposedEntity(event.getName());
			String eventName = lastCapitalWords(event.getName());
			eventName = firstToLower(eventName);

			if (fieldEvent != null) {
				if (!event.getName().equals(eventName)) {
					String fieldName = event.getName().substring(0, event.getName().length() - eventName.length());
					fieldName = firstToLower(fieldName);
					SchemaField field = getFieldComposedEntity(fieldName);
					if (field != null) {
						if (log.isWarnEnabled())
							log.warn("The action '" + event.getName() + "' will be associated as default event for the field '"
									+ fieldEvent.getEntity().getSchemaClass().getName() + "." + fieldEvent.getName() + "' instead of '" + eventName
									+ "' event for the field '" + field.getEntity().getSchemaClass().getName() + "." + field.getName() + "' ");
					}
				}
				addEvent(SchemaEvent.DEFAULT_EVENT_NAME, fieldEvent);
				continue;
			}

			if (event.getName().equals(eventName)) {
				addEvent(eventName, null);
			} else {
				// EVENT IS A FIELD EVENT
				String fieldName = event.getName().substring(0, event.getName().length() - eventName.length());
				fieldName = firstToLower(fieldName);
				SchemaField field = getFieldComposedEntity(fieldName);

				if (field == null) {
					if (log.isWarnEnabled())
						log.warn("[SchemaClassVirtual] Cannot associate the event '" + getName() + "." + eventName + "' to the field '"
								+ getName() + "." + fieldName + "'. The event will be ignored.");
					continue;
				}
				addEvent(eventName, field);
			}
			// GENERATE OR OVERWRITE (IN CASE OF INHERITANCE) EVENT CONFIGURATION
		}

		// CREATE A WRAPPER FOR ALL REMAINING EVENTS
		SchemaEventVirtual eventInfo;
		for (SchemaEvent event : events.values()) {
			if (!(event instanceof SchemaEventVirtual)) {
				// CREATE A WRAPPER FOR IT
				eventInfo = new SchemaEventVirtual(this, event.getName(), event);
				setEvent(event.getName(), eventInfo);
				eventInfo.configure();
			}
		}
	}

	protected void addEvent(String eventName, SchemaField field) {
		SchemaEvent eventInfo = null;
		if (field == null)
			eventInfo = getEvent(eventName);
		else
			eventInfo = field.getEvent(eventName);

		if (eventInfo == null) {
			// EVENT NOT EXISTENT: CREATE IT AND INSERT IN THE COLLECTION
			if (field == null) {
				eventInfo = new SchemaEventVirtual(this, eventName);
				setEvent(eventName, eventInfo);
			} else {
				eventInfo = new SchemaEventVirtual(field, eventName);
				field.setEvent(eventName, eventInfo);
			}
		} else if (!(eventInfo instanceof SchemaEventVirtual)) {
			// CREATE A NEW VIRTUAL SCHEMA ACTION THAT EXTENDS THE OTHER
			eventInfo = new SchemaEventVirtual(this, eventName, eventInfo);
			setEvent(eventInfo.getName(), eventInfo);
		}

		((SchemaEventVirtual) eventInfo).configure();
	}

	@Override
	public boolean isPrimitive() {
		return SchemaHelper.isJavaType(name);
	}

	@Override
	public boolean isAbstract() {
		return abstractType;
	}

	@Override
	public String getFullName() {
		return fullName;
	}
}
