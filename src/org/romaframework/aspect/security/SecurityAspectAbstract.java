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

import java.lang.annotation.Annotation;

import org.romaframework.aspect.authentication.UserObjectPermissionListener;
import org.romaframework.aspect.security.annotation.SecurityAction;
import org.romaframework.aspect.security.annotation.SecurityClass;
import org.romaframework.aspect.security.annotation.SecurityEvent;
import org.romaframework.aspect.security.annotation.SecurityField;
import org.romaframework.aspect.security.feature.SecurityActionFeatures;
import org.romaframework.aspect.security.feature.SecurityClassFeatures;
import org.romaframework.aspect.security.feature.SecurityEventFeatures;
import org.romaframework.aspect.security.feature.SecurityFieldFeatures;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.flow.UserObjectEventListener;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

public abstract class SecurityAspectAbstract extends SelfRegistrantConfigurableModule<String> implements SecurityAspect,
		UserObjectEventListener, UserObjectPermissionListener {

	@Override
	public void startup() {
		Controller.getInstance().registerListener(UserObjectEventListener.class, this);
		Controller.getInstance().registerListener(UserObjectPermissionListener.class, this);
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {

		DynaBean features = iClass.getFeatures(SecurityAspect.ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new SecurityClassFeatures();
			iClass.setFeatures(SecurityAspect.ASPECT_NAME, features);
		}

		readClassAnnotation(iAnnotation, features);
		readClassXml(iClass, iXmlNode);
	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation,
			Annotation iGetterAnnotation, XmlFieldAnnotation iXmlNode) {
		DynaBean features = iField.getFeatures(SecurityAspect.ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new SecurityFieldFeatures();
			iField.setFeatures(SecurityAspect.ASPECT_NAME, features);
		}

		readFieldAnnotation(iFieldAnnotation, features);
		readFieldAnnotation(iGetterAnnotation, features);
		readFieldXml(iField, iXmlNode);
	}

	public void configEvent(SchemaEvent iEvent, Annotation iEventAnnotation, Annotation iGenericAnnotation, XmlEventAnnotation iNode) {
		DynaBean features = iEvent.getFeatures(SecurityAspect.ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new SecurityActionFeatures();
			iEvent.setFeatures(SecurityAspect.ASPECT_NAME, features);
		}

		readEventAnnotation(iEvent, iEventAnnotation, features);
		readEventXml(iEvent, iNode);

	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iNode) {
		DynaBean features = iAction.getFeatures(SecurityAspect.ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new SecurityActionFeatures();
			iAction.setFeatures(SecurityAspect.ASPECT_NAME, features);
		}

		readActionAnnotation(iAction, iActionAnnotation, features);
		readActionXml(iAction, iNode);

	}

	private void readActionAnnotation(SchemaClassElement iAction, Annotation iAnnotation, DynaBean features) {
		SecurityAction annotation = (SecurityAction) iAnnotation;

		// PROCESS ANNOTATIONS
		// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
		if (annotation != null)
			features.setAttribute(SecurityActionFeatures.ROLES, annotation.roles());
	}

	private void readEventAnnotation(SchemaClassElement iEvent, Annotation iAnnotation, DynaBean features) {
		SecurityEvent annotation = (SecurityEvent) iAnnotation;

		// PROCESS ANNOTATIONS
		// ANNOTATION ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT VALUES
		if (annotation != null)
			features.setAttribute(SecurityEventFeatures.ROLES, annotation.roles());
	}

	private void readActionXml(SchemaClassElement iAction, XmlActionAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null)
			return;

		DynaBean features = iAction.getFeatures(ASPECT_NAME);

		if (iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			String roles = descriptor.getAttribute(SecurityActionFeatures.ROLES);
			if (roles != null) {
				features.setAttribute(SecurityActionFeatures.ROLES, roles.split(" "));
			}
		}
	}

	private void readEventXml(SchemaClassElement iAction, XmlActionAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null)
			return;

		DynaBean features = iAction.getFeatures(ASPECT_NAME);

		if (iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation descriptor = iXmlNode.aspect(ASPECT_NAME);

		if (descriptor != null) {
			String roles = descriptor.getAttribute(SecurityEventFeatures.ROLES);
			if (roles != null) {
				features.setAttribute(SecurityEventFeatures.ROLES, roles.split(" "));
			}
		}
	}

	public String aspectName() {
		return ASPECT_NAME;
	}

	private void readClassAnnotation(Annotation iAnnotation, DynaBean features) {
		SecurityClass annotation = (SecurityClass) iAnnotation;
		if (annotation != null) {
			// PROCESS ANNOTATIONS
			features.setAttribute(SecurityClassFeatures.ENCRYPT, annotation.encrypt());
			features.setAttribute(SecurityClassFeatures.ENCRYPTION_ALGORITHM, annotation.encryptionAlgorithm());
			features.setAttribute(SecurityClassFeatures.READ_ROLES, annotation.readRoles());
			features.setAttribute(SecurityClassFeatures.WRITE_ROLES, annotation.writeRoles());
			features.setAttribute(SecurityClassFeatures.EXECUTE_ROLES, annotation.executeRoles());
		}
	}

	private void readClassXml(SchemaClassDefinition iClass, XmlClassAnnotation iXmlNode) {
		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		DynaBean features = iClass.getFeatures(SecurityAspect.ASPECT_NAME);

		XmlAspectAnnotation featureDescriptor = iXmlNode.aspect(ASPECT_NAME);

		if (featureDescriptor != null) {

			// PROCESS DESCRIPTOR CFG
			if (featureDescriptor != null) {
				String encrypt = featureDescriptor.getAttribute(SecurityClassFeatures.ENCRYPT);
				if (encrypt != null) {
					features.setAttribute(SecurityClassFeatures.ENCRYPT, new Boolean(encrypt));
				}
				String algorithm = featureDescriptor.getAttribute(SecurityClassFeatures.ENCRYPTION_ALGORITHM);
				if (algorithm != null) {
					features.setAttribute(SecurityClassFeatures.ENCRYPTION_ALGORITHM, algorithm);
				}
				String readRoles = featureDescriptor.getAttribute(SecurityClassFeatures.READ_ROLES);
				if (readRoles != null) {
					features.setAttribute(SecurityClassFeatures.READ_ROLES, readRoles.split(" "));
				}
				String writeRoles = featureDescriptor.getAttribute(SecurityClassFeatures.WRITE_ROLES);
				if (writeRoles != null) {
					features.setAttribute(SecurityClassFeatures.WRITE_ROLES, writeRoles.split(" "));
				}
				String executeRoles = featureDescriptor.getAttribute(SecurityClassFeatures.EXECUTE_ROLES);
				if (executeRoles != null) {
					features.setAttribute(SecurityClassFeatures.EXECUTE_ROLES, executeRoles.split(" "));
				}
			}
		}
	}

	private void readFieldAnnotation(Annotation iAnnotation, DynaBean iFeatures) {
		SecurityField annotation = (SecurityField) iAnnotation;

		if (annotation != null) {
			iFeatures.setAttribute(SecurityFieldFeatures.ENCRYPT, annotation.encrypt());
			iFeatures.setAttribute(SecurityFieldFeatures.ENCRYPTION_ALGORITHM, annotation.encryptionAlgorithm());
			iFeatures.setAttribute(SecurityFieldFeatures.READ_ROLES, annotation.readRoles());
			iFeatures.setAttribute(SecurityFieldFeatures.WRITE_ROLES, annotation.writeRoles());
		}

	}

	private void readFieldXml(SchemaField iField, XmlFieldAnnotation iXmlNode) {
		// PROCESS DESCRIPTOR CFG
		// DESCRIPTOR ATTRIBUTES (IF DEFINED) OVERWRITE DEFAULT AND ANNOTATION
		// VALUES
		if (iXmlNode == null)
			return;

		DynaBean features = iField.getFeatures(ASPECT_NAME);

		if (iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation featureDescriptor = iXmlNode.aspect(ASPECT_NAME);

		if (featureDescriptor != null) {
			// PROCESS DESCRIPTOR CFG
			if (featureDescriptor != null) {
				String encrypt = featureDescriptor.getAttribute(SecurityFieldFeatures.ENCRYPT);
				if (encrypt != null) {
					features.setAttribute(SecurityFieldFeatures.ENCRYPT, Boolean.parseBoolean(encrypt));
				}
				String algorithm = featureDescriptor.getAttribute(SecurityFieldFeatures.ENCRYPTION_ALGORITHM);
				if (algorithm != null) {
					features.setAttribute(SecurityFieldFeatures.ENCRYPTION_ALGORITHM, algorithm);
				}
				String readRoles = featureDescriptor.getAttribute(SecurityFieldFeatures.READ_ROLES);
				if (readRoles != null) {
					features.setAttribute(SecurityFieldFeatures.READ_ROLES, readRoles.split(" "));
				}
				String writeRoles = featureDescriptor.getAttribute(SecurityFieldFeatures.WRITE_ROLES);
				if (writeRoles != null) {
					features.setAttribute(SecurityFieldFeatures.WRITE_ROLES, writeRoles.split(" "));
				}
			}
		}
	}

}
