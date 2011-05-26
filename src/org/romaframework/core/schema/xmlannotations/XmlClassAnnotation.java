package org.romaframework.core.schema.xmlannotations;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class XmlClassAnnotation extends XmlAnnotation {

	protected String														extendClass;
	protected String[]													implementsInterfaces;
	protected Map<String, XmlFieldAnnotation>		fields;
	protected Map<String, XmlActionAnnotation>	actions;
	protected Map<String, XmlEventAnnotation>		events;

	public void addField(XmlFieldAnnotation field) {
		if (fields == null) {
			fields = new LinkedHashMap<String, XmlFieldAnnotation>();
		}
		field.setParentClass(this);
		fields.put(field.getName(), field);
	}

	public void addAction(XmlActionAnnotation action) {
		if (actions == null) {
			actions = new LinkedHashMap<String, XmlActionAnnotation>();
		}
		actions.put(action.getSignature(), action);
	}

	public void addEvent(XmlEventAnnotation event) {
		if (events == null) {
			events = new LinkedHashMap<String, XmlEventAnnotation>();
		}
		events.put(event.getName(), event);
	}

	public XmlFieldAnnotation getField(String fieldName) {
		return fields == null ? null : fields.get(fieldName);
	}

	public XmlActionAnnotation getAction(String actionName) {
		return actions == null ? null : actions.get(actionName);
	}

	public XmlEventAnnotation getEvent(String eventName) {
		return events == null ? null : events.get(eventName);
	}

	public Collection<XmlFieldAnnotation> getFields() {
		return fields == null ? null : fields.values();
	}

	public Collection<XmlActionAnnotation> getActions() {
		return actions == null ? null : actions.values();
	}

	public Collection<XmlEventAnnotation> getEvents() {
		return events == null ? null : events.values();
	}

	public String getExtendClass() {
		return extendClass;
	}

	public void setExtendClass(String extendClass) {
		this.extendClass = extendClass;
	}

	public String[] getImplementsInterfaces() {
		return implementsInterfaces;
	}

	public void setImplementsInterfaces(String[] implementsInterfaces) {
		this.implementsInterfaces = implementsInterfaces;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append(super.toString());

		if (fields != null && fields.size() > 0) {
			buffer.append(" fields[");
			int i = 0;
			for (Entry<String, XmlFieldAnnotation> entry : fields.entrySet()) {
				if (i++ > 0)
					buffer.append(", ");

				buffer.append(entry.getValue());
			}
			buffer.append("]");
		}

		if (actions != null && actions.size() > 0) {
			buffer.append(" actions[");
			int i = 0;
			for (Entry<String, XmlActionAnnotation> entry : actions.entrySet()) {
				if (i++ > 0)
					buffer.append(", ");

				buffer.append(entry.getValue());
			}
			buffer.append("]");
		}

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

		return buffer.toString();
	}
}
