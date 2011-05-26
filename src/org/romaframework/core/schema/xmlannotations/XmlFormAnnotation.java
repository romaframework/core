package org.romaframework.core.schema.xmlannotations;

public class XmlFormAnnotation extends XmlNode implements Cloneable {

	protected XmlFormAreaAnnotation	rootArea;

	public Object clone() throws CloneNotSupportedException {
		XmlFormAnnotation cloned = (XmlFormAnnotation) super.clone();
		cloned.rootArea = (XmlFormAreaAnnotation) rootArea.clone();
		return cloned;
	}

	public XmlFormAreaAnnotation getRootArea() {
		return rootArea;
	}

	public void setRootArea(XmlFormAreaAnnotation rootArea) {
		this.rootArea = rootArea;
	}

}
