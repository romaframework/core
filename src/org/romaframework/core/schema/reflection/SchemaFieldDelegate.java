package org.romaframework.core.schema.reflection;

import java.lang.reflect.InvocationTargetException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.romaframework.core.binding.BindingException;
import org.romaframework.core.exception.ConfigurationException;
import org.romaframework.core.schema.SchemaClassDefinition;
import org.romaframework.core.schema.SchemaField;

public class SchemaFieldDelegate extends SchemaFieldReflection {

	private static Log				log								= LogFactory.getLog(SchemaFieldDelegate.class);
	private static final long	serialVersionUID	= 2722942571708547502L;

	private SchemaField				object;
	private SchemaField				delegate;

	public SchemaFieldDelegate(SchemaClassDefinition iEntity, SchemaField object, SchemaField delegate) {
		super(iEntity, delegate.getName());
		this.object = object;
		this.delegate = delegate;
		this.parent = delegate;
		SchemaField sf = iEntity.getField(delegate.getName());
		if (sf instanceof SchemaFieldReflection) {
			SchemaFieldReflection sfr = (SchemaFieldReflection) sf;
			this.getterMethod = sfr.getterMethod;
			this.setterMethod = sfr.setterMethod;
			this.field = sfr.field;
		} else if (iEntity.getSchemaClass() instanceof SchemaClassReflection) {
			SchemaClassReflection ref = (SchemaClassReflection) iEntity.getSchemaClass();
			String methodName = SchemaClassReflection.SET_METHOD + Character.toUpperCase(name.charAt(0)) + name.substring(1);
			try {
				this.setterMethod = ref.getLanguageType().getMethod(methodName, (Class<?>) delegate.getType().getSchemaClass().getLanguageType());
			} catch (SecurityException e) {
				throw new ConfigurationException("Error on find method:" + methodName, e);
			} catch (NoSuchMethodException e) {
				log.debug("Error on find method:" + methodName, e);
			}
		}
	}

	@Override
	public Object getValue(Object iObject) throws BindingException {
		if (this.getterMethod != null) {
			return super.getValue(iObject);
		}
		iObject = this.object.getValue(iObject);
		return this.delegate.getValue(iObject);
	}

	@Override
	public boolean isArray() {
		return this.delegate.isArray();
	}

	@Override
	public Class<?> getLanguageType() {
		return (Class<?>) this.delegate.getLanguageType();
	}

	public SchemaField getDelegate() {
		return delegate;
	}

	@Override
	protected void setValueFinal(Object iObject, Object iValue) throws IllegalAccessException, InvocationTargetException {
		if (this.setterMethod != null) {
			super.setValueFinal(iObject, iValue);
			return;
		}
		iObject = this.object.getValue(iObject);
		this.delegate.setValue(iObject, iValue);
	}

}
