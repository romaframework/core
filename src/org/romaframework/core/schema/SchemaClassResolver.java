/*
 * Copyright 2006-2007 Luca Garulli (luca.garulli--at--assetdata.it)
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

package org.romaframework.core.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Utility;
import org.romaframework.core.classloader.ClassLoaderListener;
import org.romaframework.core.config.Serviceable;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.resource.ResourceResolver;
import org.romaframework.core.resource.ResourceResolverListener;

/**
 * Resolve entity class and descriptors using the paths configured.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class SchemaClassResolver implements ResourceResolverListener, Serviceable {

	private final Map<String, String>	classLocations						= new HashMap<String, String>();
	private final Map<String, String>	descriptors								= new HashMap<String, String>();
	private final Map<String, File>		resourceFileOwnerMapping	= new HashMap<String, File>();

	private List<String>							packages									= new ArrayList<String>();
	private ResourceResolver					resourceResolver;

	public static final String				DOMAIN_PACKAGE_NAME				= "domain";
	public static final String				CLASS_SUFFIX							= ".class";
	public static final String				DESCRIPTOR_SUFFIX					= ".xml";

	private static Log								log												= LogFactory.getLog(SchemaClassResolver.class);

	public SchemaClassResolver(ResourceResolver iResourceResolver) {
		resourceResolver = iResourceResolver;
		Controller.getInstance().registerListener(ResourceResolverListener.class, this);
	}

	public void startup() throws RuntimeException {
	}

	public void shutdown() throws RuntimeException {
		classLocations.clear();
		descriptors.clear();
		resourceFileOwnerMapping.clear();
		packages.clear();
	}

	protected boolean acceptResorce(String iResource) {
		return iResource.endsWith(CLASS_SUFFIX) || iResource.endsWith(DESCRIPTOR_SUFFIX);
	}

	public void addPackage(String iPath) {
		String pkg = iPath.replace(File.separatorChar, '.');
		if (packages.contains(pkg))
			return;

		log.debug("[SchemaClassResolver.addPackage] " + iPath);

		packages.add(pkg);
		resourceResolver.loadResources(iPath);
	}

	public void setPackages(List<String> iPackages) {
		for (String i : iPackages) {
			log.debug("adding class package source " + i + " to SchemaClassResolver");
			addPackage(i);
		}
	}

	/**
	 * This method is deprecated in favour of getLanguageClass(String).
	 * 
	 * @see #getLanguageClass(String)
	 */
	@Deprecated
	public Class<?> getEntityClass(String iEntityName) {
		return getLanguageClass(iEntityName);
	}

	/**
	 * Get the Class instance for a requested entity.
	 * 
	 * @param iEntityName
	 *          Entity name to find
	 * @return Class instance if found, null otherwise
	 */
	public Class<?> getLanguageClass(String iEntityName) {
		try {
			String pkg = classLocations.get(iEntityName);
			if (pkg == null)
				return null;

			return Class.forName(pkg + iEntityName);
		} catch (Throwable t) {
			// NOT FOUND, TRY AGAIN WITH THE NEXT ONE
		}
		return null;
	}

	/**
	 * Get the Class instance for a requested entity.
	 * 
	 * @param iEntityName
	 *          Entity name to find
	 * @return Class instance if found, null otherwise
	 */
	public List<Class<?>> getLanguageClassByInheritance(Class<?> iBaseClass) {
		List<Class<?>> result = new ArrayList<Class<?>>();

		Set<String> classNames = classLocations.keySet();

		for (String className : classNames) {
			try {
				String pkg = classLocations.get(className);
				if (pkg == null)
					continue;
				Class<?> class1 = Class.forName(pkg + className);
				if (iBaseClass.isAssignableFrom(class1))
					result.add(class1);
			} catch (Throwable t) {
				// NOT FOUND, TRY AGAIN WITH THE NEXT ONE
			}
		}
		return result;
	}

	public Map<String, String> getClassLocations() {
		return classLocations;
	}

	public String getClassDescriptorPath(String iEntityName) {
		String path = descriptors.get(iEntityName);
		if (path == null)
			return null;
		return path + iEntityName + DESCRIPTOR_SUFFIX;
	}

	public String[] getClassPackages() {
		return packages.toArray(new String[packages.size()]);
	}

	public void addDomainPackage(String iPath) {
		addPackage(iPath + Utility.PACKAGE_SEPARATOR + DOMAIN_PACKAGE_NAME);
	}

	public void addResource(File iFile, String iName, String iPackagePrefix, String iStartingPackage) {
		if (!iPackagePrefix.startsWith(iStartingPackage))
			return;

		if (iName.toLowerCase().endsWith(CLASS_SUFFIX)) {
			addResourceClass(iFile, iName, iPackagePrefix);
		} else if (iName.toLowerCase().endsWith(DESCRIPTOR_SUFFIX)) {
			addResourceDescriptor(iFile, iName, iPackagePrefix);
		}

		resourceFileOwnerMapping.put(iName, iFile);
	}

	public File getFileOwner(String iResourceName) {
		return resourceFileOwnerMapping.get(iResourceName);
	}

	public Class<?> reloadEntityClass(String iClassName) throws ClassNotFoundException {
		/*
		 * String classPaths[] = Roma.component(ResourceResolver.class).getClassPaths(); URL[] urls = new URL[classPaths.length]; for
		 * (int i = 0; i < classPaths.length; ++i) { for (String item : classPaths[i].split(File.pathSeparator)) { try { urls[i] = new
		 * File(item).toURI().toURL(); } catch (MalformedURLException e) {
		 * log.error("[RomaClassLoader.init] Bad url setting classpath: " + item); } } }
		 */
		/*
		 * RomaClassLoader clsLoader = new RomaClassLoader(); Thread.currentThread().setContextClassLoader(clsLoader); Class<?> cls =
		 * clsLoader.reloadClass(iClassName);
		 */

		return null;// cls;
	}

	private void addResourceClass(File iFile, String iName, String iPackagePrefix) {
		// ADD A NEW CLASS
		String name = iName.substring(0, iName.length() - CLASS_SUFFIX.length());
		String currentPath = classLocations.get(name);
		if (currentPath != null) {
			// ALREADY EXISTS
			if (currentPath.equals(iPackagePrefix))
				// SAME PATH: PROBABLY THE JAR WAS SETTED MULTIPLE TIMES
				log.warn("[SchemaClassResolver.addResourceClass] Warning: found the class " + name
						+ " defined twice in the classpath and they point to the same package " + iPackagePrefix
						+ " Assure you have only one class to avoid alignment problems. Current path is: " + iFile);
			else
				// ALREADY EXISTS
				log.warn("[SchemaClassResolver.addResourceClass] Found the class " + name + " defined twice. Ignore current package "
						+ iPackagePrefix + " Use the first one found: " + currentPath + " from path: " + iFile);
		} else {
			if (log.isDebugEnabled())
				log.debug("[SchemaClassResolver.addResourceClass] > Loading class: " + iName + " from path: " + iFile
						+ " using the package: " + iPackagePrefix);

			classLocations.put(name, iPackagePrefix);

			// WAKE UP LISTENERS ON THE NEW CLASS FOUND
			List<ClassLoaderListener> listeners = Controller.getInstance().getListeners(ClassLoaderListener.class);
			if (listeners != null) {
				try {
					Class<?> clazz = Class.forName(iPackagePrefix.replace('/', '.') + iName.substring(0, iName.indexOf('.')));
					for (ClassLoaderListener listener : listeners) {
						// INVOKE THE LISTENER
						listener.onClassLoading(clazz);
					}
				} catch (ClassNotFoundException e) {
				}
			}
		}
	}

	private void addResourceDescriptor(File iFile, String iName, String iPackagePrefix) {
		if (log.isDebugEnabled())
			log.debug("[SchemaClassResolver.addResourceDescriptor] > Loading descriptor: " + iName + " from: " + iPackagePrefix);

		String name = iName.substring(0, iName.length() - DESCRIPTOR_SUFFIX.length());
		String currentPath = descriptors.get(name);
		if (currentPath != null) {
			// ALREADY EXISTS
			if (currentPath.equals(iPackagePrefix))
				// ALREADY EXISTS
				log.warn("[SchemaClassResolver.addResourceDescriptor] Warning: found the Xml Annotation " + name
						+ " defined twice in the classpath and they point to the same package " + iPackagePrefix
						+ " Assure you have only one class to avoid alignment problems. Current path is: " + iFile);
			else
				// ALREADY EXISTS
				log.warn("[SchemaClassResolver.addResourceDescriptor] Found the Xml Annotation " + name
						+ " defined twice. Ignore current package " + iPackagePrefix + " Use the first one found: " + currentPath
						+ " from path: " + iFile);
		} else {
			if (log.isDebugEnabled())
				log.debug("[SchemaClassResolver.addResourceDescriptor] > Loading Xml Annotation: " + iName + " from path: " + iFile
						+ " using the package: " + iPackagePrefix);

			descriptors.put(name, Utility.PATH_SEPARATOR + iPackagePrefix.replace('.', Utility.PATH_SEPARATOR));
		}
	}

	public String getStatus() {
		return STATUS_UNKNOWN;
	}
}
