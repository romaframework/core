package org.romaframework.core.schema.xmlannotations;

import java.util.Map.Entry;

public class XmlEventAnnotation extends XmlActionAnnotation {
	@Override
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("event{");
		buffer.append(name);

		

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

	

		buffer.append("}");
		return buffer.toString();
	}

}
