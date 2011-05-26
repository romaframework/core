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

package org.romaframework.aspect.scheduler;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.romaframework.core.aspect.Aspect;
import org.romaframework.core.command.Command;

/**
 * Scheduler Aspect behavior interface.
 */
public interface SchedulerAspect extends Aspect {
	public static final String	NAME												= "scheduler";

	public static final String	EVENT_STATUS_BLOCKED				= "Blocked";
	public static final String	EVENT_STATUS_ERROR					= "Error";
	public static final String	EVENT_STATUS_COMPLETED			= "Completed";
	public static final String	EVENT_STATUS_PAUSED					= "Paused";
	public static final String	EVENT_STATUS_NORMAL					= "Normal";
	public static final String	EVENT_STATUS_NOT_SCHEDULED	= "Not Scheduled";
	public static final String	EVENT_STATUS_EXECUTING			= "Executing";

	public SchedulerEvent createEvent() throws SchedulerException;

	public Date schedule(SchedulerEvent iTrigger) throws SchedulerException;

	public void unSchedule(SchedulerEvent iTrigger) throws SchedulerException;

	public void executeNow(SchedulerEvent iTrigger) throws SchedulerException;

	public void pauseJob(SchedulerEvent iTrigger) throws SchedulerException;

	public void unpauseJob(SchedulerEvent iTrigger) throws SchedulerException;

	public String getLastEventExcecution(String name);

	public Date getNextEventExcecution(String name);

	public void setJobsImplementation(Map<String, Command> jobsImplementation);

	public Map<String, Command> getJobsImplementation();

	public List<String> getImplementationsList();

	public String getEventState(String name);

	public Object getUnderlyingComponent();
}
