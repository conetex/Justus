package com.conetex.contract.data;

import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public interface Value<T> {

	public T get();

	public T copy() throws Invalid;

	public T set(T value) throws Invalid;

	public T setObject(Object value) throws Invalid, ValueCastException;

	public T setConverted(String value) throws Inconvertible, Invalid;

	public Class<T> getRawTypeClass();

}
