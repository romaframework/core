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

package org.romaframework.aspect.view;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.command.ViewCommand;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.aspect.Aspect;

/**
 * View Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 * 
 */
public interface ViewAspect extends Aspect {

	public static final String	ASPECT_NAME	= "view";

	/**
	 * Display the form reading information from POJO received in the current desktop, in default position.
	 * 
	 * @param iContent
	 *          Object instance to display
	 */
	public void show(Object iContent) throws ViewException;

	/**
	 * Display the form reading information from POJO received following the layout rules. Display the object on iWhere position in
	 * the current desktop.
	 * 
	 * @param iContent
	 *          Object instance to display
	 * @param iPosition
	 *          Position where to display the object
	 * @throws ViewException
	 */
	public void show(Object iContent, String iPosition) throws ViewException;

	/**
	 * Display the form reading information from POJO received following the layout rules. Display the object on iWhere position in
	 * the current desktop.
	 * 
	 * @param iContent
	 *          Object instance to display
	 * @param iPosition
	 *          Position where to display the object
	 * @param iScreen
	 *          Screen to use
	 * @param iSession
	 *          User session to use (null for the current user session)
	 * @throws ViewException
	 */
	public void show(Object iContent, String iPosition, Screen iScreen, SessionInfo iSession) throws ViewException;

	/**
	 * Return the desktop for the current user.
	 * 
	 * @return Screen instance
	 */
	public Screen getScreen();

	/**
	 * Return the screen for the user.
	 * 
	 * @param iUserSession
	 *          User session
	 * @return Screen instance
	 */
	public Screen getScreen(Object iUserSession);

	/**
	 * Set the current screen.
	 * 
	 * @param iScreen
	 *          Screen instance to set for the current user.
	 */
	public void setScreen(Screen iScreen);

	/**
	 * Set the screen in a specified session.
	 * 
	 * @param iScreen
	 *          Screen instance to set for the current user.
	 * @param iSession
	 *          User session to use (null for the current user session)
	 */
	public void setScreen(Screen screen, SessionInfo currentSession);

	/**
	 * Push a command that must be executed by the view aspect
	 * 
	 * @param iCommand
	 */
	public void pushCommand(ViewCommand iCommand);

	/**
	 * Retrieve the context where the application was deployed.
	 * 
	 * @return the context where the application was deployed.
	 */
	public String getContextPath();
}
