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
package org.romaframework.core.domain.type;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of TreeNodeMap<T> class using HashMap collection.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @see org.romaframework.module.admin.domain.tree.TreeNodeMap
 * @see java.util.HashMap
 */
public class TreeNodeHashMap extends TreeNodeMap {

	private static final long	serialVersionUID	= -7378132304192560416L;

	public TreeNodeHashMap(TreeNodeHashMap iParent, String iName) {
		super(iParent, iName);
	}

	@Override
	protected Map<String, TreeNodeMap> createMap() {
		return new HashMap<String, TreeNodeMap>(5);
	}
}
