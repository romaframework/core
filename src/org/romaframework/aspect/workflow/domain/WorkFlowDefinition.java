/*
 *
 * Copyright 2007 Luca Molino (luca.molino@--AT--assetdata.it)
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
package org.romaframework.aspect.workflow.domain;

/**
 * @author Luca Molino
 *
 */
public interface WorkFlowDefinition {
	
	public void setBeginState(WorkFlowState beginState);

	public WorkFlowState getBeginState();

	public String getName();

}
