package org.romaframework.core.schema.xmlannotations;

import java.io.Serializable;

public class XmlNode implements Serializable {

	private static final long	serialVersionUID	= 248531074995678342L;
	
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

		if (value != null && value.length() == 0)
			value = null;

		this.text = value;
	}
}
