package org.romaframework.aspect.i18n.rb;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.schema.reflection.SchemaClassReflection;

public class I18nFileManager {

	private static Log														log											= LogFactory.getLog(SchemaClassReflection.class);

	public static final String										DEFAULT_LOCALE					= "DEFAULT_LOCALE";
	public static final char											BUNDLE_SEPARATOR				= '_';
	public static final char											EXTENZIONS_SEPARATOR		= '.';
	private static final char											KEY_LANGUAGE_SEPARETOR	= '#';
	private String																defaultLocale						= DEFAULT_LOCALE;

	/**
	 * Contains the i18n keys and values. the key format is:locale#key
	 */
	private Map<String, String>										values									= new HashMap<String, String>();

	/**
	 * Contains the associations of locale with files and with the set of keys in file. Used for reloading and duplicate key check.
	 */
	private Map<String, Map<String, Set<Object>>>	localeFileKeys					= new HashMap<String, Map<String, Set<Object>>>();

	/**
	 * the set of available Languages.
	 */
	private Set<Locale>														locales;

	public synchronized void load(String fileName, InputStream inputStream) throws IOException {
		String fileN = fileName.substring(0, fileName.lastIndexOf(EXTENZIONS_SEPARATOR));
		String language;
		if (fileName.length() > 5 && fileN.charAt(fileN.length() - 5) == BUNDLE_SEPARATOR) {
			language = fileN.substring(fileN.length() - 4);
		} else if (fileName.length() > 3 && fileN.charAt(fileN.length() - 3) == BUNDLE_SEPARATOR) {
			language = fileN.substring(fileN.length() - 2);
		} else {
			language = DEFAULT_LOCALE;
		}
		Map<String, Set<Object>> fileKeys = localeFileKeys.get(language);
		if (fileKeys == null) {
			fileKeys = new HashMap<String, Set<Object>>();
			localeFileKeys.put(language, fileKeys);
		}
		Set<Object> oldKeys = fileKeys.get(fileName);
		if (oldKeys != null) {
			for (Object key : oldKeys) {
				values.remove(language + KEY_LANGUAGE_SEPARETOR + key.toString());
			}
		}
		Properties properties = new Properties();
		properties.load(inputStream);
		for (Map.Entry<Object, Object> vals : properties.entrySet()) {
			checkKey(language, vals.getKey(), fileName);
			values.put(language + KEY_LANGUAGE_SEPARETOR + vals.getKey().toString(), vals.getValue().toString());
		}
		fileKeys.put(fileName, properties.keySet());

	}

	private void checkKey(String language, Object key, String fileName) {
		List<String> files = new ArrayList<String>();
		Map<String, Set<Object>> fileKeys = localeFileKeys.get(language);
		if (fileKeys != null) {
			for (Map.Entry<String, Set<Object>> entry : fileKeys.entrySet()) {
				if (entry.getValue().contains(key)) {
					files.add(entry.getKey());
				}
			}
			if (!files.isEmpty()) {
				files.add(fileName);
				log.warn("Found duplicate key:" + key + " in Files:" + files);
			}
		}
	}

	public String getString(String key, Locale locale) {
		String value = getString(locale.toString(), key);
		if (value == null)
			value = getString(locale.getLanguage(), key);
		if (value == null)
			value = getString(locale.getCountry(), key);
		if (value == null)
			value = getString(defaultLocale, key);
		return value;
	}

	protected String getString(String locale, String key) {
		return values.get(locale + KEY_LANGUAGE_SEPARETOR + key);
	}

	public synchronized Set<Locale> getAvailableLanguages() {
		if (locales == null) {
			locales = new HashSet<Locale>();
			Set<String> keys = localeFileKeys.keySet();
			for (Locale locale : Locale.getAvailableLocales()) {
				if (keys.contains(locale.toString())) {
					locales.add(locale);
				}
			}
		}
		return locales;
	}

	public String getDefaultLocale() {
		return defaultLocale;
	}

	public void setDefaultLocale(String defaultLocale) {
		this.defaultLocale = defaultLocale;
	}

}
