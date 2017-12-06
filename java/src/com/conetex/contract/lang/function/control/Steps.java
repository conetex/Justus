package com.conetex.contract.lang.function.control;

import java.util.List;

import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.control.ReturnAbstract.Result;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class Steps<T> extends Accessible<T>{

	private final Accessible<?>[] steps;
	
	private final List<ReturnAbstract<T>> returns;
	
	Steps(String theCommand, Accessible<?>[] theSteps, List<ReturnAbstract<T>> theReturns){
		super(theCommand, new String[]{}, theSteps);
		this.steps = theSteps;
		this.returns = theReturns;
	}

	public static <SV> Steps<SV> create(String theCommand, Accessible<?>[] theSteps, Class<SV> theRawTypeClass) throws CastException {
		if(theSteps == null){
			System.err.println("theSteps is null");
			return null;
		}
		List<ReturnAbstract<SV>> returns = Function.getReturns(theSteps, theRawTypeClass);
        return new Steps<>(theCommand, theSteps, returns);
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
