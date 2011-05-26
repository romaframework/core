package org.romaframework.aspect.workflow.domain;

import java.util.Date;

public interface WorkFlowStatus {
	
	public Date getDate();

	public void setDate(Date date);

	public String getDescription();

	public void setDescription(String description);

	public abstract String getOid();

	public void setOid(String oid);

	public WorkFlowState getState();

	public void setState(WorkFlowState state);
	
	public abstract String getStateName();

}
