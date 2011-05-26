package org.romaframework.core.domain.type;

import java.util.Collection;

public interface TreeNode {
	public String getName();

	public String getPath();

	public TreeNode getParent();

	public TreeNode getChild(String iName);
	
	public boolean removeChild(TreeNode iChild);

	public Collection<? extends TreeNode> getChildren();
	
	public void addChild( TreeNode child);
}
