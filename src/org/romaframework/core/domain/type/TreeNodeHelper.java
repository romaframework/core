package org.romaframework.core.domain.type;

import java.util.Collection;
import java.util.LinkedList;

public class TreeNodeHelper {

	public static final String	NODE_SEPARATOR		= "/";
	public static final String	CURRENT_NODE			= ".";
	public static final String	CURRENT_POSITION	= "./";
	public static final String	PARENT_POSITION		= "../";
	public static final String	ANY_POSITION			= "//";

	public static String getPath(TreeNode iNode) {
		StringBuilder buffer = new StringBuilder();
		TreeNode node = iNode;
		while (node != null && node.getParent() != null) {
			buffer.insert(0, NODE_SEPARATOR);
			buffer.insert(1, node.getName());
			node = node.getParent();
		}
		return buffer.toString();
	}

	public static TreeNode searchNode(TreeNode iRoot, String iChildPath) {
		if (iChildPath == null)
			return null;

		if (iChildPath.equals(NODE_SEPARATOR))
			return getRootNode(iRoot);

		if (iChildPath.equals(CURRENT_NODE) || iChildPath.equals(CURRENT_POSITION))
			return iRoot;

		// CHECK FOR ANY POSITION
		int pos = iChildPath.indexOf(ANY_POSITION);
		if (pos > -1) {
			String nodeName = iChildPath.substring(pos + ANY_POSITION.length());
			if (iRoot.getName() != null && iRoot.getName().equals(nodeName))
				// RETURN MYSELF
				return iRoot;

			return findChildAllTree(iRoot, nodeName);
		}

		// CHECK FOR STARTING FROM ROOT
		if (iChildPath.startsWith(NODE_SEPARATOR)) {
			TreeNode current = getRootNode(iRoot);
			return findChild(current, iChildPath.substring(NODE_SEPARATOR.length()));
		}

		// CHECK FOR STARTING FROM CURRENT
		if (iChildPath.startsWith(CURRENT_POSITION)) {
			return findChild(iRoot, iChildPath.substring(CURRENT_POSITION.length()));
		}
		// CHECK FOR STARTING FROM PARENT
		if (iChildPath.startsWith(PARENT_POSITION)) {
			return findChild(iRoot.getParent(), iChildPath.substring(PARENT_POSITION.length()));
		}
		return findChild(iRoot, iChildPath);
	}

	public static TreeNode findChild(TreeNode iRoot, String iChildPath) {
		Collection<? extends TreeNode> children = iRoot.getChildren();

		if (children == null)
			return null;

		int pos = iChildPath.indexOf(NODE_SEPARATOR);
		if (pos == -1)
			return iRoot.getChild(iChildPath);

		// SEARCH CHILD RECURSIVELY
		TreeNode child = iRoot.getChild(iChildPath.substring(0, pos));

		if (child == null)
			return null;

		return findChild(child, iChildPath.substring(pos + 1));
	}

	public static TreeNode findChildAllTree(TreeNode iRoot, String iName) {
		Collection<? extends TreeNode> children = iRoot.getChildren();

		if (children == null)
			return null;

		TreeNode child = iRoot.getChild(iName);
		if (child != null)
			return child;

		// SEARCH CHILD RECURSIVELY
		for (TreeNode c : children) {
			child = findChildAllTree(c, iName);
			if (child != null)
				return child;
		}
		return null;
	}

	public static TreeNode getRootNode(TreeNode iNode) {
		TreeNode current = iNode;
		while (current.getParent() != null) {
			current = current.getParent();
		}
		return current;
	}

	public static TreeNode findChildByNumber(TreeNode node, Integer i) {
		if (node == null) {
			return null;
		}

		LinkedList<TreeNode> stack = new LinkedList<TreeNode>();
		stack.add(node);

		while (!stack.isEmpty()) {
			TreeNode currentNode = stack.removeFirst();

			if (i == 0) {
				return currentNode;
			}

			if (currentNode.getChildren() != null) {
				stack.addAll(0, currentNode.getChildren());
			}
			i = i - 1;
		}

		return null;
	}

	public static Integer findChildByNumber(TreeNode root, TreeNode otherNode) {
		if (root == null || otherNode == null) {
			return -1;
		}

		Integer result = -1;
		LinkedList<TreeNode> stack = new LinkedList<TreeNode>();
		stack.add(root);
		while (!stack.isEmpty()) {
			TreeNode currentNode = stack.removeFirst();
			result = result + 1;
			if (currentNode == otherNode) {
				return result;
			}

			if (currentNode.getChildren() != null) {
				stack.addAll(0, currentNode.getChildren());
			}
		}
		return -1;
	}

}
