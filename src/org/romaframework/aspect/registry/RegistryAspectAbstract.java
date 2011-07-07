/*
 * Copyright 2008 Luca Acquaviva (lacquaviva:at:imolinfo.it)
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
package org.romaframework.aspect.registry;

import org.romaframework.aspect.registry.annotation.RegistryClass;
import org.romaframework.aspect.registry.feature.RegistryClassFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.reflection.SchemaClassReflection;

public abstract class RegistryAspectAbstract extends SelfRegistrantConfigurableModule<String> implements RegistryAspect {

	public static final String	ASPECT_NAME	= "registry";

	public String aspectName() {
		return ASPECT_NAME;
	}

	@Override
	public void startup() {
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass) {

		if (iClass instanceof SchemaClassReflection) {
			Class<?> clazz = ((SchemaClassReflection) iClass).getLanguageType();
			RegistryClass annotation = clazz.getAnnotation(RegistryClass.class);

			if (annotation != null) {
				// PROCESS ANNOTATIONS
				iClass.setFeature(RegistryClassFeatures.REG_URI, annotation.registryURI());
				iClass.setFeature(RegistryClassFeatures.USERNAME, annotation.username());
				iClass.setFeature(RegistryClassFeatures.PASSW, annotation.password());
				iClass.setFeature(RegistryClassFeatures.SERVICE_DESC, annotation.serviceDesc());
				iClass.setFeature(RegistryClassFeatures.ORGANIZATION, annotation.organizationPackage());
				iClass.setFeature(RegistryClassFeatures.AUTHOR, annotation.author());
				iClass.setFeature(RegistryClassFeatures.WSDLADDRESS, annotation.wsdlAddress());
				iClass.setFeature(RegistryClassFeatures.ICONPATH, annotation.iconPath());

			}
		}
	}

}
