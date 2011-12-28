package org.romaframework.aspect.i18n;

public enum I18NType {
	LABEL("label"), HINT("hint"), FORMAT("format"), CONFIRM("confirm"), EXCEPTION("exception"), CONTENT("content"), ERROR("error");

	private String	name;

	private I18NType(String name) {
		this.name = "." + name;
	}

	public String getName() {
		return name;
	}

}