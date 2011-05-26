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
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.util.DynaBean;

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
		DynaBean features = iClass.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new EnterpriseClassFeatures();
			iClass.setFeatures(ASPECT_NAME, features);
		}

		readClassAnnotation(iAnnotation, features);

	}

	private void readClassAnnotation(Annotation iAnnotation, DynaBean features) {
		if (iAnnotation instanceof EnterpriseClass) {
			EnterpriseClass annotation = (EnterpriseClass) iAnnotation;

			if (annotation != null) {
				// PROCESS ANNOTATIONS
				features.setAttribute(EnterpriseClassFeatures.ESBHOST, annotation.esbHost());
				features.setAttribute(EnterpriseClassFeatures.ESBPORT, annotation.esbPort());
				features.setAttribute(EnterpriseClassFeatures.USERNAME, annotation.username());
				features.setAttribute(EnterpriseClassFeatures.PASSW, annotation.password());
				features.setAttribute(EnterpriseClassFeatures.BCADDRESS, annotation.consumerAddress());
				features.setAttribute(EnterpriseClassFeatures.WSDLADDRESS, annotation.wsdlAddress());

			}
		} else if (iAnnotation instanceof BpelClass) {
			BpelClass annotation = (BpelClass) iAnnotation;
			if (annotation != null) {

				features.setAttribute(BpelClassFeatures.PROJECTPATH, annotation.projectPath());
				features.setAttribute(BpelClassFeatures.OPERATIONNAME, annotation.operationName());
				features.setAttribute(BpelClassFeatures.BCADDRESS, annotation.consumerAddress());
				features.setAttribute(BpelClassFeatures.WSDLADDRESS, annotation.wsdlAddress());
			}
		} else if (iAnnotation instanceof ConsumerRegistrationClass) {
			ConsumerRegistrationClass annotation = (ConsumerRegistrationClass) iAnnotation;
			if (annotation != null) {

				features.setAttribute(ConsumerRegistrationClassFeatures.REG_URI, annotation.registryURI());
				features.setAttribute(ConsumerRegistrationClassFeatures.USERNAME, annotation.username());
				features.setAttribute(ConsumerRegistrationClassFeatures.PASSW, annotation.password());
				features.setAttribute(ConsumerRegistrationClassFeatures.AUTHOR, annotation.author());
				features.setAttribute(ConsumerRegistrationClassFeatures.ORGANIZATION, annotation.organizationPackage());
				features.setAttribute(ConsumerRegistrationClassFeatures.SERVICE_DESC, annotation.serviceDesc());
				features.setAttribute(ConsumerRegistrationClassFeatures.SERVICE_NAME, annotation.serviceName());
			}
		}

	}

}
