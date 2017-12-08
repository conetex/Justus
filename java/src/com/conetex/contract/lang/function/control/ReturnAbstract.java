package com.conetex.contract.lang.function.control;

import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.AccessibleWithChildren;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class ReturnAbstract<T> extends AccessibleWithChildren<T>{

	protected ReturnAbstract(Accessible<?>[] theChildren) {
		super(theChildren);
	}

	public static class Result{
		public boolean toReturn = false;
	}

	public abstract T getFrom(Structure thisObject, Result r) throws AbstractRuntimeException;

}
