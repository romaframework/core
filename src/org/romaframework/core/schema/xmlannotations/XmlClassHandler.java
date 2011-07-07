package org.romaframework.core.schema.xmlannotations;

import java.util.Stack;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlClassHandler extends DefaultHandler {

	protected XmlClassAnnotation	rootClass;
	protected Stack<XmlNode>			nodeStack	= new Stack<XmlNode>();
	protected Stack<String>				tagStack	= new Stack<String>();
	protected StringBuilder				text			= new StringBuilder();

	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		for (int i = 0; i < length; ++i)
			text.append(ch[start + i]);
	}

	private XmlNode currentNode() {
		if (nodeStack.isEmpty()) {
			return null;
		}
		return nodeStack.peek();
	}

	public void setCurrentNode(XmlNode node) {
		if (node.equals(currentNode())) {
			return;
		}
		nodeStack.push(node);
	}

	static void addIndent(StringBuffer buffer, int indent) {
		for (int i = 0; i < indent; i++) {
			buffer.append("\t");
		}
	}

	static void append(StringBuffer buffer, String toAppend, int indent) {
		addIndent(buffer, indent);
		buffer.append(toAppend);
	}

	XmlClassAnnotation getRootClass() {
		return rootClass;
	}

	@Override
	public void endElement(String uri, String localName, String name) throws SAXException {
		tagStack.pop();

		if (isToPop(name)) {
			XmlNode lastNode = nodeStack.pop();
			if (lastNode != null && text.length() > 0) {
				if (lastNode instanceof XmlAspectAnnotation)
					lastNode.setText(text.toString());
				else if (lastNode instanceof XmlValueAnnotation)
					((XmlFieldAnnotation) nodeStack.peek()).setText(text.toString());
			}
		}
	}

	private boolean isToPop(String name) {
		if ("aspects".equals(name) || "fields".equals(name) || "actions".equals(name) || "parameters".equals(name) || "events".equals(name)) {
			return false;
		}
		return true;
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
		text.setLength(0);
	}

	private XmlNode parseNext(String nodeType, Attributes attributes) throws SAXException {
		XmlNode currentNode = currentNode();

		String nodeName = nodeType.toLowerCase();

		if (currentNode == null) {
			if (nodeName.equals("class")) {
				rootClass = createClass(attributes, null);
				return rootClass;
			} else {
				throw new SAXException("Invalid root tag: " + nodeType);
			}
		}
		String currentTag = getCurrentTag();
		if (currentNode instanceof XmlFieldAnnotation) {
			if (currentTag.equals("field")) {
				if (nodeName.equals("aspects") || nodeName.equals("events")) {
					return currentNode;
				} else if (nodeName.equals("class")) {
					return createClass(attributes, (XmlFieldAnnotation) currentNode);
				} else if (nodeName.equals("value")) {
					return new XmlValueAnnotation();
				}
			} else if (currentTag.equals("aspects")) {
				return createAspect(nodeType, attributes, (XmlAnnotation) currentNode);
			} else if (currentTag.equals("events") && nodeType.equals("event")) {
				return createEvent(attributes, (XmlFieldAnnotation) currentNode);
			}
		}
		if (currentNode instanceof XmlActionAnnotation) {
			if (currentTag.equals("action") && nodeName.equals("parameters"))
				return currentNode;

			if (currentTag.equals("parameters") && nodeName.equals("param")) {
				XmlParameterAnnotation param = new XmlParameterAnnotation();
				((XmlActionAnnotation) currentNode).addParameter(param);
				String name = attributes.getValue("name");
				param.setName(name);
				String type = attributes.getValue("type");
				param.setType(type);
				return param;
			}
			if (currentTag.equals("action") && nodeName.equals("aspects")) {
				return currentNode;
			} else if (currentTag.equals("aspects")) {
				return createAspect(nodeType, attributes, (XmlAnnotation) currentNode);
			}
		}
		if (currentNode instanceof XmlEventAnnotation) {
			if (currentTag.equals("event") && nodeName.equals("aspects")) {
				return currentNode;
			} else if (currentTag.equals("aspects")) {
				return createAspect(nodeType, attributes, (XmlAnnotation) currentNode);
			}
		}
		if (currentNode instanceof XmlClassAnnotation) {
			if (currentTag.equals("class")) {
				if (nodeName.equals("fields") || nodeName.equals("actions") || nodeName.equals("events") || nodeName.equals("aspects")) {
					return currentNode;
				}
			} else if (currentTag.equals("aspects")) {
				return createAspect(nodeType, attributes, (XmlAnnotation) currentNode);
			} else if (currentTag.equals("fields") && nodeType.equals("field")) {
				return createField(attributes, (XmlClassAnnotation) currentNode);
			} else if (currentTag.equals("actions") && nodeType.equals("action")) {
				return createAction(attributes, (XmlClassAnnotation) currentNode);
			} else if (currentTag.equals("events") && nodeType.equals("event")) {
				return createEvent(attributes, (XmlClassAnnotation) currentNode);
			}
		}
		if (currentNode instanceof XmlAspectAnnotation && nodeName.equals("form")) {
			return createForm(attributes, (XmlAspectAnnotation) currentNode);
		}
		if ((currentNode instanceof XmlFormAnnotation) || (currentNode instanceof XmlFormAreaAnnotation) && nodeName.equals("area")) {
			return createArea(attributes, currentNode);
		}
		throw new SAXException("Invalid node: " + nodeType);
	}

	private XmlNode createArea(Attributes attributes, Object parent) {
		XmlFormAreaAnnotation result = new XmlFormAreaAnnotation();
		if (parent instanceof XmlFormAreaAnnotation)
			((XmlFormAreaAnnotation) parent).addChild(result);
		else
			((XmlFormAnnotation) parent).setRootArea(result);

		result.setName(attributes.getValue("name"));

		String align = attributes.getValue("align");
		if (align != null)
			result.setAlign(align);

		String size = attributes.getValue("size");
		if (size != null)
			result.setSize(Integer.parseInt(size));

		String type = attributes.getValue("type");
		if (type != null)
			result.setType(type);

		String style = attributes.getValue("style");
		if (style != null)
			result.setStyle(style);

		return result;
	}

	private XmlNode createForm(Attributes attributes, XmlAspectAnnotation parent) {
		XmlFormAnnotation result = new XmlFormAnnotation();
		parent.setForm(result);
		return result;
	}

	private XmlActionAnnotation createAction(Attributes attributes, XmlClassAnnotation parent) {
		XmlActionAnnotation result = new XmlActionAnnotation();
		result.setName(attributes.getValue("name"));
		if (parent != null) {
			parent.addAction(result);
		}
		return result;
	}

	private XmlFieldAnnotation createField(Attributes attributes, XmlClassAnnotation parent) {
		String fieldName = attributes.getValue("name");
		XmlFieldAnnotation result = null;
		if (fieldName != null) {
			result = parent.getField(fieldName);
		}
		if (result == null) {
			result = new XmlFieldAnnotation();
			result.setName(fieldName);
			result.setType(attributes.getValue("type"));
			if (parent != null) {
				parent.addField(result);
			}
		}

		return result;
	}

	private XmlEventAnnotation createEvent(Attributes attributes, XmlClassAnnotation parent) {
		XmlEventAnnotation result = new XmlEventAnnotation();
		result.setName(attributes.getValue("name"));
		if (parent != null) {
			parent.addEvent(result);
		}
		return result;
	}

	private XmlEventAnnotation createEvent(Attributes attributes, XmlFieldAnnotation parent) {
		XmlEventAnnotation result = new XmlEventAnnotation();
		result.setName(attributes.getValue("name"));
		if (parent != null) {
			parent.addEvent(result);
		}
		return result;
	}

	private XmlAspectAnnotation createAspect(String name, Attributes attributes, XmlAnnotation parent) {
		XmlAspectAnnotation result = parent.aspect(name);
		if (result == null) {
			result = new XmlAspectAnnotation();
			result.setName(name);
			parent.addAspect(result);
		}
		for (int i = 0; i < attributes.getLength(); i++) {
			String attrName = attributes.getQName(i);
			if (attrName == null) {
				attrName = attributes.getLocalName(i);
			}
			String value = attributes.getValue(i);
			result.setAttribute(attrName, value);
		}

		if (text.length() > 0)
			result.setText(text.toString());

		return result;
	}

	private XmlClassAnnotation createClass(Attributes attributes, XmlFieldAnnotation parent) {
		XmlClassAnnotation result = new XmlClassAnnotation();
		String extendClassTag = attributes.getValue("extends");
		if (extendClassTag != null && extendClassTag.length() > 0)
			result.setExtendClass(extendClassTag);

		String implementsInterfacesTag = attributes.getValue("implements");
		if (implementsInterfacesTag != null && implementsInterfacesTag.length() > 0)
			result.setImplementsInterfaces(implementsInterfacesTag.split(" "));

		if (parent != null) {
			parent.setClassAnnotation(result);
		}
		return result;
	}

	private String getCurrentTag() {
		return tagStack.peek();
	}
}
