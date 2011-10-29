package org.romaframework.core.util.parser;

import org.romaframework.core.schema.SchemaHelper;

public class ObjectVariableResolver implements VariableParserListener {

	public static final String	VAR_BEGIN	= "${";
	public static final String	VAR_END		= "}";
	protected String						format;
	protected Object						arg;

	public ObjectVariableResolver(String format) {
		this.format = format;
	}

	public String resolveVariables(Object arg) {
		this.arg = arg;
		return VariableParser.resolveVariables(format, VAR_BEGIN, VAR_END, this);
	}

	public String resolve(String iVariable) {
		try {
			int i = Integer.parseInt(iVariable) - 1;
			if (arg instanceof Object[]) {
				Object[] argArray = (Object[]) arg;
				if (i >= 0 && i < argArray.length) {
					return argArray[i] == null ? null : argArray[i].toString();
				}
			}
			return null;
		} catch (NumberFormatException e) {
		}
		Object val0Arg = null;
		if (arg instanceof Object[]) {
			if (((Object[]) arg).length > 0) {
				val0Arg = ((Object[]) arg)[0];
			}
		} else {
			val0Arg = arg;
		}
		try {
			Object value = SchemaHelper.getFieldValue(val0Arg, iVariable);
			return value == null ? null : value.toString();
		} catch (Exception exception) {
		}
		try {
			Object value = SchemaHelper.getFieldValue(val0Arg, iVariable);
			return value == null ? null : value.toString();
		} catch (Exception e) {
		}
		return null;
	}

}
