/*
 * Copyright 2009 Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
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
package org.romaframework.aspect.serialization;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.romaframework.core.schema.SchemaFeatures;
import org.romaframework.core.util.DynaBean;


/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class SerializationData extends SchemaFeatures {

	private static final long	serialVersionUID	= -9063141913291614417L;
	
	private String	name;
	private Object simpleValue;
	private List<SerializationData> collection;
	private List<SerializationElement> fields = new ArrayList<SerializationElement>();
	private List<SerializationElement> actions = new ArrayList<SerializationElement>();
	private List<SerializationElement> events = new ArrayList<SerializationElement>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getSimpleValue() {
		return simpleValue;
	}

	public void setSimpleValue(Object simpleValue) {
		this.simpleValue = simpleValue;
	}

	public List<SerializationElement> getFields() {
		return fields;
	}

	public void setFields(List<SerializationElement> fields) {
		this.fields = fields;
	}
	
	public List<SerializationElement> getActions() {
		return actions;
	}

	public void setActions(List<SerializationElement> actions) {
		this.actions = actions;
	}

	public List<SerializationElement> getEvents() {
		return events;
	}

	public void setEvents(List<SerializationElement> events) {
		this.events = events;
	}

	public List<SerializationData> getCollection() {
		return collection;
	}

	public void setCollection(List<SerializationData> collection) {
		this.collection = collection;
	}

	public boolean containCollection(){
		return collection != null;
	}
	
	public boolean containSimpleValue(){
		return simpleValue!=null;
	}
	
  public void setAllFeatures(Map<String, DynaBean> allFeatures) {
    this.allFeatures=allFeatures;
  }
  
  @Override
  public String toString() {
  	return getName() +" :" +(containCollection()?"Collection":containSimpleValue()?"simpleValue":"object");
  }
}
