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

package org.romaframework.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.config.ApplicationConfiguration;
import org.romaframework.core.config.RomaApplicationContext;
import org.romaframework.core.io.virtualfile.classpath.ClassPathFile;
import org.romaframework.core.io.virtualfile.physical.PhysicalFile;

public class Utility {

	private static final String	ASPECT										= "aspect";
	public static final String	ROMA_PACKAGE							= "org.romaframework";
	public static final char		PACKAGE_SEPARATOR					= '.';
	public static final String	PACKAGE_SEPARATOR_STRING	= ".";
	public static final char		PATH_SEPARATOR						= '/';
	public static final String	PATH_SEPARATOR_STRING			= "/";
	public static final char		WINDOWS_SEPARATOR					= '\\';
	public static final String	CLASSPATH_PREFIX					= ClassPathFile.PREFIX;
	public static final String	FILE_PREFIX								= PhysicalFile.PREFIX;

	private static final Log		log												= LogFactory.getLog(Utility.class);

	public static URL loadURL(String iPath) {
		URL url = null;
		try {
			if (iPath.startsWith(CLASSPATH_PREFIX)) {
				// BUILD URL FROM CLASS LOADER
				url = Utility.class.getClassLoader().getResource(iPath.substring(CLASSPATH_PREFIX.length()));
			} else if (iPath.startsWith(FILE_PREFIX)) {
				// LOAD FILE DIRECTLY
				url = new URL(iPath);
			}
		} catch (MalformedURLException e) {
		}
		return url;
	}

	public static InputStream loadStream(String iPath) {
		InputStream result = null;
		try {
			if (iPath.startsWith(CLASSPATH_PREFIX)) {
				// BUILD URL FROM CLASS LOADER
				result = Utility.class.getClassLoader().getResourceAsStream(iPath.substring(CLASSPATH_PREFIX.length()));
			} else if (iPath.startsWith(FILE_PREFIX)) {
				// LOAD FILE DIRECTLY

				URL url = new URL(iPath);
				File file = null;
				try {
					file = new File(url.toURI());
				} catch (URISyntaxException sintaxEx) {
					file = new File(url.getPath());
				}
				if (file != null && file.exists() && !file.isDirectory()) {
					result = new FileInputStream(file);
				}
			}
		} catch (Exception e) {
			log.error("could not load resource as stream: " + iPath);
		}
		return result;
	}

	public static String getCapitalizedString(String iSource) {
		return Character.toUpperCase(iSource.charAt(0)) + iSource.substring(1);
	}

	public static String getUncapitalizedString(String iSource) {
		return Character.toLowerCase(iSource.charAt(0)) + iSource.substring(1);
	}

	public static String getClearName(String iJavaName) {
		StringBuilder buffer = new StringBuilder();

		char c;
		if (iJavaName != null) {
			buffer.append(Character.toUpperCase(iJavaName.charAt(0)));
			for (int i = 1; i < iJavaName.length(); ++i) {
				c = iJavaName.charAt(i);

				if (Character.isUpperCase(c)) {
					buffer.append(' ');
				}

				buffer.append(c);
			}

		}
		return buffer.toString();
	}

	public static boolean isRomaClass(Class<?> iClass) {
		return iClass.getName().startsWith(ROMA_PACKAGE);
	}

	public static String[] getResourceNamesFirstSeparator(String iResourceName, String iSeparator, String iDefaultPrefixName) {
		if (iResourceName == null) {
			return null;
		}

		String names[] = new String[2];
		int pos = iResourceName.indexOf(iSeparator);
		names[0] = pos > -1 ? iResourceName.substring(0, pos) : iDefaultPrefixName;

		names[1] = iResourceName.substring(pos + 1);
		return names;
	}

	public static String[] getResourceNamesLastSeparator(String iResourceName, String iSeparator, String iDefaultPrefixName) {
		if (iResourceName == null) {
			return null;
		}

		String names[] = new String[2];
		int pos = iResourceName.lastIndexOf(iSeparator);
		names[0] = pos > -1 ? iResourceName.substring(0, pos) : iDefaultPrefixName;

		names[1] = iResourceName.substring(pos + 1);
		return names;
	}

	/**
	 * Get the full package of aspect inside user application.
	 * 
	 * @param iAspectName
	 *          Aspect name
	 * @return
	 */
	public static String getApplicationAspectPackage(String iAspectName) {
		return Roma.component(ApplicationConfiguration.class).getApplicationPackage() + PACKAGE_SEPARATOR + iAspectName;
	}

	/**
	 * Get the full package of aspect inside Roma.
	 * 
	 * @param iAspectName
	 *          Aspect name
	 * @return
	 */
	public static String getRomaAspectPackage(String iAspectName) {
		return ROMA_PACKAGE + PACKAGE_SEPARATOR + ASPECT + PACKAGE_SEPARATOR + iAspectName;
	}

	/**
	 * Convert a package path style in resource path style. If the path starts with ".", then it means relative path.
	 * 
	 * @param iClassPath
	 * @return
	 */
	public static String getAbsoluteResourcePath(String iClassPath) {
		boolean filePrefixed = iClassPath.startsWith(FILE_PREFIX);
		String prefix = filePrefixed ? iClassPath.substring(0, FILE_PREFIX.length()) : "";
		String data = filePrefixed ? iClassPath.substring(FILE_PREFIX.length()) : iClassPath;

		boolean relativePath = data.startsWith(".");
		if (relativePath) {
			data = data.substring(1);
		}
		data = data.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);

		if (relativePath) {
			data = RomaApplicationContext.getApplicationPath() + data;
		}

		if (prefix.length() > 0 && data.charAt(0) != PATH_SEPARATOR) {
			data = PATH_SEPARATOR + data;
		}

		return prefix + data;
	}

	/**
	 * Convert a package path style in resource path style.
	 * 
	 * @param iClassPath
	 * @return
	 */
	public static String getPackagePath(String iResourcePath) {
		String path = iResourcePath.charAt(0) == PATH_SEPARATOR ? iResourcePath.substring(1) : iResourcePath;
		return path.replace(PATH_SEPARATOR, PACKAGE_SEPARATOR);
	}

	public static String getResourcePath(String iClassPath) {
		return iClassPath.replace(PACKAGE_SEPARATOR, PATH_SEPARATOR);
	}

	/**
	 * Convert a package path style in resource path style.
	 * 
	 * @param iResourcePath
	 * @return
	 */
	public static String getUniversalResourcePath(String iResourcePath) {
		if (iResourcePath == null) {
			return null;
		}
		return iResourcePath.replace(WINDOWS_SEPARATOR, PATH_SEPARATOR);
	}

	public static String getContainerFile(String filePath) {
		return filePath;
	}

	public static String removeLastSeparatorIfAny(String entryName) {
		if (entryName.endsWith(PATH_SEPARATOR_STRING)) {
			entryName = entryName.substring(0, entryName.length() - 1);
		}
		return entryName;
	}

	public static String filePatternToRegExp(String iPattern) {
		iPattern = iPattern.replace(".", "\\.");
		iPattern = iPattern.replace("*", ".*");
		return iPattern;
	}

	/**
	 * returns the name of the class passed as Clazz <br>
	 * also takes into consideration the anonymous classes, or nested
	 * 
	 * @param iClass
	 * @return String
	 */
	public static String getClassName(Class<?> iClass) {

		if (iClass.getName().indexOf('$') == -1)
			return iClass.getSimpleName();

		// ANONYMOUS CLASS: EXTRACT THE NAME HOWEVER
		String name = iClass.getName();
		int begin = name.lastIndexOf(PACKAGE_SEPARATOR);
		if (begin == -1)
			return null;

		return name.substring(begin + 1);
	}

	
	/**
	 * check if the class is associated to a primitive type
	 * 
	 * @param clazz
	 * @return boolean
	 */
	public static boolean isPrimitiveType(Class<?> clazz) {
		return clazz.isPrimitive() || clazz.isAssignableFrom(Byte.class) || clazz.isAssignableFrom(Short.class)
				|| clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(Float.class)
				|| clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(Character.class) || clazz.isAssignableFrom(String.class)
				|| clazz.isAssignableFrom(Boolean.class);
	}

	public static String cutString(String summary, int maxLength) {
		if (summary == null || summary.length() == 0)
			return "";

		if (summary.length() > maxLength)
			return summary.substring(0, maxLength) + "...";

		return summary;
	}

	public static String getExtension(String iFileName) {
		if (iFileName == null)
			return null;

		int pos = iFileName.lastIndexOf(".");
		if (pos == -1)
			return null;

		return iFileName.substring(pos + 1);
	}

	public static String array2String(Object[] iArray) {
		if (iArray == null || iArray.length == 0)
			return "";

		StringBuilder buffer = new StringBuilder();
		for (Object o : iArray) {
			if (buffer.length() > 0)
				buffer.append(", ");

			buffer.append(o != null ? o.getClass().getSimpleName() : "null");
		}

		return buffer.toString();
	}
}
