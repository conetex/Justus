package com.conetex.contract.lang.access;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.runtime.exceptionValue.Invalid;
import com.conetex.contract.runtime.exceptionValue.ValueCastException;

public interface Setable<T> {

	public Class<T> getRawTypeClass();

	public T setTo(Structure thisObject, T value) throws Invalid, ValueCastException;

	// public <X> Setable<X> asSetable(Setable<?> thisObj, Class<X> rawType);

}
