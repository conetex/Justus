package com.conetex.contract.lang.value;

import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;
import com.conetex.contract.runNew.Writer;

public interface Value<T> {

	public T get();

	public T getCopy() throws Invalid;

	public T set(T value) throws Invalid;

	public T setObject(Object value) throws Invalid, ValueCastException;

	public T setConverted(String value) throws Inconvertible, Invalid;

	public Class<T> getRawTypeClass();

	public Value<T> cloneValue() throws Invalid;

	public void persist(Writer w, Attribute<?> a) throws UnknownCommandParameter, UnknownCommand;
	
}
