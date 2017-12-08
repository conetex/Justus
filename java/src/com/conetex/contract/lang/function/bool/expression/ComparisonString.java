package com.conetex.contract.lang.function.bool.expression;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.AccessibleWithChildren;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class ComparisonString extends AccessibleWithChildren<Boolean>{

	private static final int	SMALLER	= -1;
	private static final int	EQUAL	= 0;
	private static final int	GREATER	= 1;

	public static ComparisonString create(Accessible<String> theA, Accessible<String> theB, String operation) {
		if(theA == null || theB == null){
			return null;
		}
		if(operation.equals(Symbols.comSmaller())){
			return create(theA, theB, ComparisonString.SMALLER);
		}
		if(operation.equals(Symbols.comEqual())){
			return create(theA, theB, ComparisonString.EQUAL);
		}
		if(operation.equals(Symbols.comGreater())){
			return create(theA, theB, ComparisonString.GREATER);
		}
		return null;
	}

	private static ComparisonString create(Accessible<String> theA, Accessible<String> theB, int operation) {
		if(theA == null || theB == null){
			return null;
		}
		if(operation < ComparisonString.SMALLER || operation > ComparisonString.GREATER){
			return null;
		}
		return new ComparisonString(theA, theB, operation);
	}

	private final int operator;

	private final Accessible<String> a;

	private final Accessible<String> b;

	// private Comparison(Accessible<T> theA, Accessible<T> theB, int
	// theOperation){
	private ComparisonString(Accessible<String> theA, Accessible<String> theB, int theOperation) {
		super(new Accessible<?>[] {theA, theB});
		this.a = theA;
		this.b = theB;
		this.operator = theOperation;
	}

	// public Comparison(Accessible<Comparable<?>> theA,
	// Accessible<Comparable<?>>
	// theB, int theOperation) {
	/*
	 * public Comparison(Accessible<Comparable<?>> theA, Accessible<Comparable<?>>
	 * theB, int theOperation) { super(theA, theB); this.operator = theOperation; }
	 */

	@Override
	public Boolean getFrom(Structure thisObject) throws AbstractRuntimeException {
		String aN = this.a.getFrom(thisObject);
		String bN = this.b.getFrom(thisObject);
		if(aN == null){
			if(bN == null && this.operator == ComparisonString.EQUAL){
				return Boolean.TRUE;
			}
			return null;
		}
		else if(bN == null){
			return null;
		}

		return comp(aN, bN);
	}

	private <T extends Comparable<T>> Boolean comp(T aA, T aB) {
		if(this.operator == ComparisonString.GREATER){
			if(aA.compareTo(aB) > 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		if(this.operator == ComparisonString.SMALLER){
			if(aA.compareTo(aB) < 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		if(this.operator == ComparisonString.EQUAL){
			if(aA.compareTo(aB) == 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		return null;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) throws AbstractRuntimeException {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

	@Override
	public String getCommand() {
		if(this.operator == ComparisonString.SMALLER){
			return Symbols.comSmaller();
		}
		if(this.operator == ComparisonString.EQUAL){
			return Symbols.comEqual();
		}
		if(this.operator == ComparisonString.GREATER){
			return Symbols.comGreater();
		}
		return null;
	}

}
