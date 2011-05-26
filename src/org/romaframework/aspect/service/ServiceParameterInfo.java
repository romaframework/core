package org.romaframework.aspect.service;

public class ServiceParameterInfo {
  private String name;
  private String type;
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getType() {
    return type;
  }
  public void setType(String type) {
    this.type = type;
  }
  public Class<?> getJavaType(){
  	if(type==null){
  		return null;
  	}
  	//TODO fix just a trial... support right types...
  	if("string".equals(type)){
  		return String.class;
  	}
  	if("integer".equals(type)){
  		return Integer.class;
  	}
  	if("double".equals(type)){
  		return Double.class;
  	}
  	if("float".equals(type)){
  		return Float.class;
  	}
  	if("boolean".equals(type)){
  		return Boolean.class;
  	}
  	return Object.class;
  }
}
