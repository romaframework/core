package org.romaframework.core.schema.xmlannotations;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlScreenHandler extends DefaultHandler {

	private XmlScreenAnnotation	root;

	private Stack<Object>				nodeStack	= new Stack<Object>();

	private Stack<String>				tagStack	= new Stack<String>();

	private Object currentNode() {
		if (nodeStack.isEmpty()) {
			return null;
		}
		return nodeStack.peek();
	}

	public void setCurrentNode(Object node) {
		if (node.equals(currentNode())) {
			return;
		}
		nodeStack.push(node);
	}

	@Override
	public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
		String realName;
		if (uri == null || uri.length() == 0) {
			realName = name;
		} else {
			realName = localName;
		}
		setCurrentNode(parseNext(realName, attributes));
		tagStack.push(realName);

	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		tagStack.pop();
		if (currentNode() instanceof XmlFormAreaAnnotation) {
			nodeStack.pop();
		}
	}

	private Object parseNext(String nodeType, Attributes attributes) throws SAXException {
		Object currentNode = currentNode();
		if (currentNode == null) {
			if (nodeType.toLowerCase().equals("screen")) {
				root = createScreen(attributes, null);
				return root;
			} else {
				throw new SAXException("invalid root tag: " + nodeType);
			}
		}
		if (nodeType.toLowerCase().equals("area")) {
			XmlFormAreaAnnotation area = createArea(attributes, currentNode);
			return area;
		}
		throw new SAXException("invalid node: " + nodeType);
	}

	private XmlScreenAnnotation createScreen(Attributes attributes, Object object) throws SAXException {
		XmlScreenAnnotation result = new XmlScreenAnnotation();
		String defaultArea = attributes.getValue("defaultArea");
		if (defaultArea == null) {
			throw new SAXException("Required attribute defaultArea for screen");
		}
		result.setDefaultArea(defaultArea);
		return result;
	}

	private XmlFormAreaAnnotation createArea(Attributes attributes, Object parent) {
		XmlFormAreaAnnotation result = new XmlFormAreaAnnotation();
		if (parent instanceof XmlScreenAnnotation) {
			((XmlScreenAnnotation) parent).setRootArea(result);
		} else {
			((XmlFormAreaAnnotation) parent).addChild(result);
		}

		String align = attributes.getValue("align");
		if (align != null) {
			result.setAlign(align);
		}
		String name = attributes.getValue("name");
		if (name != null) {
			result.setName(name);
		}
		String size = attributes.getValue("size");
		if (size != null) {
			result.setSize(Integer.parseInt(size));
		}
		String type = attributes.getValue("type");
		if (type != null) {
			result.setType(type);
		}
		return result;
	}

	public XmlScreenAnnotation getScreen() {
		return root;
	}

}
