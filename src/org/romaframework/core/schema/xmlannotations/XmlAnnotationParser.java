package org.romaframework.core.schema.xmlannotations;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlAnnotationParser extends DefaultHandler {

	public static String print(XmlClassAnnotation annotation) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buffer.append(printClass(annotation, true, 0));
		return buffer.toString();
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

	static StringBuffer printClass(XmlClassAnnotation annotation, boolean printNamespace, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<class", indent);
		if (printNamespace) {
			buffer.append(" xmlns=\"http://www.romaframework.org/xml/roma\"\n");
			append(buffer, "xmlns:xsd=\"http://www.w3.org/2001/XMLSchema-instance\"\n", indent + 1);
			append(buffer, "xsd:schemaLocation=\"http://www.romaframework.org/xml/roma http://www.romaframework.org/schema/v2/roma.xsd\"", indent + 1);
		}
		buffer.append(">\n");
		printAspectsBlock(annotation, buffer, indent + 1);
		if (annotation.getFields() != null && annotation.getFields().size() > 0) {
			append(buffer, "<fields>\n", indent + 1);
			for (XmlFieldAnnotation field : annotation.getFields()) {
				buffer.append(printField(field, indent + 2));
			}
			append(buffer, "</fields>\n", indent + 1);
		}
		if (annotation.getActions() != null && annotation.getActions().size() > 0) {
			append(buffer, "<actions>\n", indent + 1);
			for (XmlActionAnnotation action : annotation.getActions()) {
				buffer.append(printAction(action, indent + 2));
			}
			append(buffer, "</actions>\n", indent + 1);
		}

		if (annotation.getEvents() != null && annotation.getEvents().size() > 0) {
			append(buffer, "<events>\n", indent + 1);
			for (XmlEventAnnotation action : annotation.getEvents()) {
				buffer.append(printEvent(action, indent + 2));
			}
			append(buffer, "</events>\n", indent + 1);
		}

		append(buffer, "</class>\n", indent);
		return buffer;
	}

	private static void printAspectsBlock(XmlAnnotation annotation, StringBuffer buffer, int indent) {
		Collection<XmlAspectAnnotation> aspects = annotation.aspects();
		if (aspects != null && aspects.size() > 0) {
			append(buffer, "<aspects>\n", indent);
			for (XmlAspectAnnotation aspect : aspects) {
				buffer.append(printAspect(aspect, indent + 1));
			}
			append(buffer, "</aspects>\n", indent);
		}
	}

	private static StringBuffer printAction(XmlActionAnnotation action, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<action", indent);
		buffer.append(" name=\"");
		buffer.append(action.getName());
		buffer.append("\">\n");

		if (action.getParameters() != null) {
			append(buffer, "<parameters>\n", indent + 1);
			for (XmlParameterAnnotation p : action.getParameters()) {
				append(buffer, "<param ", indent + 2);

				if (p.getName() != null && p.getName().length() > 0) {
					buffer.append("name=\"");
					buffer.append(p.getName());
					buffer.append("\" ");
				}

				if (p.getType() != null && p.getType().length() > 0) {
					buffer.append("type=\"");
					buffer.append(p.getType());
					buffer.append("\" ");
				}
				buffer.append("/>\n");
			}
			append(buffer, "</parameters>\n", indent + 1);
		}

		printAspectsBlock(action, buffer, indent + 1);
		append(buffer, "</action>\n", indent);
		return buffer;
	}

	private static StringBuffer printEvent(XmlEventAnnotation action, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<event", indent);
		buffer.append(" name=\"");
		buffer.append(action.getName());
		buffer.append("\">\n");

		if (action.getParameters() != null) {
			append(buffer, "<parameters>\n", indent + 1);
			for (XmlParameterAnnotation p : action.getParameters()) {
				append(buffer, "<param ", indent + 2);

				if (p.getName() != null && p.getName().length() > 0) {
					buffer.append("name=\"");
					buffer.append(p.getName());
					buffer.append("\" ");
				}

				if (p.getType() != null && p.getType().length() > 0) {
					buffer.append("type=\"");
					buffer.append(p.getType());
					buffer.append("\" ");
				}
				buffer.append("/>\n");
			}
			append(buffer, "</parameters>\n", indent + 1);
		}

		printAspectsBlock(action, buffer, indent + 1);
		append(buffer, "</event>\n", indent);
		return buffer;
	}

	private static StringBuffer printField(XmlFieldAnnotation field, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<field name=\"" + field.getName() + "\">\n", indent);
		printAspectsBlock(field, buffer, indent + 1);
		if (field.getEvents() != null && field.getEvents().size() > 0) {
			append(buffer, "<events>\n", indent + 1);
			for (XmlEventAnnotation action : field.getEvents()) {
				buffer.append(printEvent(action, indent + 2));
			}
			append(buffer, "</events>\n", indent + 1);
		}
		if (field.getClassAnnotation() != null) {
			buffer.append(printClass(field.getClassAnnotation(), false, indent + 1));
		}
		append(buffer, "</field>\n", indent);
		return buffer;
	}

	private static StringBuffer printAspect(XmlAspectAnnotation aspect, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<" + aspect.getName(), indent);
		for (Map.Entry<String, String> entry : aspect.getAttributes().entrySet()) {
			buffer.append(" " + entry.getKey() + "=\"" + entry.getValue() + "\"");
		}
		if (aspect.getForm() == null) {
			buffer.append("/>\n");
		} else {
			buffer.append(">\n");
			buffer.append(printForm(aspect.getForm(), indent + 1));
			append(buffer, "</" + aspect.getName() + ">\n", indent);
		}
		return buffer;
	}

	private static StringBuffer printForm(XmlFormAnnotation form, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<form>\n", indent);
		buffer.append(printArea(form.getRootArea(), indent + 1));
		append(buffer, "</form>\n", indent);
		return buffer;
	}

	private static StringBuffer printArea(XmlFormAreaAnnotation area, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<area", indent);
		if (area.getName() != null) {
			buffer.append(" name=\"" + area.getName() + "\"");
		}
		if (area.getAlign() != null) {
			buffer.append(" align=\"" + area.getAlign() + "\"");
		}
		if (area.getSize() != null) {
			buffer.append(" size=\"" + area.getSize() + "\"");
		}
		if (area.getType() != null) {
			buffer.append(" type=\"" + area.getType() + "\"");
		}
		if (area.getChildren() == null || area.getChildren().size() == 0) {
			buffer.append("/>\n");
		} else {
			buffer.append(">\n");
			for (XmlFormAreaAnnotation child : area.getChildren()) {
				buffer.append(printArea(child, indent + 1));
			}
			append(buffer, "</area>\n", indent);
		}
		return buffer;
	}

	public static XmlScreenAnnotation parseScreen(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		XmlScreenHandler handler = new XmlScreenHandler();
		parser.parse(inputStream, handler);
		inputStream.close();
		return handler.getScreen();
	}

	public static void save(XmlClassAnnotation doc, File fileToSave) throws IOException {
		FileWriter output = new FileWriter(fileToSave);
		output.write(print(doc));
	}

	public static void save(XmlScreenAnnotation doc, File fileToSave) throws IOException {
		FileWriter output = new FileWriter(fileToSave);
		output.write(print(doc));
	}

	public static String print(XmlScreenAnnotation doc) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
		buffer.append(printScreen(doc, true, 0));
		return buffer.toString();
	}

	static StringBuffer printScreen(XmlScreenAnnotation annotation, boolean printNamespace, int indent) {
		StringBuffer buffer = new StringBuffer();
		append(buffer, "<screen", indent);
		if (printNamespace) {
			buffer.append("  xmlns=\"http://www.romaframework.org/xml/roma\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema-instance\"\n");
			append(buffer, " xsd:schemaLocation=\"http://www.romaframework.org/xml/roma http://www.romaframework.org/schema/roma-view-screen.xsd\"", indent + 1);
		}
		buffer.append(">\n");
		buffer.append(printArea(annotation.getRootArea(), indent + 1));

		append(buffer, "</screen>\n", indent);
		return buffer;
	}

	public static XmlClassAnnotation parseClass(InputStream inputStream) throws ParserConfigurationException, SAXException, IOException {
		SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
		XmlClassHandler handler = new XmlClassHandler();
		parser.parse(inputStream, handler);
		return handler.getRootClass();
	}
}
