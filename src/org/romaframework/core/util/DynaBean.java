/*
 * Copyright 20062-007 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.util;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Dynamic Bean. Acts in two ways: <li>Dynamic mode, you can get/set attributes without define them before. It works like an
 * HashMap.</li> <li>Static mode, you had to define the attributes before to get/set them.</li>
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class DynaBean implements Cloneable, Serializable {

	private static final long					serialVersionUID	= 2801912719184911296L;

	private final Map<String, Object>	properties;
	private final boolean							staticMode;
	private DynaBean									parent;

	private static Log								log								= LogFactory.getLog(DynaBean.class);

	public DynaBean() {
		this(true);
	}

	public DynaBean(boolean iStaticMode) {
		staticMode = iStaticMode;
		properties = new HashMap<String, Object>();
	}

	/**
	 * Define a new attribute with the default value
	 * 
	 * @param iName
	 *          Attribute name
	 * @param iDefaultValue
	 *          Default attribute value
	 */
	public void defineAttribute(String iName, Object iDefaultValue) {
		if (staticMode && properties.containsKey(iName))
			throw new DynaBeanException("Attribute " + iName + " is already defined for object " + getClass().getSimpleName());
		properties.put(iName, iDefaultValue);
	}

	public boolean setAttribute(String iName, Object iNewValue) {
		// GET CURRENT VALUE
		Object oldValue = getAttribute(iName);

		if (iNewValue == null && oldValue == null)
			// NO CHANGES
			return false;
		if (iNewValue != null && oldValue != null)
			if (iNewValue.equals(oldValue))
				// NO CHANGES
				return false;

		// STORE ALWAYS LOCALLY
		properties.put(iName, iNewValue);

		if (log.isDebugEnabled())
			log.debug("[DynaBean.setAttribute] " + iName + " = " + iNewValue);

		return true;
	}

	/**
	 * Search the owner of the attribute and then return it. If the attribute is not found, a DynaBeanException is thrown if static
	 * mode is true (by default).
	 * 
	 * @return attribute if found
	 * @exception DynaBeanException
	 *              if no attribute was found and static mode is true (by default)
	 */
	public Object getAttribute(String iName) {
		return getAttributeOwner(iName, true).properties.get(iName);
	}

	public boolean existAttribute(String iName) {
		DynaBean owner = getAttributeOwner(iName, false);
		return owner != null && owner.properties.containsKey(iName);
	}

	public boolean isAttributeInherited(String iName) {
		DynaBean owner = getAttributeOwner(iName, true);
		return owner != this;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		DynaBean cloned = new DynaBean(staticMode);
		cloned.parent = this;
		return cloned;
	}

	public Map<String, Object> getAttributes() {
		return populatetAttributes(new HashMap<String, Object>());
	}

	private Map<String, Object> populatetAttributes(Map<String, Object> iProperties) {
		if (parent != null)
			parent.populatetAttributes(iProperties);

		iProperties.putAll(properties);

		return iProperties;
	}

	@Override
	public String toString() {
		return getAttributes().toString();
	}

	private DynaBean getAttributeOwner(String iName, boolean iErrorNotFound) {
		DynaBean current = this;
		while (current != null && !current.properties.containsKey(iName))
			current = current.parent;

		if (iErrorNotFound && current == null && staticMode) {
			String msg = "Attribute " + iName + " not defined for object " + getClass().getSimpleName();
			log.error("[DynaBean.checkIfExists] Error: " + msg);
			throw new DynaBeanException(msg);
		}

		return current;
	}

	public DynaBean getParent() {
		return parent;
	}
}
