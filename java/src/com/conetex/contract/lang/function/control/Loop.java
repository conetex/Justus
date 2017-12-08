package com.conetex.contract.lang.function.control;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.lang.function.Accessible;
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
		//Steps<SV> theSteps = Steps.create(Symbols.comThen(), theStepsIf, theRawTypeClass);
		Steps<SV> theSteps = new Steps<SV>(theStepsIf, Function.getReturns(theStepsIf, theRawTypeClass)){
			@Override
			public String getCommand() {
				return Symbols.comThen();
			}};
        return new Loop<>(theCondition, theSteps, theRawTypeClass);
	}

	Loop(Accessible<Boolean> theCondition, Steps<V> theSteps, Class<V> theRawTypeClass) {
		super(theCondition, theSteps, theRawTypeClass);
	}
	
	@Override
	public V getFrom(Structure thisObject, Result r) throws AbstractRuntimeException {
		Boolean res = this.condition.getFrom(thisObject);
		if(res != null && res.booleanValue()){
			V re = this.steps.getFrom(thisObject, r);
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
