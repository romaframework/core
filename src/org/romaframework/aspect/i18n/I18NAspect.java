/*
 * Copyright 2006 Luca Garulli (luca.garulli--at--assetdata.it)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.romaframework.aspect.i18n;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Set;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaObject;

/**
 * I18N Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface I18NAspect extends Aspect {
	public static final String	ASPECT_NAME				= "i18n";
	public static final String	VARNAME_PREFIX		= "$";
	public static final String	CONTEXT_SEPARATOR	= ".";
	public static final String	DEFAULT_VALUE_KEY	= "$content";

	public Set<Locale> getAvailableLanguages();

	public String getLabel(SchemaObject iObject, String iElementName, String iElementLabel);

	public DateFormat getDateFormat();

	public DateFormat getDateTimeFormat();

	public DateFormat getDateTimeFormat(Locale iLocale);

	public DateFormat getTimeFormat();

	public DateFormat getTimeFormat(Locale iLocale);

	public DateFormat getDateFormat(Locale iLocale);

	public NumberFormat getNumberFormat();

	public NumberFormat getNumberFormat(Locale iLocale);

	public String resolveString(SchemaClassDefinition iObjectClass, String iText, Object... iArgs);

	public String resolveString(Class<?> iObjectClass, String iText, Object... iArgs);

	public String resolveString(String iText, Object... iArgs);

	public String getString(String iText);

	public String getString(String iText, Locale iLocale);
}
