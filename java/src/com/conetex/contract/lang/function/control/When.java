package com.conetex.contract.lang.function.control;

import java.util.List;

import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public class When<V> extends ReturnAbstract<V>{

	final Accessible<Boolean> condition;

	final Accessible<?>[] stepsIf;

	final List<ReturnAbstract<V>> returnsIf;

	private final Class<V> rawTypeClass;

	public static <SV> When<SV> create(Accessible<?>[] theStepsIf, Accessible<Boolean> theCondition, Class<SV> theRawTypeClass) throws CastException {
		if(theStepsIf == null){
			System.err.println("theStepsIf is null");
			return null;
		}
		if(theCondition == null){
			System.err.println("theName is null");
			return null;
		}
		List<ReturnAbstract<SV>> returns = Function.getReturns(theStepsIf, theRawTypeClass);
        return new When<>(theStepsIf, returns, theCondition, theRawTypeClass);
	}

	When(Accessible<?>[] theStepsIf, List<ReturnAbstract<V>> returns, Accessible<Boolean> theCondition, Class<V> theRawTypeClass) {
		this.stepsIf = theStepsIf;
		this.returnsIf = returns;
		this.condition = theCondition;
		this.rawTypeClass = theRawTypeClass;
	}

	@Override
	public final V getFrom(Structure thisObject) throws AbstractRuntimeException {
		return this.getFrom(thisObject, new Result());
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<V> getRawTypeClass() {
		return this.rawTypeClass;
	}

	public boolean returns() {
        return this.returnsIf.size() > 0;
    }

	@Override
	public V getFrom(Structure thisObject, Result r) throws AbstractRuntimeException {
		Boolean res = this.condition.getFrom(thisObject);
		if(res == null){
			System.err.println("Function Structure getFrom: no access to data for if ... ");
			return null;
		}
		if(res.booleanValue()){
			return Function.doSteps(this.stepsIf, this.returnsIf, r, thisObject);
		}
		return null;
	}
}
