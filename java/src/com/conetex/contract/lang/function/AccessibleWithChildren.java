package com.conetex.contract.lang.function;

import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public abstract class AccessibleWithChildren<T> extends AccessibleImp<T> {

	private Accessible<?>[] children;

	protected AccessibleWithChildren(Accessible<?>[] theChildren) {
		super();
		if (theChildren.length == 0) {
			System.err.println("das darf nicht sein...");// TODO exception ...
		}
		this.children = theChildren;
	}

	public Accessible<?>[] getChildren() {
		return this.children;
	}

	@Override
	public abstract String getCommand();

	@Override
	public abstract T getFrom(Structure thisObject) throws AbstractRuntimeException;

	@Override
	public abstract T copyFrom(Structure thisObject) throws Invalid, AbstractRuntimeException;

	@Override
	public abstract Class<T> getRawTypeClass();

}
