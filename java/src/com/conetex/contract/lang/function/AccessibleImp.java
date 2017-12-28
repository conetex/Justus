package com.conetex.contract.lang.function;

import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class AccessibleImp<T> extends Accessible<T> {

	protected AccessibleImp() {
	}

	public abstract T getFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract T copyFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract Class<T> getRawTypeClass();

	public abstract String getCommand();

	public Accessible<?>[] getChildren() {
		return super.getChildrenDft();
	}

	public String[] getParameter() {
		return super.getParameterDft();
	}

}