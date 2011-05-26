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
package org.romaframework.aspect.enterprise.feature;

import org.romaframework.core.util.DynaBean;

public class EnterpriseClassFeatures extends DynaBean {
	

	private static final long	serialVersionUID	= 1L;
	
	public EnterpriseClassFeatures()  {
		super();
		
		defineAttribute(ESBHOST,"");
		defineAttribute(ESBPORT,"");
		defineAttribute(USERNAME,"");		
		defineAttribute(PASSW,"");
		defineAttribute(BCADDRESS,"");
		defineAttribute(WSDLADDRESS,"");
		
		
	}

	
	public static final String ESBHOST = "esbHost";
	public static final String ESBPORT = "esbPort";
	public static final String USERNAME="username";
	public static final String PASSW="password";
	public static final String BCADDRESS="consumerAddress";
	public static final String WSDLADDRESS="wsdlAddress";
	
	
}
