/*
 * Copyright 2006-2008 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.aspect.profiling;


import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaAction;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaEvent;

/**
 * Abstract implementation for Profiling Aspect.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class ProfilingAspectAbstract extends SelfRegistrantConfigurableModule<String> implements ProfilingAspect {

	public void configClass(SchemaClassDefinition iClass) {

	}

	public void configAction(SchemaAction iAction) {
	}

	public void configEvent(SchemaEvent iEvent) {
		configAction(iEvent);
	}

	public String aspectName() {
		return ASPECT_NAME;
	}
}
