package org.romaframework.aspect.service;

import java.util.List;

public class ServiceInfo {
	private String											url;
	private String											operationName;
	private List<ServiceParameterInfo>	input;
	private List<ServiceParameterInfo>	output;

	public ServiceInfo() {
	}

	public ServiceInfo(String operationName) {
		this.operationName = operationName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public List<ServiceParameterInfo> getInput() {
		return input;
	}

	public void setInput(List<ServiceParameterInfo> input) {
		this.input = input;
	}

	public List<ServiceParameterInfo> getOutput() {
		return output;
	}

	public void setOutput(List<ServiceParameterInfo> output) {
		this.output = output;
	}
}
