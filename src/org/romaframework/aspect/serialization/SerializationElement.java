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

import org.romaframework.core.schema.SchemaFeatures;

/**
 * 
 * @author Emanuele Tagliaferri (emanuele.tagliaferri--at--assetdata.it)
 * 
 */
public class SerializationElement extends SchemaFeatures {

	private static final long						serialVersionUID	= 2474013569417995250L;

	private String											name;
	private List<SerializationElement>	events						= new ArrayList<SerializationElement>();

	private SerializationData						data;

	public SerializationElement() {
		super(null);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SerializationData getData() {
		return data;
	}

	public void setData(SerializationData data) {
		this.data = data;
	}

	public List<SerializationElement> getEvents() {
		return events;
	}

	public void setEvents(List<SerializationElement> events) {
		this.events = events;
	}

}
