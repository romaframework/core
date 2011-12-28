package org.romaframework.aspect.i18n;

import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;

@Deprecated
public class I18NHelper {

	public static final String	CLASS_LABEL_POSTFIX	= "label";
	public static final String	LABEL_POSTFIX				= ".label";

	public static final String	CLASS_HINT_POSTFIX	= "hint";
	public static final String	HINT_POSTFIX				= ".hint";

	public static String getLabel(SchemaClassElement iElement) {
		return getLabel(iElement, null);
	}

	public static String getLabel(SchemaClassElement iElement, String label) {
		return Roma.i18n().get(iElement, I18NType.LABEL);
	}

	public static String getHint(SchemaClassElement iElement, String hint) {
		return Roma.i18n().get(iElement, I18NType.HINT);
	}

	public static String getLabel(SchemaClassElement iElement, String label, String postfix) {
		return Roma.i18n().get(iElement.getEntity().getSchemaClass(), I18NType.LABEL);
	}

	public static String getLabel(SchemaClassDefinition iClass, String label) {
		return Roma.i18n().get(iClass.getSchemaClass(), I18NType.LABEL);
	}

	public static String getLabel(SchemaClassDefinition iClass) {
		return getLabel(iClass, null);
	}

	public static String getLabel(Class<?> iClass) {
		return getLabel(Roma.schema().getSchemaClass(iClass));
	}
}
