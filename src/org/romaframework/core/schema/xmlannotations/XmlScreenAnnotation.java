package org.romaframework.core.schema.xmlannotations;

public class XmlScreenAnnotation {
	private XmlFormAreaAnnotation	rootArea;

	private String								defaultArea;

	public XmlFormAreaAnnotation getRootArea() {
		return rootArea;
	}

	public void setRootArea(XmlFormAreaAnnotation rootArea) {
		this.rootArea = rootArea;
	}

	public String getDefaultArea() {
		return defaultArea;
	}

	public void setDefaultArea(String defaultArea) {
		this.defaultArea = defaultArea;
	}

}
