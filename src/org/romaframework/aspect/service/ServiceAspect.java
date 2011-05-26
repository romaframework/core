package org.romaframework.aspect.service;

import java.util.HashMap;
import java.util.List;

import org.romaframework.core.aspect.Aspect;

public interface ServiceAspect extends Aspect {

	public static final String	ASPECT_NAME	= "service";

	/**
	 *Get the WSDL created fro the service Aspect implementation
	 * 
	 * @return the HashMap<Class,Definition> that contain the definition for each class using Class as Key
	 *@author Luca Acquaviva
	 */
	public HashMap<Class<?>, Object> getDefinitionMap();

	public <T> T getClient(Class<T> iInterface, String iUrl);

	/**
	 * 
	 * @param serviceURL
	 *          the service URL
	 * @param operationName
	 *          the operation name
	 * @param inputs
	 *          the list of input parameters
	 * @return a list containing all the outputs of the invocation
	 * @throws UnsupportedOperationException
	 *           if the implementation doesn't support this operation
	 */
	public List<Object> invokeDynamicService(String serviceURL, String operationName, List<Object> inputs)
			throws UnsupportedOperationException;

	/**
	 * lists available operation names for a service
	 * 
	 * @param serviceURL
	 *          the service URL
	 * @return a list of available operation names
	 * @throws UnsupportedOperationException
	 *           if the implementation doesn't support this operation
	 */
	public List<ServiceInfo> listOperations(String serviceURL) throws UnsupportedOperationException;
}
