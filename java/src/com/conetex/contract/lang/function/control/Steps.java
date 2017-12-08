package com.conetex.contract.lang.function.control;

import java.util.List;

import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.AccessibleWithChildren;
import com.conetex.contract.lang.function.control.ReturnAbstract.Result;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class Steps<T> extends AccessibleWithChildren<T>{

	private final Accessible<?>[] steps;
	
	private final List<ReturnAbstract<T>> returns;
	
	Steps(Accessible<?>[] theSteps, List<ReturnAbstract<T>> theReturns){
		super(theSteps);
		this.steps = theSteps;
		this.returns = theReturns;
	}
	
	@Override
	public T getFrom(Structure thisObject) throws AbstractRuntimeException {
		return Function.doSteps(this.steps, this.returns, new Result(), thisObject);
	}
	
	public T getFrom(Structure thisObject, Result r) throws AbstractRuntimeException {
		return Function.doSteps(this.steps, this.returns, r, thisObject);
	}

	@Override
	public T copyFrom(Structure thisObject) throws AbstractRuntimeException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<T> getRawTypeClass() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
