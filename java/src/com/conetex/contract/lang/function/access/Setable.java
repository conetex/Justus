package com.conetex.contract.lang.function.access;

import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public abstract class Setable<T> extends Accessible<T>{

	protected Setable(String theCommand, String[] theParameter, Accessible<?>[] theChildren) {
		super(theCommand, theParameter, theChildren);
	}

	public abstract T setTo(Structure thisObject, T value) throws Invalid, ValueCastException;

	// public <X> Setable<X> asSetable(Setable<?> thisObj, Class<X> rawType);

}
