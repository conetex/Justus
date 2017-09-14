package com.conetex.contract.lang;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public interface Accessible2<T> {

	public T getFrom(Structure thisObject);

	public T copyFrom(Structure thisObject) throws Invalid;

	public Class<T> getBaseType();

	public Accessible2<T> _is(Class<?> rawType);

	public <B> Accessible2<B> as(Class<B> rawType);
}
