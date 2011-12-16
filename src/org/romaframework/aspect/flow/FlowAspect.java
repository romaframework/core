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

package org.romaframework.aspect.flow;

import java.util.Map;
import java.util.Stack;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.aspect.Aspect;

/**
 * Flow Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface FlowAspect extends Aspect {

	public static final String	ASPECT_NAME	= "flow";

	/**
	 * Return the Map with the screen area and POJO in area.
	 */
	public Map<String, Object> current();

	/**
	 * Return the current Map of area/POJO of requested session.
	 * 
	 * @param iSession
	 *          User session to use (null for the current user session)
	 */
	public Map<String, Object> current(SessionInfo iSession);

	/**
	 * Return the current POJO in the default screen area.
	 */
	public Object currentDefault();

	/**
	 * Return the POJO in the specified screen area.
	 */
	public Object current(String area);

	/**
	 * Return the POJO in the specified screen area of requested session.
	 */
	public Object current(String area, SessionInfo iSession);

	/**
	 * Follow the application flow moving forward. If the iNextObject is a string, the virtual object will be used.
	 * 
	 * @param iNextObject
	 *          Next object to display
	 */
	public void forward(Object iNextObject);

	/**
	 * Follow the application flow moving forward in the default area.
	 * 
	 * @param iNextObject
	 *          Next object to display
	 */
	public void forwardDefault(Object iNextObject);

	/**
	 * Follow the application flow moving forward.
	 * 
	 * @param iNextObject
	 *          Next object to display
	 * @param iPosition
	 *          Position where to display the object
	 */
	public void forward(Object iNextObject, String iPosition);

	/**
	 * Follow the application flow moving forward and specifying screen and user session to use.
	 * 
	 * @param iNextObject
	 *          Next object to display
	 * @param iPosition
	 *          Position where to display the object
	 * @param iScreen
	 *          Screen to use
	 * @param iSession
	 *          User session to use (null for the current user session)
	 */
	public void forward(Object iNextObject, String iPosition, Screen iScreen, SessionInfo iSession);

	/**
	 * Go back, reading the user's history. It displays the previous POJO if any.
	 * 
	 * @return previous POJO if any
	 * 
	 */
	public Object back();

	/**
	 * Go back,reading the user's history in the default screen area.
	 * 
	 * @return previous POJO if any
	 * 
	 */
	public Object backDefault();

	/**
	 * Go back,reading the user's history in the specified screen area.
	 * 
	 * @param area
	 *          where back.
	 * @return the POJO before the object provided
	 * 
	 */
	public Object back(String area);

	/**
	 * Go back, reading the user session's history. It displays the previous POJO if any.
	 * 
	 * @param iSession
	 *          User session to use (null for the current user session)
	 * @return previous POJO if any
	 * 
	 */
	public Object back(SessionInfo iSession);

	/**
	 * Go back, reading the user session's history. It displays the previous POJO if any.
	 * 
	 * @param area
	 *          where back.
	 * @param iSession
	 *          User session to use (null for the current user session)
	 * @return previous POJO if any
	 * 
	 */
	public Object back(String area, SessionInfo iSession);

	/**
	 * Returns the history of current user session. The history is a list of pair as POJO and Position where is displayed.
	 * 
	 * @return list of pair as POJO and Position where is displayed.
	 */
	public Map<String, Stack<Object>> getHistory();

	/**
	 * Returns the history of the user session passed as parameter. The history is a list of pair as POJO and Position where is
	 * displayed.
	 * 
	 * @param iSession
	 *          User session to use (null for the current user session)
	 * @return list of pair as POJO and Position where is displayed.
	 */
	public Map<String, Stack<Object>> getHistory(SessionInfo iSession);

	/**
	 * Clear the current history for all areas.
	 */
	public void clearHistory();

	/**
	 * Clear the history for specified area.
	 * 
	 * @param area
	 *          the name of the area to clear the history.
	 */
	public void clearHistory(String area);

	/**
	 * Clear the history of the requested session closing also all the open popups if any.
	 * 
	 * @param iSession
	 *          User session to use (null for the current user session)
	 */
	public void clearHistory(SessionInfo currentSession);

	/**
	 * Show an object as modal popup.
	 * 
	 * @param popup
	 *          object to show.
	 */
	public void popup(Object popup);

	/**
	 * Show an object as popup.
	 * 
	 * @param popup
	 *          object to show.
	 * @param modal
	 *          true if the popup is modal otherwise false.
	 */
	public void popup(Object popup, boolean modal);

	/**
	 * opens an alert dialog with given title and body
	 * 
	 * @param iTitle
	 *          the alert title
	 * @param iBody
	 *          the alert text
	 */
	public void alert(String iTitle, String iBody);

}
