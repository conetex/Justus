package com.conetex.contract.lang.function.control;

import java.util.List;

import com.conetex.contract.lang.function.access.Accessible;
import com.conetex.contract.lang.function.access.AccessibleValue;
import com.conetex.contract.lang.function.assign.AbstractAssigment;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexOfFunction;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public class FunctionCall<V> extends Accessible<V>{ // V extends Value<?>

	public static <SV> FunctionCall<SV> create(Function<SV> theFunction, AccessibleValue<Structure> theReference,
			List<AbstractAssigment<?>> assig) {
		// TODO drop this
		if(theFunction == null){
			System.err.println("theFunction is null");
			return null;
		}
		if(theReference == null){
			System.err.println("theReference is null");
			return null;
		}
		if(assig == null){
			System.err.println("params is null");
			return null;
		}
		return new FunctionCall<>(theFunction, theReference, assig);
	}

	private final Function<V> function;

	private final AccessibleValue<Structure> reference;

	private final List<AbstractAssigment<?>> paramAssigments;

	@Override
	public String toString() {
		return "call function " + this.function;
	}

	private FunctionCall(Function<V> theExpression, AccessibleValue<Structure> theReference, List<AbstractAssigment<?>> assig) {
		this.function = theExpression;
		this.reference = theReference;
		this.paramAssigments = assig;
	}

	@Override
	public V getFrom(Structure thisObject) throws AbstractRuntimeException {
		// block hier ...

		Structure obj = this.reference.getFrom(thisObject);
		if(obj == null){
			System.err.println("Call getFrom ERROR");
			return null;
		}

		System.out.println("Function getFrom " + this.function.getName() + " - " + this.reference.getPath());
		TypeComplex x = obj.getComplex();// .getInstance(this.name);
		// Attribute<?> y = x.getFunctionAttribute(this.function.name);
		// TODO der cast ist scheisse
		// ComplexFunction z =
		// x.getComplexFunction(this.function.name);//(ComplexFunction)(y.getType());
		TypeComplexOfFunction z = TypeComplexOfFunction.getInstance(x.getName() + "." + this.function.getName());
		return getFromComplexFun(z, obj);

		// return this.function.getFrom(thisObject);
	}

	private V getFromComplexFun(TypeComplexOfFunction z, Structure obj) throws AbstractRuntimeException {

		Structure thisObject = z.utilizeStructure(obj); // .prototype;//thisObject.getStructure(this.name);

		if(thisObject == null){
			System.err.println("Function Structure getFrom: no access to data for function " + this.function.getName());
			return null;
		}
		// thisObjectB = thisObjectB.copy();

		for(AbstractAssigment<?> a : this.paramAssigments){
			a.getFrom(thisObject);
		}

		V re = this.function.getFrom(thisObject);

		z.unutilizeStructure(thisObject);
		return re;
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
