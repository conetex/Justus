package com.conetex.contract.lang.value;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;
import com.conetex.contract.runNew.Writer;

public interface Value<T> {

	T get();

	T getCopy() throws Invalid;

	T set(T value) throws Invalid;

	void setObject(Object value) throws Invalid, ValueCastException;

	void setConverted(String value) throws Inconvertible, Invalid;

	Class<T> getRawTypeClass();

	Value<T> cloneValue() throws Invalid;

	CodeNode createCodeNode(Attribute<?> a) throws UnknownCommandParameter, UnknownCommand;
	
}
