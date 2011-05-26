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
package org.romaframework.aspect.enterprise;

import java.lang.annotation.Annotation;

import org.romaframework.aspect.enterprise.annotation.BpelClass;
import org.romaframework.aspect.enterprise.annotation.ConsumerRegistrationClass;
import org.romaframework.aspect.enterprise.annotation.EnterpriseClass;
import org.romaframework.aspect.enterprise.feature.BpelClassFeatures;
import org.romaframework.aspect.enterprise.feature.ConsumerRegistrationClassFeatures;
import org.romaframework.aspect.enterprise.feature.EnterpriseClassFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.reflection.SchemaClassReflection;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;

public abstract class EnterpriseAspectAbstract extends SelfRegistrantConfigurableModule<String> implements EnterpriseAspect {

	public static final String	ASPECT_NAME	= "enterprise";

	public String aspectName() {
		return ASPECT_NAME;
	}

	@Override
	public void startup() {
		// REGISTER THE VIEW DOMAIN TO SCHEMA CLASS RESOLVER
		Roma.component(SchemaClassResolver.class).addDomainPackage(Utility.getApplicationAspectPackage(aspectName()));
		Roma.component(SchemaClassResolver.class).addDomainPackage(Utility.getRomaAspectPackage(aspectName()));

	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {

		readClassAnnotation(iClass);

	}

	private void readClassAnnotation(SchemaClassDefinition iClass) {
		Class<?> realClass = null;
		if (iClass instanceof SchemaClassReflection) {
			realClass = ((SchemaClassReflection) iClass).getLanguageType();
		}
		if (realClass == null)
			return;
		EnterpriseClass annotation = realClass.getAnnotation(EnterpriseClass.class);

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			iClass.setFeature(EnterpriseClassFeatures.ESBHOST, annotation.esbHost());
			iClass.setFeature(EnterpriseClassFeatures.ESBPORT, Long.parseLong(annotation.esbPort()));
			iClass.setFeature(EnterpriseClassFeatures.USERNAME, annotation.username());
			iClass.setFeature(EnterpriseClassFeatures.PASSW, annotation.password());
			iClass.setFeature(EnterpriseClassFeatures.BCADDRESS, annotation.consumerAddress());
			iClass.setFeature(EnterpriseClassFeatures.WSDLADDRESS, annotation.wsdlAddress());

		}
		BpelClass bpleAnnotation = realClass.getAnnotation(BpelClass.class);
		if (bpleAnnotation != null) {

			iClass.setFeature(BpelClassFeatures.PROJECTPATH, bpleAnnotation.projectPath());
			iClass.setFeature(BpelClassFeatures.OPERATIONNAME, bpleAnnotation.operationName());
			iClass.setFeature(BpelClassFeatures.BCADDRESS, bpleAnnotation.consumerAddress());
			iClass.setFeature(BpelClassFeatures.WSDLADDRESS, bpleAnnotation.wsdlAddress());
		}
		ConsumerRegistrationClass ConsymerAnnotation = realClass.getAnnotation(ConsumerRegistrationClass.class);
		if (ConsymerAnnotation != null) {

			iClass.setFeature(ConsumerRegistrationClassFeatures.REG_URI, ConsymerAnnotation.registryURI());
			iClass.setFeature(ConsumerRegistrationClassFeatures.USERNAME, ConsymerAnnotation.username());
			iClass.setFeature(ConsumerRegistrationClassFeatures.PASSW, ConsymerAnnotation.password());
			iClass.setFeature(ConsumerRegistrationClassFeatures.AUTHOR, ConsymerAnnotation.author());
			iClass.setFeature(ConsumerRegistrationClassFeatures.ORGANIZATION, ConsymerAnnotation.organizationPackage());
			iClass.setFeature(ConsumerRegistrationClassFeatures.SERVICE_DESC, ConsymerAnnotation.serviceDesc());
			iClass.setFeature(ConsumerRegistrationClassFeatures.SERVICE_NAME, ConsymerAnnotation.serviceName());
		}

	}

}
