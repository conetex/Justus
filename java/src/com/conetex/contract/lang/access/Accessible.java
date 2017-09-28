package com.conetex.contract.lang.access;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.runtime.exceptionValue.Invalid;
import com.conetex.contract.runtime.exceptionValue.ValueCastException;

public abstract class Accessible<T> {// implements Accessible<T> {

	public abstract T getFrom(Structure thisObject) throws ValueCastException;

	public abstract T copyFrom(Structure thisObject) throws Invalid, ValueCastException;

	public abstract Class<T> getRawTypeClass();

	public final Accessible<T> _is(Class<?> rawType) {
		if (rawType == this.getRawTypeClass()) {
			return this;
		}
		return null;
	}

}
