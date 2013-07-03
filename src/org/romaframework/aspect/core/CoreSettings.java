package org.romaframework.aspect.core;

import java.util.HashSet;
import java.util.Set;

import org.romaframework.core.Roma;

public class CoreSettings {

	public static CoreSettings getInstance() {
		return Roma.autoComponent(CoreSettings.class);
	}

	private String			schemaIgnoreFields	= "";
	private String			schemaIgnoreActions	= "";
	private String			schemaIgnoreEvents	= "";
	private Set<String>	additionalPackages	= new HashSet<String>();

	public String getSchemaIgnoreFields() {
		return schemaIgnoreFields;
	}

	public void setSchemaIgnoreFields(String schemaIgnoreFields) {
		this.schemaIgnoreFields = schemaIgnoreFields;
	}

	public String getSchemaIgnoreActions() {
		return schemaIgnoreActions;
	}

	public void setSchemaIgnoreActions(String schemaIgnoreActions) {
		this.schemaIgnoreActions = schemaIgnoreActions;
	}

	public String getSchemaIgnoreEvents() {
		return schemaIgnoreEvents;
	}

	public void setSchemaIgnoreEvents(String schemaIgnoreEvents) {
		this.schemaIgnoreEvents = schemaIgnoreEvents;
	}

	public Set<String> getAdditionalPackages() {
		return additionalPackages;
	}

	public void setAdditionalPackages(Set<String> additionalPackages) {
		this.additionalPackages = additionalPackages;
	}

}
