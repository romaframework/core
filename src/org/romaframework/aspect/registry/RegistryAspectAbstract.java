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

import java.lang.annotation.Annotation;

import org.romaframework.aspect.registry.annotation.RegistryClass;
import org.romaframework.aspect.registry.feature.RegistryClassFeatures;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.util.DynaBean;

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

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {
		DynaBean features = iClass.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new RegistryClassFeatures();
			iClass.setFeatures(ASPECT_NAME, features);
		}

		readClassAnnotation(iAnnotation, features);
	}

	private void readClassAnnotation(Annotation iAnnotation, DynaBean features) {
		RegistryClass annotation = (RegistryClass) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			features.setAttribute(RegistryClassFeatures.REG_URI, annotation.registryURI());
			features.setAttribute(RegistryClassFeatures.USERNAME, annotation.username());
			features.setAttribute(RegistryClassFeatures.PASSW, annotation.password());
			features.setAttribute(RegistryClassFeatures.SERVICE_DESC, annotation.serviceDesc());
			features.setAttribute(RegistryClassFeatures.ORGANIZATION, annotation.organizationPackage());
			features.setAttribute(RegistryClassFeatures.AUTHOR, annotation.author());
			features.setAttribute(RegistryClassFeatures.WSDLADDRESS, annotation.wsdlAddress());
			features.setAttribute(RegistryClassFeatures.ICONPATH, annotation.iconPath());

		}
	}
}
