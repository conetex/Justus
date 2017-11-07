package com.conetex.contract.lang.function.access;

import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public interface Setable<T> {

	Class<T> getRawTypeClass();

	T setTo(Structure thisObject, T value) throws Invalid, ValueCastException;

	// public <X> Setable<X> asSetable(Setable<?> thisObj, Class<X> rawType);

}
