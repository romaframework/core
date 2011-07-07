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

import java.security.NoSuchAlgorithmException;
import java.util.Map;

import org.romaframework.core.aspect.Aspect;

/**
 * Authentication Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface AuthenticationAspect extends Aspect {
	public static final String	ASPECT_NAME	= "authentication";

	public Object authenticate(Object iContext, String iUserName, String iPassword, Map<String, String> iAdditionalInfo) throws AuthenticationException;

	public boolean allow(Object iProfile, String iFunctionName);

	public Object getCurrentAccount();

	public Object getCurrentRealm();

	public Object getCurrentProfile();

	public String encryptPassword(String iPassword) throws NoSuchAlgorithmException;

	public void logout() throws AuthenticationException;
}
