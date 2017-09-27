package com.conetex.contract.lang.access;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public interface Setable<T> {

	public Class<T> getRawTypeClass();

	public T setTo(Structure thisObject, T value) throws Invalid;

	//public <X> Setable<X> asSetable(Setable<?> thisObj, Class<X> rawType);

}
