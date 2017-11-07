package com.conetex.contract.lang.function.control;

import java.util.List;

import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.lang.function.access.Accessible;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class Loop<V> extends When<V>{

	public static <SV> Loop<SV> create(Accessible<?>[] theStepsIf, Accessible<Boolean> theCondition, Class<SV> theRawTypeClass) throws CastException {
		if(theStepsIf == null){
			System.err.println("theStepsIf is null");
			return null;
		}
		if(theCondition == null){
			System.err.println("theName is null");
			return null;
		}
		List<ReturnAbstract<SV>> returns = Function.getReturns(theStepsIf, theRawTypeClass);
		return new Loop<>(theStepsIf, returns, theCondition, theRawTypeClass);
	}

	private Loop(Accessible<?>[] theStepsIf, List<ReturnAbstract<V>> returns, Accessible<Boolean> theCondition, Class<V> theRawTypeClass) {
		super(theStepsIf, returns, theCondition, theRawTypeClass);
	}

	@Override
	public V getFrom(Structure thisObject, Result r) throws AbstractRuntimeException {
		Boolean res = this.condition.getFrom(thisObject);
		while(res != null && res){
			V re = Function.doSteps(this.stepsIf, this.returnsIf, r, thisObject);
			if(r.toReturn){
				return re;
			}
			res = this.condition.getFrom(thisObject);
		}
		if(res == null){
			System.err.println("Function Structure getFrom: no access to data for if ... ");
			return null;
		}
		return null;
	}

}
