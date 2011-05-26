package org.romaframework.core.schema.xmlannotations;

import java.util.ArrayList;
import java.util.List;

public class XmlActionAnnotation extends XmlAnnotation {

	protected String												name;
	protected List<XmlParameterAnnotation>	parameters;

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("action{");
		buffer.append(name);

		buffer.append(super.toString());

		buffer.append("}");
		return buffer.toString();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void addParameter(XmlParameterAnnotation iParam) {
		if (parameters == null)
			parameters = new ArrayList<XmlParameterAnnotation>();

		parameters.add(iParam);
	}

	public List<XmlParameterAnnotation> getParameters() {
		return parameters;
	}

	public String getSignature() {
		return name + (parameters != null ? parameters.toString() : "");
	}
}
