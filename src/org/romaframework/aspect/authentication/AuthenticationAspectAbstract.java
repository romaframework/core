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

package org.romaframework.aspect.authentication;

import java.lang.annotation.Annotation;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaClassResolver;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

/**
 * Base abstract class implementing Authentication Aspect interface. Encryption uses the well know MD5 algorithm.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public abstract class AuthenticationAspectAbstract extends SelfRegistrantConfigurableModule<String> implements AuthenticationAspect {

	public static final String			DEF_ALGORITHM				= "MD5";
	private sun.misc.BASE64Encoder	encoder							= new sun.misc.BASE64Encoder();
	private String									encryptionAlgorithm	= DEF_ALGORITHM;

	public String encryptPassword(final String iPassword) throws NoSuchAlgorithmException {
		return encryptPasswordInBytes(getEncryptionAlgorithm(), iPassword);
	}

	public String encryptPasswordInBytes(final String iAlgorithm, final String iPassword) throws NoSuchAlgorithmException {
		if (iAlgorithm == null || iPassword == null)
			return iPassword;

		final MessageDigest md = MessageDigest.getInstance(iAlgorithm);
		md.update(iPassword.getBytes());
		return encoder.encode(md.digest());
	}

	public Object getCurrentAccount() {
		final SessionInfo sess = Roma.session().getActiveSessionInfo();
		if (sess == null)
			return null;
		return sess.getAccount();
	}

	/**
	 * Return the current encryption algorithm used, null if encryption is disabled.
	 * 
	 * @return
	 */
	public String getEncryptionAlgorithm() {
		return encryptionAlgorithm;
	}

	/**
	 * Change the default encryption algorithm. Set it to null in order to disable encryption.
	 * 
	 * @param algorithm
	 *          algorithm name or null to disable encryption
	 */
	public void setEncryptionAlgorithm(final String algorithm) {
		this.encryptionAlgorithm = algorithm;
	}

	@Override
	public void startup() {
		// REGISTER THE VIEW DOMAIN TO SCHEMA CLASS RESOLVER
		Roma.component(SchemaClassResolver.class).addDomainPackage(Utility.getApplicationAspectPackage(aspectName()));

		super.startup();
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configAction(final SchemaClassElement action, final Annotation actionAnnotation, final Annotation genericAnnotation,
			XmlActionAnnotation node) {
	}

	public void configClass(final SchemaClassDefinition class1, final Annotation annotation, final XmlClassAnnotation node) {
	}

	public void configEvent(final SchemaEvent event, final Annotation eventAnnotation, final Annotation genericAnnotation,
			final XmlEventAnnotation node) {
	}

	public void configField(SchemaField field, Annotation fieldAnnotation, Annotation genericAnnotation, Annotation getterAnnotation,
			final XmlFieldAnnotation node) {
	}

	public Object getUnderlyingComponent() {
		return null;
	}

	public String aspectName() {
		return ASPECT_NAME;
	}
}
