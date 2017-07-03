package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.data.values.exception.Inconvertible;

public class AccessibleValue<T> implements Accessible<T> {

	public static <T> AccessibleValue<T> create(String thePath, Class<? extends Value<T>> theClass) {
		if (thePath == null) {
			return null;
		}
		return new AccessibleValue<T>(thePath, theClass);
	}

	private String path;

	private final Class<? extends Value<T>> clazz;

	private AccessibleValue(String thePath, Class<? extends Value<T>> theClass) {
		this.path = thePath;
		this.clazz = theClass;
	}

	@Override
	public T getFrom(Structure thisObject) {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		return value.get();
	}

	@Override
	public void setTo(Structure thisObject, T newValue) throws Invalid {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		value.set(newValue);
	}

	public void transSet(Structure thisObject, String newValue) throws Inconvertible, Invalid {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		value.setConverted(newValue);
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		return value.copy();
	}

}
