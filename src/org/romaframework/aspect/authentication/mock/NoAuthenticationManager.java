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

package org.romaframework.aspect.authentication.mock;

import java.util.Map;

import org.romaframework.aspect.authentication.AuthenticationAspectAbstract;
import org.romaframework.aspect.authentication.AuthenticationException;

/**
 * No authentication management: the login returns always true.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class NoAuthenticationManager extends AuthenticationAspectAbstract {

	public Object authenticate(Object iContext, String iUserName, String iPassword, Map<String, String> iAdditionalInfo) {
		return null;
	}

	public boolean allow(Object iAccount, String iFunctionName) {
		return true;
	}

	public void logout() throws AuthenticationException {
	}

	@Override
	public String getEncryptionAlgorithm() {
		return null;
	}

	public Object getCurrentProfile() {
		return null;
	}

	public Object getCurrentRealm() {
		return null;
	}
}
