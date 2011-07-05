package org.romaframework.aspect.service;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.romaframework.core.schema.SchemaHelper;

public class ServiceInvocationHandler implements InvocationHandler {

	private Object	instance;

	public ServiceInvocationHandler(Object instance) {
		this.instance = instance;
	}

	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return SchemaHelper.invokeAction(instance, method.getName(), method.getParameterTypes(), args);
	}

}
