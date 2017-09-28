package com.conetex.contract.lang.control;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.AccessibleValue;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class FunctionCall<V> extends Accessible<V> { // V extends Value<?>

	public static <SV> FunctionCall<SV> create(Accessible<SV> theFunction, AccessibleValue<Structure> theReference) {
		// TODO drop this
		if (theFunction == null) {
			System.err.println("theFunction is null");
			return null;
		}
		if (theReference == null) {
			System.err.println("theReference is null");
			return null;
		}
		return new FunctionCall<>(theFunction, theReference);
	}

	private Accessible<V> function;

	private AccessibleValue<Structure> reference;

	@Override
	public String toString() {
		return "call function " + this.function;
	}

	private FunctionCall(Accessible<V> theExpression, AccessibleValue<Structure> theReference) {
		this.function = theExpression;
		this.reference = theReference;
	}

	@Override
	public V getFrom(Structure thisObject) throws ValueCastException {

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
