package org.romaframework.core.schema.virtual;

import java.lang.reflect.InvocationTargetException;

import org.romaframework.core.Roma;

public class VirtualObjectWrapper {

	protected VirtualObject	realObject;

	public VirtualObjectWrapper(VirtualObject iVObject) {
		realObject = iVObject;
	}

	public Object getPojo() {
		return realObject;
	}

	public Object getParent() {
		return realObject.getSuperClassObject();
	}

	public Object field(String iName) {
		return realObject.getClazz().getField(iName).getValue(realObject);
	}

	public VirtualObjectWrapper field(String iName, Object iValue) {
		realObject.getClazz().getField(iName).setValue(realObject, iValue);
		Roma.fieldChanged(realObject, iName);
		return this;
	}

	public Object action(String iName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return realObject.getClazz().getAction(iName).invoke(realObject);
	}

	public Object event(String iName) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		return realObject.getClazz().getEvent(iName).invoke(realObject);
	}

	public void refresh() {
		Roma.objectChanged(realObject);
	}

	public void refresh(String iName) {
		Roma.fieldChanged(realObject, iName);
	}

	public void refresh(String[] iName) {
		Roma.fieldChanged(realObject, iName);
	}
}
