package com.conetex.contract.lang.control;

import java.util.List;

import com.conetex.contract.build.exceptionLang.CastException;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class If<V> extends ReturnAbstract<V> {

	Accessible<Boolean> condition;

	Accessible<?>[] stepsIf;

	List<ReturnAbstract<V>> returnsIf;

	private Class<V> rawTypeClass;

	public static <SV> If<SV> create(Accessible<?>[] theStepsIf, Accessible<Boolean> theCondition, Class<SV> theRawTypeClass) throws CastException {
		if (theStepsIf == null) {
			System.err.println("theStepsIf is null");
			return null;
		}
		if (theCondition == null) {
			System.err.println("theName is null");
			return null;
		}
		List<ReturnAbstract<SV>> returns = Function.getReturns(theStepsIf, theRawTypeClass);
		If<SV> re = new If<>(theStepsIf, returns, theCondition, theRawTypeClass);
		return re;
	}

	If(Accessible<?>[] theStepsIf, List<ReturnAbstract<V>> returns, Accessible<Boolean> theCondition, Class<V> theRawTypeClass) {
		this.stepsIf = theStepsIf;
		this.returnsIf = returns;
		this.condition = theCondition;
		this.rawTypeClass = theRawTypeClass;
	}

	@Override
	public final V getFrom(Structure thisObject) throws ValueCastException {
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
		if (this.returnsIf.size() > 0) {
			return true;
		}
		return false;
	}

	@Override
	public V getFrom(Structure thisObject, Result r) throws ValueCastException {
		Boolean res = this.condition.getFrom(thisObject);
		if (res == null) {
			System.err.println("Function Structure getFrom: no access to data for if ... ");
			return null;
		}
		if (res.booleanValue()) {
			return Function.doSteps(this.stepsIf, this.returnsIf, r, thisObject);
		}
		return null;
	}
}
