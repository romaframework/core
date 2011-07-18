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

import java.util.List;

import org.romaframework.aspect.session.SessionInfo;
import org.romaframework.aspect.view.screen.Screen;
import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.domain.type.Pair;

/**
 * Flow Aspect behavior interface.
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface FlowAspect extends Aspect {

	public static final String	ASPECT_NAME	= "flow";

	/**
	 * Return the current position as form of Pair of POJO/Screen area.
	 */
	public Pair<Object, String> current();

	/**
	 * Return the current position of requested session as form of Pair of POJO/Screen area.
	 * 
	 * @param iSession
	 *          User session to use (null for the current user session)
	 */
	public Pair<Object, String> current(SessionInfo iSession);

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
	 * Follow the application flow moving forward.
	 * 
	 * @param iNextObject
	 *          Next object to display
	 * @param iPosition
	 *          Position where to display the object
	 */
	public void forward(Object iNextObject, String iPosition);

	/**
	 * Follow the application flow moving forward. If the iNextObject is a string, the virtual object will be used.
	 * 
	 * @param iNextObject
	 *          Next object to display
	 */
	public void forward(Object iNextObject);

	/**
	 * Go back, reading the user's history. It displays the previous POJO if any.
	 * 
	 * @return previous POJO if any
	 * 
	 */
	public Object back();

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
	 * Go back until the given POJO, reading the user's history. It displays the previous POJO if any.
	 * 
	 * @return the POJO before the object provided
	 * 
	 */
	public Object back(Object iGoBackUntil);

	/**
	 * Returns the history of current user session. The history is a list of pair as POJO and Position where is displayed.
	 * 
	 * @return list of pair as POJO and Position where is displayed.
	 */
	public List<Pair<Object, String>> getHistory();

	/**
	 * Returns the history of the user session passed as parameter. The history is a list of pair as POJO and Position where is
	 * displayed.
	 * 
	 * @param iSession
	 *          User session to use (null for the current user session)
	 * @return list of pair as POJO and Position where is displayed.
	 */
	public List<Pair<Object, String>> getHistory(SessionInfo iSession);

	/**
	 * Clear the current history closing also all the open popups if any.
	 */
	public void clearHistory();

	/**
	 * Clear the history of the requested session closing also all the open popups if any.
	 * 
	 * @param iSession
	 *          User session to use (null for the current user session)
	 */
	public void clearHistory(SessionInfo currentSession);
}
