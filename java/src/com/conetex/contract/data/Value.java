package com.conetex.contract.data;

import com.conetex.contract.runtime.exceptionValue.Inconvertible;
import com.conetex.contract.runtime.exceptionValue.Invalid;

public interface Value<T> {

	public T get();

	public T copy() throws Invalid;

	public T set(T value) throws Invalid;

	public T setConverted(String value) throws Inconvertible, Invalid;

	public Class<T> getRawTypeClass();

}
