package org.romaframework.aspect.workflow.domain;

import java.util.Set;

public interface WorkFlowState {
	
	public Set<String> getActivities();

	public String getDefinition();

	public void setDefinition(String definition);

	public String getName();

	public void setName(String name);

	public Set<String> getNextStates();

}
