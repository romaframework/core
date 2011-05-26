package org.romaframework.core.schema.xmlannotations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class XmlFormAreaAnnotation extends XmlNode implements Cloneable, Serializable {

	private static final long							serialVersionUID	= 3506371439115247466L;

	protected String											name;
	protected String											type							= "placeholder";
	protected Integer											size;
	protected String											align;
	protected String											style;
	protected List<XmlFormAreaAnnotation>	children					= new ArrayList<XmlFormAreaAnnotation>();
	private static int										serialAreaId			= 0;

	public XmlFormAreaAnnotation() {
	}

	public XmlFormAreaAnnotation(String iName, String type, Integer size) {
		setName(iName);
		this.type = type;
		this.size = size;
	}

	public XmlFormAreaAnnotation(String iName) {
		setName(iName);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		XmlFormAreaAnnotation cloned = (XmlFormAreaAnnotation) super.clone();
		cloned.children = new ArrayList<XmlFormAreaAnnotation>();
		for (XmlFormAreaAnnotation c : children)
			cloned.children.add((XmlFormAreaAnnotation) c.clone());

		return cloned;
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(name);
		buffer.append(":");
		buffer.append(type);

		if (children.size() > 0) {
			buffer.append("{ ");
			int i = 0;
			for (XmlFormAreaAnnotation c : children) {
				if (i++ > 0)
					buffer.append(", ");

				buffer.append(c.toString());
			}
			buffer.append(" }");
		}

		return buffer.toString();
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
		this.size = size;
	}

	public String getAlign() {
		return align;
	}

	public void setAlign(String align) {
		this.align = align;
	}

	public String getName() {
		return name;
	}

	public void setName(String iName) {
		if (iName == null)
			// AUTO-ASSIGN A UNIQUE NAME
			iName = String.valueOf(serialAreaId++);

		this.name = iName;
	}

	public List<XmlFormAreaAnnotation> getChildren() {
		return children;
	}

	public XmlFormAreaAnnotation addChild(XmlFormAreaAnnotation child) {
		if (this.children == null) {
			this.children = new ArrayList<XmlFormAreaAnnotation>();
		}
		this.children.add(child);
		return child;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}
}
