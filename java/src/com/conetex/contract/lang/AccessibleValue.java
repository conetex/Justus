package com.conetex.contract.lang;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public class AccessibleValue<T> extends Accessible<T> {

	public static <T> AccessibleValue<T> create(String thePath, Class<T> theClass) {
		if (thePath == null) {
			return null;
		}
		return new AccessibleValue<>(thePath, theClass);
	}

	public static <T extends Number> AccessibleValue<T> _createNum(String thePath, Class<T> theClass) {
		if (thePath == null) {
			System.err.println("thePath is null");
			return null;
		}
		return new AccessibleValue<>(thePath, theClass);
	}

	protected String path;

	protected final Class<T> clazz;

	protected AccessibleValue(String thePath, Class<T> theClass) {
		this.path = thePath;
		this.clazz = theClass;
	}

	@Override
	public T getFrom(Structure thisObject) {

		// return thisObject.getValueNewNew(this.path, this.clazz);

		Value<T> value = thisObject.getValueNew(this.path, this.clazz);
		if (value == null) {
			return null;
		}
		return value.get();
	}

	public T setTo(Structure thisObject, T newValue) throws Invalid {
		Value<T> value = thisObject.getValueNew(this.path, this.clazz);
		if (value == null) {
			return null;
		}
		// just 4 debug:
		// T valueOld = value.get();
		return value.set(newValue);
		// just 4 debug:
		// value = thisObject.getValueNew(this.path, this.clazz);
		// System.out.println(valueOld + " setTo " + newValue + " -> " + value.get());
		// return newValue;
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		Value<T> value = thisObject.getValueNew(this.path, this.clazz);
		if (value == null) {
			return null;
		}
		return value.copy();
	}

	@Override
	public Class<T> getBaseType() {
		return this.clazz;
	}

}
