package org.romaframework.core.factory;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.Roma;
import org.romaframework.core.schema.SchemaClass;
import org.romaframework.core.schema.SchemaHelper;

@SuppressWarnings("unchecked")
public abstract class AbstractFactory<T> implements GenericFactory<T> {

	private static Log	log	= LogFactory.getLog(AbstractFactory.class);

	public T createInstance(Object... iArgs) {
		if (iArgs == null || iArgs.length == 0)
			return create();

		Class<?>[] parameterTypes = new Class<?>[iArgs.length];
		for (int i = 0; i < iArgs.length; ++i)
			// TODO: supports the null parameters in a smarter way
			parameterTypes[i] = iArgs[i] != null ? iArgs[i].getClass() : Object.class;

		try {
			Method c = getClass().getMethod("create", parameterTypes);
			return (T) c.invoke(this, iArgs);
		} catch (Exception e) {
			log.error("[AbstractFactory.create(...)] Error in invoking custom create method", e);
		}
		return null;
	}

	public T create() {
		T instance = null;
		Class<T> cl = getEntityClass();
		if (cl != null) {
			try {
				instance = (T) cl.newInstance();
			} catch (Exception e) {
				log.error("[AbstractFactory.create()] Error in invoking default constructor", e);
			}
		}
		return instance;
	}

	public <Z extends T> Class<Z> getEntityClass(){
		return (Class<Z>) SchemaHelper.getGenericClass(getClass().getGenericSuperclass());
	}

	public SchemaClass getEntitySchemaClass() {
		return Roma.schema().getSchemaClass(getEntityClass());
	}
}
