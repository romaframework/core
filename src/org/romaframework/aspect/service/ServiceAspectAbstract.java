package org.romaframework.aspect.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.aspect.core.annotation.AnnotationConstants;
import org.romaframework.aspect.service.annotation.ServiceClass;
import org.romaframework.aspect.service.feature.ServiceClassFeatures;
import org.romaframework.core.Roma;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.module.SelfRegistrantConfigurableModule;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaClassElement;
import org.romaframework.core.schema.SchemaEvent;
import org.romaframework.core.schema.SchemaField;
import org.romaframework.core.schema.reflection.SchemaClassReflection;
import org.romaframework.core.schema.xmlannotations.XmlActionAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlAspectAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlClassAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlEventAnnotation;
import org.romaframework.core.schema.xmlannotations.XmlFieldAnnotation;
import org.romaframework.core.util.DynaBean;

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
		DynaBean features = iClass.getFeatures(ASPECT_NAME);
		if (features == null) {
			// CREATE EMPTY FEATURES
			features = new ServiceClassFeatures();
			iClass.setFeatures(ASPECT_NAME, features);
		}

		readClassAnnotation(iAnnotation, features);
		readClassXml(iClass, iXmlNode);

	}

	private void readClassXml(SchemaClassDefinition iClass, XmlClassAnnotation iXmlNode) {
		DynaBean features = iClass.getFeatures(ASPECT_NAME);

		if (iXmlNode == null || iXmlNode.aspect(ASPECT_NAME) == null)
			return;

		XmlAspectAnnotation featureDescriptor = iXmlNode.aspect(ASPECT_NAME);

		if (featureDescriptor != null) {

			// PROCESS DESCRIPTOR CFG
			if (featureDescriptor != null) {
				String interfaceClass = featureDescriptor.getAttribute(ServiceClassFeatures.INTERFACE_CLASS);
				if (interfaceClass != null) {
					features.setAttribute(ServiceClassFeatures.INTERFACE_CLASS,
							((SchemaClassReflection) Roma.schema().getSchemaClass(interfaceClass)).getLanguageType());
				}

				String serviceName = featureDescriptor.getAttribute(ServiceClassFeatures.SERVICE_NAME);
				if (serviceName != null) {
					features.setAttribute(ServiceClassFeatures.SERVICE_NAME, serviceName);
				}

				String aspectImpl = featureDescriptor.getAttribute(ServiceClassFeatures.ASPECT_IMPLEMENTATION);
				if (aspectImpl != null) {
					try {
						features.setAttribute(ServiceClassFeatures.ASPECT_IMPLEMENTATION, Class.forName(aspectImpl));
					} catch (ClassNotFoundException e) {
						log.error("[ServiceAspectAbstract.readClassXml] Aspect implementation " + aspectImpl + " not found in classpath");
					}
				}
			}
		}
	}

	private void readClassAnnotation(Annotation iAnnotation, DynaBean features) {
		ServiceClass annotation = (ServiceClass) iAnnotation;

		if (annotation != null) {
			// PROCESS ANNOTATIONS
			if (!annotation.interfaceClass().equals(Object.class))
				features.setAttribute(ServiceClassFeatures.INTERFACE_CLASS, annotation.interfaceClass());
			if (!annotation.serviceName().equals(AnnotationConstants.DEF_VALUE))
				features.setAttribute(ServiceClassFeatures.SERVICE_NAME, annotation.serviceName());
			if (annotation.aspectImplementation() != Object.class)
				features.setAttribute(ServiceClassFeatures.ASPECT_IMPLEMENTATION, annotation.aspectImplementation());
		}

	}

	public void configAction(SchemaClassElement iAction, Annotation iActionAnnotation, Annotation iGenericAnnotation,
			XmlActionAnnotation iNode) {
	}

	public void configEvent(SchemaEvent iEvent, Annotation iEventAnnotation, Annotation iGenericAnnotation, XmlEventAnnotation iNode) {
	}

	public void configField(SchemaField iField, Annotation iFieldAnnotation, Annotation iGenericAnnotation,
			Annotation iGetterAnnotation, XmlFieldAnnotation iNode) {
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
	protected Object createServiceInstance(SchemaClass implementation) throws IllegalArgumentException, SecurityException,
			InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		Class<?> intClass = (Class<?>) implementation.getFeature(ASPECT_NAME, ServiceClassFeatures.INTERFACE_CLASS);

		if (intClass == null)
			throw new ConfigurationException("Class " + implementation
					+ " must implement an interface as service. Unable to create security rules.");

		Object realInstance = implementation.newInstance();
		InvocationHandler handler = new ServiceInvocationHandler(realInstance);
		Object instance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class<?>[] { intClass }, handler);
		return instance;
	}

}
