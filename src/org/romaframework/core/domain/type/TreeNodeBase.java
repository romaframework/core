package org.romaframework.core.domain.type;

import java.io.Serializable;

import org.romaframework.core.config.Destroyable;

public abstract class TreeNodeBase implements TreeNode, Cloneable, Serializable, Destroyable {
	private static final long	serialVersionUID	= 3842339758826007833L;

	protected String					name;
	protected TreeNode				parent;

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		if (name != null)
			buffer.append(name);

		if (getChildren() != null) {
			buffer.append("{");
			int counter = 0;
			for (TreeNode child : getChildren()) {
				if (counter > 0)
					buffer.append(", ");

				if (child != null)
					buffer.append(child);

				counter++;
			}
			buffer.append("}");
		}

		return buffer.toString();
	}

	// @ViewField(visible = AnnotationConstants.FALSE)
	public TreeNode getParent() {
		return parent;
	}

	public void setParent(TreeNode iParent) {
		this.parent = iParent;
	}

	public String getName() {
		return name;
	}

	public void setName(String iName) {
		this.name = iName;
	}

	// public void onChildrenAdd() {
	// CRUDHelper.show((Class) getClass(), this, "children");
	// }

	/**
	 * Return a String containing the path of the node.
	 * 
	 * @return The string representation of the node.
	 */
	// @ViewField(visible = AnnotationConstants.FALSE)
	public String getPath() {
		return TreeNodeHelper.getPath(this);
	}

	/**
	 * Find a child node following a path. Paths can be expressed using a XPath-like language. / is the separator for tree nodes:
	 * <ul>
	 * <li><b>no prefix</b> = browse all tree (ie: log)</li>
	 * <li><b>/</b> = child (ie: /root/log)</li>
	 * <li><b>../</b> = parent (ie: ../log)</li>
	 * <li><b>./</b> = current (ie: ./log)</li>
	 * </ul>
	 * 
	 * @param iChildPath
	 * @return TreeNode instance if found, otherwise null
	 */
	public TreeNode searchNode(String iChildPath) {
		return TreeNodeHelper.searchNode(this, iChildPath);
	}

	public TreeNode findChild(String iChildPath) {
		return TreeNodeHelper.findChild(this, iChildPath);
	}
}
