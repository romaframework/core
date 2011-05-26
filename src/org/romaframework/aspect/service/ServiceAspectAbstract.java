package org.romaframework.aspect.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.service.feature.ServiceClassFeatures;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;

public abstract class ServiceAspectAbstract extends SelfRegistrantConfigurableModule<String> implements ServiceAspect {
	protected Set<String>	additionalPaths;
	private static Log		log	= LogFactory.getLog(ServiceAspectAbstract.class);

	public String aspectName() {
		return ASPECT_NAME;
	}

	public void beginConfigClass(SchemaClassDefinition iClass) {
	}

	public void endConfigClass(SchemaClassDefinition iClass) {
	}

	public void configClass(SchemaClassDefinition iClass, Annotation iAnnotation, XmlClassAnnotation iXmlNode) {

	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation, XmlActionAnnotation iNode) {
	}

	public void configEvent(SchemaEvent iEvent, Annotation iEventAnnotation, Annotation iGenericAnnotation, XmlEventAnnotation iNode) {
	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation, Annotation iGetterAnnotation, XmlFieldAnnotation iNode) {
	}

	public Set<String> getAdditionalPaths() {
		return additionalPaths;
	}

	public void setAdditionalPaths(Set<String> additionalPaths) {
		this.additionalPaths = additionalPaths;
	}

	/**
	 * Create a new instance of a service.
	 * 
	 * @param implementation
	 *          to instanziate.
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws IllegalArgumentException
	 */
	protected Object createServiceInstance(SchemaClass implementation) throws IllegalArgumentException, SecurityException, InstantiationException,
			IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> intClass = implementation.getFeature(ServiceClassFeatures.INTERFACE_CLASS);

		if (intClass == null)
			throw new ConfigurationException("Class " + implementation + " must implement an interface as service. Unable to create security rules.");

		Object realInstance = implementation.newInstance();
		InvocationHandler handler = new ServiceInvocationHandler(realInstance);
		Object instance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { intClass }, handler);
		return instance;
	}

}
