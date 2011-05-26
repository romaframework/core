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
 * WITHOUTreeNodeMap WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.romaframework.core.domain.type;

import java.util.Collection;
import java.util.Map;

/**
 * Generic TreeNode implementation that uses Map type for children. Children property is lazy instantiated: first time a child is
 * added a Map implementation is created in background. This class is abstract, so use implementations.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @see org.romaframework.module.admin.domain.tree.TreeNodeList
 */
public abstract class TreeNodeMap extends TreeNodeBase {
	private static final long						serialVersionUID	= -7646086691811657444L;
	protected Map<String, TreeNodeMap>	childrenMap;

	public TreeNodeMap(String iName) {
		name = iName;
	}

	public TreeNodeMap(TreeNodeMap iParent, String iName) {
		parent = iParent;
		name = iName;
	}

	public Map<String, TreeNodeMap> getChildrenMap() {
		return childrenMap;
	}

	public void setChildrenMap(Map<String, TreeNodeMap> childrenMap) {
		this.childrenMap = childrenMap;
	}

	protected abstract Map<String, TreeNodeMap> createMap();

	/**
	 * Destroy children recursively
	 */
	// @ViewField(visible = AnnotationConstants.FALSE)
	public void destroy() {
		if (childrenMap != null) {
			// CALL DESTROY RECURSIVELY
			for (TreeNodeMap v : childrenMap.values()) {
				v.destroy();
			}
			childrenMap.clear();
		}
		childrenMap = null;
		parent = null;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		TreeNodeMap cloned = (TreeNodeMap) super.clone();

		cloned.parent = (TreeNodeMap) ((TreeNodeMap) parent).clone();

		// CLONE CHILDREN RECURSIVELY
		cloned.childrenMap = createMap();
		for (Map.Entry<String, TreeNodeMap> entry : childrenMap.entrySet()) {
			cloned.childrenMap.put(entry.getKey(), (TreeNodeMap) entry.getValue().clone());
		}
		return cloned;
	}

	public TreeNodeMap getChild(String iName) {
		if (childrenMap == null) {
			return null;
		}

		return childrenMap.get(iName);
	}

	// @ViewField(visible = AnnotationConstants.FALSE)
	public Collection<? extends TreeNode> getChildren() {
		if (childrenMap == null) {
			return null;
		}

		return childrenMap.values();
	}

	public synchronized TreeNodeMap addChild(TreeNodeMap iChild) {
		return addChild(iChild.getName(), iChild);
	}

	public synchronized void addChild(TreeNode iChild) {
		addChild(iChild.getName(), (TreeNodeMap) iChild);
	}

	public synchronized TreeNodeMap addChild(String iName, TreeNodeMap iChild) {
		if (childrenMap == null) {
			synchronized (getClass()) {
				if (childrenMap == null) {
					childrenMap = createMap();
				}
			}
		}

		childrenMap.put(iName, iChild);
		iChild.setParent(this);

		return iChild;
	}

	public boolean removeChild(TreeNode name) {
		synchronized (getClass()) {
			if (childrenMap == null) {
				return false;
			} else {
				boolean result = childrenMap.get(name.getName()) != null;
				childrenMap.remove(name.getName());
				return result;
			}
		}
	}
}
