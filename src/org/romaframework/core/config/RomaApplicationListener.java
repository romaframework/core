/*
 * Copyright 2006-2009 Luca Garulli (luca.garulli--at--assetdata.it)
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

/**
 * Listener for the main Roma events such as startup and shutdown. Register a listener implementation to the Controller using:<br/>
 * <br/>
 * Controller.getInstance().registerListener(RomaApplicationListener.class, this);
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface RomaApplicationListener {

	/**
	 * Called before startup of all module and of application.
	 * 
	 */
	public void onBeforeStartup();

	/**
	 * Called at end of startup of all module and of application.
	 */
	public void onAfterStartup();

	/**
	 * Called before shutdown of all modules and of application.
	 */
	public void onBeforeShutdown();

	/**
	 * Called at end shutdown of all modules and of application.
	 */
	public void onAfterShutdown();
}
