package com.conetex.contract.lang.function;

import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public abstract class AccessibleWithParams<T> extends AccessibleImp<T> {

	private String[] parameter;
	
	protected AccessibleWithParams(String[] theParameter) {
		super();
		this.parameter = theParameter;
	}

	public String[] getParameter() {
		return this.parameter;
	}
	
	@Override
	public abstract String getCommand();
	
	@Override
	public abstract T getFrom(Structure thisObject) throws ValueCastException;

	@Override
	public abstract T copyFrom(Structure thisObject) throws Invalid, ValueCastException;

	@Override
	public abstract Class<T> getRawTypeClass();

}
