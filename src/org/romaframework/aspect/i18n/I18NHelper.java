package org.romaframework.aspect.i18n;

import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;

public class I18NHelper {

	public static final String	CLASS_LABEL_POSTFIX	= "label";
	public static final String	LABEL_POSTFIX				= ".label";

	public static final String	CLASS_HINT_POSTFIX	= "hint";
	public static final String	HINT_POSTFIX				= ".hint";

	public static String getLabel(SchemaClassElement iElement) {
		return getLabel(iElement, null);
	}

	public static String getLabel(SchemaClassElement iElement, String label) {
		if (label == null)
			label = I18NAspect.VARNAME_PREFIX + iElement.getName();
		else if (!label.startsWith(I18NAspect.VARNAME_PREFIX))
			// RETURN FIXED LABEL
			return label;

		if (iElement.getEntity() == null)
			label = null;
		else {
			label += LABEL_POSTFIX;
			label = Roma.i18n().resolve(iElement.getEntity().getSchemaClass(), label);
		}

		if (label == null)
			label = Utility.getClearName(iElement.getName());

		return label;
	}

	public static String getHint(SchemaClassElement iElement, String hint) {
		if (hint == null) {
			hint = I18NAspect.VARNAME_PREFIX + iElement.getName();
		} else if (!hint.startsWith(I18NAspect.VARNAME_PREFIX)) {
			// RETURN FIXED LABEL
			return hint;
		}
		hint += HINT_POSTFIX;
		hint = Roma.component(I18NAspect.class).resolve(iElement.getEntity().getSchemaClass(), hint);
		if (hint == null) {
			hint = Utility.getClearName(iElement.getName());
		}
		return hint;
	}

	public static String getLabel(SchemaClassElement iElement, String label, String postfix) {
		if (label == null) {
			label = I18NAspect.VARNAME_PREFIX + iElement.getName();
		} else if (!label.startsWith(I18NAspect.VARNAME_PREFIX)) {
			// RETURN FIXED LABEL
			return label;
		}
		label += "." + postfix;
		label = Roma.component(I18NAspect.class).resolve(iElement.getEntity().getSchemaClass(), label);
		if (label == null) {
			label = Utility.getClearName(iElement.getName());
		}
		return label;
	}

	public static String getLabel(SchemaClassDefinition iClass, String label) {

		if (label == null) {
			label = I18NAspect.VARNAME_PREFIX + CLASS_LABEL_POSTFIX;
		} else if (!label.startsWith(I18NAspect.VARNAME_PREFIX)) {
			// RETURN FIXED LABEL
			return label;
		}
		label = Roma.component(I18NAspect.class).resolve(iClass.getSchemaClass(), label);
		if (label == null) {
			label = Utility.getClearName(iClass.getSchemaClass().getName());
		}
		return label;
	}

	public static String getLabel(SchemaClassDefinition iClass) {
		return getLabel(iClass, null);
	}

	public static String getLabel(Class<?> iClass) {
		return getLabel(Roma.schema().getSchemaClass(iClass));
	}
}
