package com.conetex.contract.lang.access;

import com.conetex.contract.data.value.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class Accessible<T> {// implements Accessible<T> {

	public abstract T getFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract T copyFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract Class<T> getRawTypeClass();

	public final Accessible<T> _is(Class<?> rawType) {
		if(rawType == this.getRawTypeClass()){
			return this;
		}
		return null;
	}

}
