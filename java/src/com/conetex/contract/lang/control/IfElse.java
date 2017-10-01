package com.conetex.contract.lang.control;

import java.util.List;

import com.conetex.contract.build.exceptionLang.CastException;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public class IfElse<V> extends If<V> {

	private Accessible<?>[] stepsElse;

	private List<ReturnAbstract<V>> returnsElse;

	public static <SV> IfElse<SV> create(Accessible<?>[] theStepsIf, Accessible<?>[] theStepsElse, Accessible<Boolean> theCondition, Class<SV> theRawTypeClass)
			throws CastException {
		if (theStepsIf == null) {
			System.err.println("theStepsIf is null");
			return null;
		}
		if (theStepsElse == null) {
			System.err.println("theStepsElse is null");
			return null;
		}
		if (theCondition == null) {
			System.err.println("theName is null");
			return null;
		}
		List<ReturnAbstract<SV>> returnsIf = Function.getReturns(theStepsIf, theRawTypeClass);
		List<ReturnAbstract<SV>> returnsElse = Function.getReturns(theStepsElse, theRawTypeClass);
		IfElse<SV> re = new IfElse<>(theStepsIf, returnsIf, theStepsElse, returnsElse, theCondition, theRawTypeClass);
		return re;
	}

	private IfElse(Accessible<?>[] theStepsIf, List<ReturnAbstract<V>> theReturnsIf, Accessible<?>[] theStepsElse, List<ReturnAbstract<V>> theReturnsElse,
			Accessible<Boolean> theCondition, Class<V> theRawTypeClass) {
		super(theStepsIf, theReturnsIf, theCondition, theRawTypeClass);
		this.stepsElse = theStepsElse;
		this.returnsElse = theReturnsElse;
	}

	@Override
	public V getFrom(Structure thisObject, Result r) throws AbstractRuntimeException {
		Boolean res = super.condition.getFrom(thisObject);
		if (res == null) {
			System.err.println("Function Structure getFrom: no access to data for if ... ");
			return null;
		}
		if (res.booleanValue()) {
			return Function.doSteps(super.stepsIf, super.returnsIf, r, thisObject);
		}
		else {
			return Function.doSteps(this.stepsElse, this.returnsElse, r, thisObject);
		}
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO Auto-generated method stub
		return null;
	}

}
