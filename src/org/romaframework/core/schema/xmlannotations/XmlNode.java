package org.romaframework.core.schema.xmlannotations;

public class XmlNode {
	protected String	text;

	@Override
	public String toString() {
		if (text == null)
			return "";

		return "value[" + text + "]";
	}

	public String getText() {
		return text;
	}

	public void setText(String value) {
		if (value != null)
			value = value.trim();

		if (value.length() == 0)
			value = null;

		this.text = value;
	}
}
