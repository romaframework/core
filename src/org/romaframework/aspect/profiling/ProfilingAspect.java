/*
 * Copyright 2006-2008 Luca Garulli (luca.garulli--at--assetdata.it)
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
package org.romaframework.aspect.profiling;

import org.romaframework.core.aspect.Aspect;

/**
 * Profiling Aspect behavior interface. This aspect handle all the profiling concerns. Use it in this way:<br/>
 * <br/>
 * <code>
 *   &nbsp;&nbsp;long timer = Roma.aspect(ProfilingAspect.class).begin();<br/>
 *   &nbsp;&nbsp;...<br/>
 *     &nbsp;&nbsp;Roma.aspect(ProfilingAspect.class).end( "TestEmployeeLoading", timer );<br/>
 * </code>
 * 
 * @author Luca Garulli (luca.garulli--at--assetdata.it)
 */
public interface ProfilingAspect extends Aspect {

	public static final String	ASPECT_NAME	= "profiling";

	/**
	 * Start a new timer.
	 * 
	 * @return New Timer to use in end() method.
	 * @see #end(String, long);
	 */
	public long begin();

	/**
	 * Profile a new call passing the key and caller.
	 * 
	 * @param iKey
	 *          key to collect data
	 * @param iBeginTime
	 *          Begin time taken with begin()
	 * @return The delta time between begin and end
	 * @see #begin
	 */

	public long end(String iKey, long iBeginTime);
}
