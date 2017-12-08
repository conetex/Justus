package com.conetex.contract.lang.function;

import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public abstract class AccessibleWithChildrenAndParams<T> extends AccessibleWithChildren<T> {

	private String[] parameter;
	
	protected AccessibleWithChildrenAndParams(String[] theParameter, Accessible<?>[] theChildren) {
		super(theChildren);
		this.parameter = theParameter;
	}

	public String[] getParameter() {
		return this.parameter;
	}
	
	@Override
	public abstract String getCommand();
	
	@Override
	public abstract T getFrom(Structure thisObject) throws AbstractRuntimeException;

	@Override
	public abstract T copyFrom(Structure thisObject) throws Invalid;

	@Override
	public abstract Class<T> getRawTypeClass();

}
