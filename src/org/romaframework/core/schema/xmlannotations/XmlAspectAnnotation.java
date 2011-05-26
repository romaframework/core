package org.romaframework.core.schema.xmlannotations;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class XmlAspectAnnotation extends XmlNode implements Serializable {
	private String							name;
	private XmlFormAnnotation		form;
	private Map<String, String>	attributes				= new HashMap<String, String>();
	private static final long		serialVersionUID	= -4006210726209178161L;

	public XmlFormAnnotation getForm() {
		return form;
	}

	public void setForm(XmlFormAnnotation form) {
		this.form = form;
	}

	public Map<String, String> getAttributes() {
		return attributes;
	}

	public String getAttribute(String name) {
		return this.attributes.get(name);
	}

	public void setAttribute(String name, String value) {
		this.attributes.put(name, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		buffer.append("aspect{");
		buffer.append(name);

		if (form != null) {
			buffer.append(" form=");
			buffer.append(form);
		}

		if (attributes.size() > 0) {
			buffer.append(" attributes[");
			int i = 0;
			for (Entry<String, String> entry : attributes.entrySet()) {
				if (i++ > 0)
					buffer.append(", ");

				buffer.append(entry.getKey());
				buffer.append("=");
				buffer.append(entry.getValue());
			}
			buffer.append("]");
		}

		super.toString();
		buffer.append("}");

		return buffer.toString();
	}
}
