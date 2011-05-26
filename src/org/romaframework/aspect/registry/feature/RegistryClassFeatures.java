/*
 * Copyright 2009 Luca Acquaviva (lacquaviva:at:imolinfo.it)
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
package org.romaframework.aspect.registry.feature;

import org.romaframework.core.util.DynaBean;

public class RegistryClassFeatures extends DynaBean {

	private static final long	serialVersionUID	= 1L;

	public RegistryClassFeatures() {
		super();

		defineAttribute(SERVICE_DESC, "");
		defineAttribute(REG_URI, "");
		defineAttribute(USERNAME, "");
		defineAttribute(PASSW, "");
		defineAttribute(ORGANIZATION, "");
		defineAttribute(AUTHOR, "");
		defineAttribute(WSDLADDRESS, "");
		defineAttribute(ICONPATH, "");
	}

	public static final String	SERVICE_DESC	= "serviceDesc";
	public static final String	REG_URI				= "registryURI";
	public static final String	USERNAME			= "username";
	public static final String	PASSW					= "password";
	public static final String	ORGANIZATION	= "organization";
	public static final String	AUTHOR				= "author";
	public static final String	WSDLADDRESS		= "wsdlAddress";
	public static final String  ICONPATH		  = "iconpath";
}
