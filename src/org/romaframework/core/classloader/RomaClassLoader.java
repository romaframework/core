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
package org.romaframework.core.classloader;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.flow.Controller;

/**
 * Roma Class Loader. It overrides default class loading by reloading Roma annotation if any.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class RomaClassLoader extends URLClassLoader {

	private Log														log	= LogFactory.getLog(RomaClassLoader.class);
	// private boolean classPathSetted = false;
	private static Map<String, Class<?>>	reloadedClasses;

	public RomaClassLoader() {
		super(new URL[] {});
	}

	public RomaClassLoader(URL[] urls) {
		super(urls);
	}

	/**
	 * Try to find the class between reloaded class, otherwise delegate the loading to the superclass.
	 * 
	 * @param name
	 *          Class name
	 * @return Class if found
	 */
	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		if (reloadedClasses == null) {
			return super.loadClass(name);
		}

		Class<?> cls = null;
		synchronized (reloadedClasses) {
			cls = reloadedClasses.get(name);
		}

		if (cls == null) {
			cls = super.loadClass(name);
		}

		return cls;
	}

	/**
	 * Explicit reload a class
	 * 
	 * @param iClassName
	 * @return
	 * @throws ClassNotFoundException
	 */
	public Class<?> reloadClass(String iClassName) throws ClassNotFoundException {
		init();

		Class<?> cls;

		synchronized (this) {
			if (reloadedClasses == null) {
				reloadedClasses = new HashMap<String, Class<?>>();
			}
		}

		synchronized (reloadedClasses) {
			// LOAD CLASS FOR REAL
			cls = super.findClass(iClassName);
			reloadedClasses.put(iClassName, cls);

			signalToAllListeners(cls);
		}

		log.warn("[RomaClassLoader.reloadClass] Reloaded class: " + cls + ", hash: " + cls.hashCode());

		return cls;
	}

	private void init() {
		/*
		 * if (!classPathSetted) { synchronized (this) { if (!classPathSetted) { classPathSetted = true; String classPaths[] =
		 * Roma.component(ResourceResolver.class).getClassPaths(); for (String classPath : classPaths) { for (String item :
		 * classPath.split(File.pathSeparator)) { try { addURL(new File(item).toURI().toURL()); } catch (MalformedURLException e) {
		 * log.error("[RomaClassLoader.init] Bad url setting classpath: " + item); } } } } } }
		 */
	}

	/**
	 * Signal to all registered listeners the class loading event.
	 * 
	 * @param cls
	 *          Just loaded class
	 */
	private void signalToAllListeners(Class<?> cls) {
		if (Controller.getInstance().getListeners(ClassLoaderListener.class) != null) {
			for (Object obj : Controller.getInstance().getListeners(ClassLoaderListener.class)) {
				ClassLoaderListener l = (ClassLoaderListener) obj;
				if (l != null) {
					l.onClassLoading(cls);
				}
			}
		}
	}
}