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
import java.util.Map;

public interface SchedulerEvent {

	public void setName(String name);

	public String getName();

	public Date getStartTime();

	public void setStartTime(Date iStartTime);

	public String getRule();

	public void setRule(String rule);

	public String getImplementation();

	public void setImplementation(String implementation);

	public Map<String, Object> getContext();

	public void addContextParameter(String iKey, Object iValue);
}
