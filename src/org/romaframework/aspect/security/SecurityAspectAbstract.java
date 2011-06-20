/*
 * Copyright 2009 Luigi Dell'Aquila (luigi.dellaquila@assetdata.it)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.romaframework.aspect.security;


import org.romaframework.aspect.authentication.UserObjectPermissionListener;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.SchemaActionListener;
import org.romaframework.core.flow.SchemaFieldListener;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;

public abstract class SecurityAspectAbstract extends SelfRegistrantConfigurableModule<String> implements SecurityAspect, UserObjectPermissionListener,
		SchemaActionListener, SchemaFieldListener {

	public SecurityAspectAbstract() {
		Controller.getInstance().registerListener(SchemaFieldListener.class, this);
		Controller.getInstance().registerListener(SchemaActionListener.class, this);
		Controller.getInstance().registerListener(UserObjectPermissionListener.class, this);
	}
	
	
	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass) {

	}

	public void configField(SchemaField iField) {
	}

	public void configEvent(SchemaEvent iEvent) {

	}

	public void configAction(SchemaAction iAction) {

	}

	public String aspectName() {
		return ASPECT_NAME;
	}

}
