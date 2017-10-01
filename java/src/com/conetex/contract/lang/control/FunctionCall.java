package com.conetex.contract.lang.control;

import java.util.List;

import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.AccessibleValue;
import com.conetex.contract.lang.assign.AbstractAssigment;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public class FunctionCall<V> extends Accessible<V> { // V extends Value<?>

	public static <SV> FunctionCall<SV> create(Accessible<SV> theFunction, AccessibleValue<Structure> theReference, List<AbstractAssigment<? extends Object>> assig) {
		// TODO drop this
		if (theFunction == null) {
			System.err.println("theFunction is null");
			return null;
		}
		if (theReference == null) {
			System.err.println("theReference is null");
			return null;
		}
		if (assig == null) {
			System.err.println("params is null");
			return null;
		}		
		return new FunctionCall<>(theFunction, theReference, assig);
	}

	private Accessible<V> function;

	private AccessibleValue<Structure> reference;

	private List<AbstractAssigment<? extends Object>> paramAssigments;
	
	@Override
	public String toString() {
		return "call function " + this.function;
	}

	private FunctionCall(Accessible<V> theExpression, AccessibleValue<Structure> theReference, List<AbstractAssigment<? extends Object>> assig) {
		this.function = theExpression;
		this.reference = theReference;
		this.paramAssigments = assig;
	}

	@Override
	public V getFrom(Structure thisObject) throws AbstractRuntimeException {

		for(AbstractAssigment<? extends Object> a : this.paramAssigments){
			a.getFrom(thisObject);
		}
		Structure obj = this.reference.getFrom(thisObject);
		if (obj == null) {
			System.err.println("Call getFrom ERROR");
			return null;
		}
		return this.function.getFrom(obj);

		// return this.function.getFrom(thisObject);
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO copyFrom is obsolet
		return null;
	}

	@Override
	public Class<V> getRawTypeClass() {
		return this.function.getRawTypeClass();
	}

}
