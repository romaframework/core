package org.romaframework.core.schema.xmlannotations;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class XmlFieldAnnotation extends XmlAnnotation {
	private String														name;
	private String														type;
	private XmlClassAnnotation								classAnnotation;
	private XmlClassAnnotation								parentClass;
	protected Map<String, XmlEventAnnotation>	events;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public XmlClassAnnotation getClassAnnotation() {
		return classAnnotation;
	}

	public void setClassAnnotation(XmlClassAnnotation classAnnotation) {
		this.classAnnotation = classAnnotation;
	}

	public XmlClassAnnotation getParentClass() {
		return parentClass;
	}

	public void setParentClass(XmlClassAnnotation parentClass) {
		this.parentClass = parentClass;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Set<XmlEventAnnotation> getEvents() {
		if (events == null)
			return null;
		return new HashSet<XmlEventAnnotation>(events.values());
	}

	public void addEvent(XmlEventAnnotation result) {
		if (events == null) {
			events = new LinkedHashMap<String, XmlEventAnnotation>();
		}
		events.put(result.getName(), result);
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("field{");
		buffer.append(name);

		if (type != null) {
			buffer.append(":");
			buffer.append(type);
		}

		buffer.append(super.toString());

		if (events != null && events.size() > 0) {
			buffer.append(" events[");
			int i = 0;
			for (Entry<String, XmlEventAnnotation> entry : events.entrySet()) {
				if (i++ > 0)
					buffer.append(", ");

				buffer.append(entry.getValue());
			}
			buffer.append("]");
		}
		
		if (classAnnotation != null) {
			buffer.append(" ");
			buffer.append(classAnnotation);
		}

		if (text != null) {
			buffer.append(" value=");
			buffer.append(text);
		}

		
		
		buffer.append("}");

		return buffer.toString();
	}

}
