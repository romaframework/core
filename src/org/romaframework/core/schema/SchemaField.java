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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.feature.CoreFieldFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.exception.FieldErrorUserException;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.SchemaFieldListener;
import org.romaframework.core.schema.config.SchemaConfiguration;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

/**
 * Generic abstract class that represents a Field.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class SchemaField extends SchemaClassElement {
	private static final long						serialVersionUID	= -4789886810661429988L;

	protected Map<String, SchemaEvent>	events;
	protected SchemaClassDefinition			type;
	protected SchemaClass								embeddedType;
	protected SchemaClass[]							embeddedTypeGenerics;
	private static Log									log								= LogFactory.getLog(SchemaField.class);

	public abstract Object getValue(Object iObject) throws BindingException;

	public abstract boolean isArray();

	public abstract Object getLanguageType();

	protected abstract SchemaClass getSchemaClassFromLanguageType();

	protected abstract void setValueFinal(Object iObject, Object iValue) throws IllegalAccessException, InvocationTargetException;

	public SchemaField(SchemaClassDefinition iEntity, String iName) {
		super(iEntity, iName, FeatureType.FIELD);
		events = new HashMap<String, SchemaEvent>();
	}

	public void setValue(Object iObject, Object iFieldValue) {
		try {
			Object value = convertValue(iFieldValue,getType());
			List<SchemaFieldListener> listeners = Controller.getInstance().getListeners(SchemaFieldListener.class);
			try {
				Roma.context().create();

				// CALL ALL LISTENERS BEFORE FIELD WRITE CALLBACKS
				synchronized (listeners) {
					for (SchemaFieldListener listener : listeners) {
						value = listener.onBeforeFieldWrite(iObject, this, value);
					}
				}

				setValueFinal(iObject, value);
			} finally {
				// ASSURE TO CALL THE AFTER FIELD WRITE CALLBACKS
				synchronized (listeners) {
					for (SchemaFieldListener listener : listeners) {
						value = listener.onAfterFieldWrite(iObject, this, value);
					}
				}
			}
		} catch (Exception e) {
			if (e.getCause() instanceof FieldErrorUserException) {
				throw (FieldErrorUserException) e.getCause();
			} else {
				log.error("[SchemaHelper.setFieldValue] Error on setting value '" + iFieldValue + "' for field '" + name + "' on object " + iObject, e);
				throw new BindingException(iObject, name, e);
			}
		} finally {

			Roma.context().destroy();
		}
	}

	/**
	 * Return field's class information
	 * 
	 * @return SchemaClassReflection if found, otherwise null
	 */
	public SchemaClass getClassInfo() {
		SchemaClassDefinition def = getType();
		if (def != null)
			return def.getSchemaClass();
		return null;
	}

	@Override
	public String toString() {
		return super.toString() + name;
	}

	public SchemaClassDefinition getType() {
		if (type == null)
			type = getSchemaClassFromLanguageType();
		if (entity instanceof SchemaObject && !(type instanceof SchemaObject)) {
			Object value = getValue(((SchemaObject) entity).getInstance());
			if (value != null)
				setType(Roma.session().getSchemaObject(value));
			else
				setType(Roma.session().getSchemaObject(type));
		}
		return type;
	}

	public void setType(SchemaClassDefinition iType) {
		this.type = iType;
	}

	public XmlFieldAnnotation getDescriptorInfo() {
		SchemaConfiguration classDescriptor = entity.getSchemaClass().getDescriptor();

		if (classDescriptor == null || classDescriptor.getType() == null)
			return null;
		return classDescriptor.getType().getField(name);
	}

	public SchemaEvent getEvent(String iEventName) {
		return events.get(iEventName);
	}

	public Iterator<SchemaEvent> getEventIterator() {
		return events.values().iterator();
	}

	public void setEvent(String iEventName, SchemaEvent iEvent) {
		events.put(iEventName, iEvent);
	}

	public void setEvent(SchemaEvent iEvent) {
		setEvent(iEvent.getName(), iEvent);
	}

	public Type getEmbeddedLanguageType() {
		SchemaClass cls = getFeature(CoreFieldFeatures.EMBEDDED_TYPE);
		if (cls != null)
			return (Type) cls.getLanguageType();

		return null;
	}

	public void setEmbeddedLanguageType(Type iEmbeddedType) {
		setEmbeddedType(Roma.schema().getSchemaClassIfExist((Class<?>) iEmbeddedType));
	}

	public SchemaClass getEmbeddedType() {
		return getFeature(CoreFieldFeatures.EMBEDDED_TYPE);
	}

	public void setEmbeddedType(SchemaClass iEmbeddedSchemaClass) {
		setFeature(CoreFieldFeatures.EMBEDDED_TYPE, iEmbeddedSchemaClass);
	}

	public SchemaClass[] getEmbeddedTypeGenerics() {
		return embeddedTypeGenerics;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		SchemaField copy = (SchemaField) super.clone();
		copy.events = new HashMap<String, SchemaEvent>();
		for (Map.Entry<String, SchemaEvent> entry : events.entrySet()) {
			copy.events.put(entry.getKey(), (SchemaEvent) entry.getValue().clone());
		}
		return copy;
	}


	protected Object invokeCallbackBeforeFieldRead(List<SchemaFieldListener> listeners, Object iObject) {
		Object value = SchemaFieldListener.IGNORED;

		synchronized (listeners) {
			Object callbackReturn;
			for (SchemaFieldListener listener : listeners) {
				callbackReturn = listener.onBeforeFieldRead(iObject, this, value);

				if (callbackReturn != SchemaFieldListener.IGNORED)
					value = callbackReturn;
			}
		}
		return value;
	}

	protected Object invokeCallbackAfterFieldRead(List<SchemaFieldListener> listeners, Object iObject, Object value) {
		synchronized (listeners) {
			for (SchemaFieldListener listener : listeners) {
				value = listener.onAfterFieldRead(iObject, this, value);
			}
		}
		return value;
	}

	public void setEmbeddedTypeGenerics(SchemaClass[] embeddedTypeGenerics) {
		this.embeddedTypeGenerics = embeddedTypeGenerics;
	}
}
