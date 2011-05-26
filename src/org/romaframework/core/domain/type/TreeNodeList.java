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
 * WITHOUTreeNodeList WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.romaframework.core.domain.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Generic TreeNode implementation that uses List type for children. Children property is lazy instantiated: first time a child is
 * added an ArrayList is created in background.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * @see org.romaframework.module.admin.domain.tree.TreeNodeMap
 */
public class TreeNodeList extends TreeNodeBase {
	private static final long			serialVersionUID	= -2325896501850979902L;

	protected List<TreeNodeList>	children;

	public TreeNodeList(String iName) {
		name = iName;
	}

	public TreeNodeList(TreeNodeList iParent, String iName) {
		parent = iParent;
		name = iName;
	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		TreeNodeList cloned = (TreeNodeList) super.clone();

		cloned.parent = (TreeNodeList) ((TreeNodeList) parent).clone();

		// CLONE CHILDREN RECURSIVELY
		cloned.children = new ArrayList<TreeNodeList>();
		for (TreeNodeList entry : children) {
			cloned.children.add((TreeNodeList) entry.clone());
		}
		return cloned;
	}

	/**
	 * Destroy children recursively
	 */
	// @ViewAction(visible = AnnotationConstants.FALSE)
	public void destroy() {
		if (children != null) {
			// CALL DESTROY RECURSIVELY
			for (TreeNodeList v : children) {
				v.destroy();
			}
			children.clear();
		}
		children = null;
		parent = null;
	}

	public TreeNodeList getChild(String iName) {
		if (children == null)
			return null;

		for (TreeNodeList v : children) {
			if (v.getName().equals(iName))
				return v;
		}
		return null;
	}

	// @ViewField(visible = AnnotationConstants.FALSE)
	public Collection<? extends TreeNode> getChildren() {
		return children;
	}

	public TreeNodeList addChild(TreeNodeList iChild) {
		if (children == null)
			synchronized (getClass()) {
				if (children == null) {
					children = new ArrayList<TreeNodeList>(5);
				}
			}

		children.add(iChild);
		iChild.setParent(this);

		return iChild;
	}

	public void addChild(TreeNode iChild) {
		addChild((TreeNodeList) iChild);
	}

	public boolean removeChild(TreeNode child) {
		synchronized (getClass()) {
			if (children == null) {
				return false;
			} else {
				return children.remove(child);
			}
		}
	}

}
