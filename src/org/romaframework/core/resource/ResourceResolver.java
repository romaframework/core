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

package org.romaframework.core.resource;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Utility;
import org.romaframework.core.flow.Controller;

/**
 * Resolve entity class and descriptors using the paths configured.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class ResourceResolver {

	protected static final String		PACKAGE_SEPARATOR		= ".";
	protected static final String		JAR_PATH_SEPARATOR	= "/";
	protected static final String		JAR_SUFFIX					= ".jar";
	protected static final String[]	ARCHIVE_SUFFIXES		= { ".jar", ".zip" };

	private static Log							log									= LogFactory.getLog(ResourceResolver.class);

	protected ClassLoader						classLoader;

	public ResourceResolver() {
		this.classLoader = getClass().getClassLoader();
	}

	public ResourceResolver(ClassLoader iClassLoader) {
		this.classLoader = iClassLoader;
	}

	/**
	 * Delegates to the listeners the adding of resource.
	 * 
	 * @param iFile
	 * @param iName
	 * @param iPackagePrefix
	 * @param iStartingPackage
	 */
	protected void addResource(File iFile, String iName, String iPackagePrefix, String iStartingPackage) {
		List<ResourceResolverListener> listeners = Controller.getInstance().getListeners(ResourceResolverListener.class);
		if (listeners != null)
			for (ResourceResolverListener l : listeners) {
				if (l != null)
					l.addResource(iFile, iName, iPackagePrefix, iStartingPackage);
			}
	}

	/**
	 * Default accept any resources. Override this to filer interested resources only.
	 * 
	 * @param iResource
	 *          Resource name
	 * @return true if it's accepted, otherwise false
	 */
	protected boolean acceptResorce(String iResource) {
		return true;
	}

	protected boolean acceptArchives(String iResource) {
		for (String res : ARCHIVE_SUFFIXES)
			if (iResource.endsWith(res))
				return true;
		return false;

	}

	@Deprecated
	public void setClasspaths(List<String> iClassPaths) {
	}

	/**
	 * Search the resource in the specified package if the package is "" find all class in current classloader jars.
	 * 
	 * @param iStartingPackage
	 *          the package to find.
	 */
	public void loadResources(String iStartingPackage) {
		File f;
		log.debug("[ResourceResolver.loadEntities] loading Resources for: " + iStartingPackage);
		// ClassLoader classLoader = getClass().getClassLoader();
		try {
			String path = iStartingPackage.replace(PACKAGE_SEPARATOR, JAR_PATH_SEPARATOR);
			Enumeration<URL> esploringPackage;
			if (path.length() == 0 && classLoader instanceof URLClassLoader) {
				esploringPackage = Collections.enumeration(Arrays.asList(((URLClassLoader) classLoader).getURLs()));
			} else {
				esploringPackage = classLoader.getResources(path);
			}
			while (esploringPackage.hasMoreElements()) {
				URL url = esploringPackage.nextElement();
				String file = url.getPath();
				file = URLDecoder.decode(file, "UTF-8");
				if (file.startsWith(Utility.FILE_PREFIX)) {
					file = file.substring(Utility.FILE_PREFIX.length());
				}
				if (file.contains(JAR_SUFFIX)) {
					file = file.substring(0, file.indexOf(JAR_SUFFIX) + JAR_SUFFIX.length());
					f = new File(file);
					if (acceptArchives(f.getName()) && f.exists()) {
						log.debug("[ResourceResolver.loadEntities] > Inspecting jar: " + file);
						examineJarFile(f, iStartingPackage);
					}
				} else {
					f = new File(file);
					if (f.exists() && f.isDirectory()) {
						log.debug("[ResourceResolver.loadEntities] > Inspecting path: " + file);
						addFiles(f.listFiles(), iStartingPackage + PACKAGE_SEPARATOR, iStartingPackage);
					}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		log.debug("[ResourceResolver.loadEntities] End of loading resources for: " + iStartingPackage);
	}

	/**
	 * Examine a jar file searching for entities.
	 * 
	 * @param f
	 * @throws IOException
	 */
	protected void examineJarFile(File f, String iStartingPackage) {
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(f);

			final JarInputStream jis = new JarInputStream(new BufferedInputStream(fis));
			JarEntry entry;
			String fullName, packagePrefix, name;
			String pathStartingPackage = Utility.getResourcePath(iStartingPackage);
			int i;

			while ((entry = jis.getNextJarEntry()) != null) {
				fullName = entry.getName();
				if (fullName.startsWith(pathStartingPackage) && acceptResorce(fullName) ) {
					i = fullName.lastIndexOf(JAR_PATH_SEPARATOR) + 1;
					packagePrefix = fullName.substring(0, i).replace(JAR_PATH_SEPARATOR, PACKAGE_SEPARATOR);
					name = fullName.substring(i, fullName.length());
					addResource(f, name, packagePrefix, iStartingPackage);
				}
			}
		} catch (Exception e) {
		} finally {
			try {
				if (fis != null)
					fis.close();
			} catch (IOException e) {
			}
		}
	}

	protected void addFiles(File[] files, String packagePrefix, String iStartingPackage) {
		String name;

		for (int i = 0; i < files.length; i++) {
			name = files[i].getName();
			if (files[i].isDirectory()) {
				addFiles(files[i].listFiles(), packagePrefix + name + PACKAGE_SEPARATOR, iStartingPackage);
			} else {
				addResource(files[i], name, packagePrefix, iStartingPackage);
			}
		}
	}
}
