package org.romaframework.aspect.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import org.romaframework.core.schema.SchemaClass;

public abstract class UnmanagedServiceAspectAbstract extends ServiceAspectAbstract {

	private Map<String, Object>	instances	= new HashMap<String, Object>();

	@Override
	protected Object createServiceInstance(SchemaClass implementation) throws IllegalArgumentException, SecurityException, InstantiationException, IllegalAccessException,
			InvocationTargetException, NoSuchMethodException {
		Object instance = instances.get(implementation.getName());
		if (instance == null) {
			instance = super.createServiceInstance(implementation);
		}
		return instance;
	}
}
