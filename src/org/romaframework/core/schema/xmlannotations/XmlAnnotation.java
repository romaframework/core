package org.romaframework.core.schema.xmlannotations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class XmlAnnotation extends XmlNode {

	protected Map<String, XmlAspectAnnotation>	aspects	= new HashMap<String, XmlAspectAnnotation>();

	public void clearAspects() {
		aspects.clear();
	}

	public void addAspect(XmlAspectAnnotation value) {
		if (value != null)
			aspects.put(value.getName(), value);
	}

	public XmlAspectAnnotation removeAspect(String aspectName) {
		return aspects.remove(aspectName);
	}

	public XmlAspectAnnotation aspect(String name) {
		return aspects.get(name);
	}

	public Collection<XmlAspectAnnotation> aspects() {
		return aspects.values();
	}

	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();

		if (aspects.size() > 0) {
			buffer.append(" aspects[");
			int i = 0;
			for (Entry<String, XmlAspectAnnotation> entry : aspects.entrySet()) {
				if (i++ > 0)
					buffer.append(", ");

				buffer.append(entry.getValue());
			}
			buffer.append("]");
		}

		return buffer.toString();
	}

}
