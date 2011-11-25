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

package org.romaframework.core.config;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.text.NumberFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.Utility;
import org.romaframework.core.component.ComponentAspect;
import org.romaframework.core.component.SpringComponentEngine;
import org.romaframework.core.exception.InternalException;
import org.romaframework.core.flow.Controller;
import org.romaframework.core.module.ModuleManager;

/**
 * Central singleton class to gather all service using Spring Application Context.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public class RomaApplicationContext implements Serviceable {

	protected ComponentAspect								componentAspect;

	protected static String									applicationPath;
	protected static ResourceAccessor				resourceAccessor;
	protected final static String						OS_VAR_ROMA_HOME	= "ROMA_HOME";
	protected final static String						APP_VAR_ROMA_HOME	= "roma.home";

	protected static RomaApplicationContext	instance					= null;
	protected static Log										log								= LogFactory.getLog(RomaApplicationContext.class);

	/**
	 * Constructor called by Spring configuration.
	 */
	public RomaApplicationContext() {
		setRomaHomeVariable();
	}

	public void startup() throws RuntimeException {
		// TODO REMOVE WIRED SPRING IOC
		componentAspect = new SpringComponentEngine();
		componentAspect.startup();

		List<RomaApplicationListener> listeners = Controller.getInstance().getListeners(RomaApplicationListener.class);

		if (listeners != null)
			for (RomaApplicationListener listener : listeners)
				listener.onBeforeStartup();

		try {
			// START ALL MODULES
			ModuleManager.getInstance().startup();

			// STAR USER APPLICATION CONFIGURATION OBJECT
			Roma.component(ApplicationConfiguration.class).startup();
		} catch (Throwable t) {
			log.error("[RomaApplicationContext.startup] Error on startup the application", t);
			throw new InternalException("Error on startup the application", t);
		} finally {
			if (listeners != null)
				for (RomaApplicationListener listener : listeners)
					listener.onAfterStartup();
		}

		logMemoryUsage();
	}

	public synchronized void shutdown() {
		// STOP USER APPLICATION CONFIGURATION OBJECT
		List<RomaApplicationListener> listeners = Controller.getInstance().getListeners(RomaApplicationListener.class);
		if (listeners != null)
			for (RomaApplicationListener listener : listeners)
				listener.onBeforeShutdown();

		Roma.component(ApplicationConfiguration.class).shutdown();

		// STOP ALL MODULES
		ModuleManager.getInstance().shutdown();

		if (listeners != null)
			for (RomaApplicationListener listener : listeners)
				listener.onAfterShutdown();

		componentAspect.shutdown();

	}

	public static String getApplicationPath() {
		return applicationPath;
	}

	public static void setApplicationPath(String iApplicationPath) {
		iApplicationPath = Utility.getUniversalResourcePath(iApplicationPath);

		if (iApplicationPath.endsWith(Utility.PATH_SEPARATOR_STRING))
			iApplicationPath = iApplicationPath.substring(0, iApplicationPath.length() - 1);

		applicationPath = iApplicationPath;

		log.info("[RomaApplicationContext.setApplicationPath] " + applicationPath);
	}

	public static RomaApplicationContext getInstance() {
		if (instance == null)
			synchronized (RomaApplicationContext.class) {
				if (instance == null)
					instance = new RomaApplicationContext();
			}

		return instance;
	}

	public ComponentAspect getComponentAspect() {
		return componentAspect;
	}

	private void logMemoryUsage() {
		MemoryMXBean memBean = ManagementFactory.getMemoryMXBean();
		MemoryUsage heapMemory = memBean.getHeapMemoryUsage();
		MemoryUsage nonHeapMemory = memBean.getNonHeapMemoryUsage();

		NumberFormat nf = NumberFormat.getInstance();
		log.info("--------------- MEMORY USAGE ---------------");
		log.info("HEAP INIT MEMORY:       " + nf.format(heapMemory.getInit()) + " bytes");
		log.info("HEAP USED MEMORY:       " + nf.format(heapMemory.getUsed()) + " bytes");
		log.info("HEAP COMMITTED MEMORY:  " + nf.format(heapMemory.getCommitted()) + " bytes");
		log.info("HEAP MAX MEMORY:        " + nf.format(heapMemory.getMax()) + " bytes");
		log.info(" ");
		log.info("NON HEAP INIT MEMORY:       " + nf.format(nonHeapMemory.getInit()) + " bytes");
		log.info("NON HEAP USED MEMORY:       " + nf.format(nonHeapMemory.getUsed()) + " bytes");
		log.info("NON HEAP COMMITTED MEMORY:  " + nf.format(nonHeapMemory.getCommitted()) + " bytes");
		log.info("NON HEAP MAX MEMORY:        " + nf.format(nonHeapMemory.getMax()) + " bytes");
		log.info("--------------------------------------------");
	}

	public String getStatus() {
		if (componentAspect != null)
			return componentAspect.getStatus();
		return STATUS_DOWN;
	}

	private void setRomaHomeVariable() {
		if (System.getProperty(APP_VAR_ROMA_HOME) == null) {
			String romaHome = System.getenv(OS_VAR_ROMA_HOME);
			if (romaHome != null) {
				log.info("[RomaApplicationContext] Setting ${roma.home} to os environment variable ROMA_HOME: " + romaHome);
				System.setProperty(APP_VAR_ROMA_HOME, romaHome);
			}
		}
	}

	public void setComponentAspect(ComponentAspect componentAspect) {
		this.componentAspect = componentAspect;
	}

	public static ResourceAccessor getResourceAccessor() {
		return resourceAccessor;
	}

	public static void setResourceAccessor(ResourceAccessor resourceAccessor) {
		RomaApplicationContext.resourceAccessor = resourceAccessor;
	}
}
