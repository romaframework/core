package org.romaframework.core.schema.xmlannotations;

public class XmlParameterAnnotation extends XmlAnnotation {

	protected String	name;
	protected String	type;

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(type);
		buffer.append(" ");
		buffer.append(name);
		return buffer.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
