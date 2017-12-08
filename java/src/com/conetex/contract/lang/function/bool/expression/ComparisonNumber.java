package com.conetex.contract.lang.function.bool.expression;

import java.math.BigInteger;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.AccessibleWithChildren;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class ComparisonNumber extends Accessible<Boolean>{
	// TODO: eigentlich doof, dass hier doch erst zur Laufzeit ueber den
	// tats�chlichen typ entschieden wird.

	private static final int	SMALLER	= -1;
	private static final int	EQUAL	= 0;
	private static final int	GREATER	= 1;

	public static ComparisonNumber create(Accessible<? extends Number> theA, Accessible<? extends Number> theB, String operation) {
		if(theA == null || theB == null){
			return null;
		}
		if(operation.equals(Symbols.comSmaller())){
			return create(theA, theB, operation, ComparisonNumber.SMALLER);
		}
		if(operation.equals(Symbols.comEqual())){
			return create(theA, theB, operation, ComparisonNumber.EQUAL);
		}
		if(operation.equals(Symbols.comGreater())){
			return create(theA, theB, operation, ComparisonNumber.GREATER);
		}
		return null;
	}

	private static ComparisonNumber create(Accessible<? extends Number> theA, Accessible<? extends Number> theB, String command, int operation) {
		if(theA == null || theB == null){
			return null;
		}
		if(operation < ComparisonNumber.SMALLER || operation > ComparisonNumber.GREATER){
			return null;
		}
		return new ComparisonNumber(theA, theB, operation);
	}

	@Override
	public String getCommand() {
		if(this.operator == ComparisonNumber.SMALLER){
			return Symbols.comSmaller();
		}
		if(this.operator == ComparisonNumber.EQUAL){
			return Symbols.comEqual();
		}
		if(this.operator == ComparisonNumber.GREATER){
			return Symbols.comGreater();
		}
		return null;
	}
	
	private final int operator;

	private final Accessible<? extends Number> a;

	private final Accessible<? extends Number> b;

	// private Comparison(Accessible<T> theA, Accessible<T> theB, int
	// theOperation){
	private ComparisonNumber(Accessible<? extends Number> theA, Accessible<? extends Number> theB, int theOperation) {
		super();
		this.a = theA;
		this.b = theB;
		this.operator = theOperation;
	}

	@Override
	public Accessible<?>[] getChildren() {
		return new Accessible<?>[] {this.a, this.b};
	}

	@Override
	public String[] getParameter() {
		return super.getParameterDft();
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
		Number aN = this.a.getFrom(thisObject);
		Number bN = this.b.getFrom(thisObject);
		if(aN == null){
			if(bN == null && this.operator == ComparisonNumber.EQUAL){
				return Boolean.TRUE;
			}
			return null;
		}
		if(bN == null){
			return null;
		}

		if(aN.getClass() == BigInteger.class){
			if(bN instanceof BigInteger){
				BigInteger aConcret = (BigInteger) aN;
				BigInteger bConcret = (BigInteger) bN;
				return comp(aConcret, bConcret);
			}
			else{
				BigInteger aConcret = (BigInteger) aN;
				BigInteger bConcret = (BigInteger.valueOf(bN.longValue()));
				return comp(aConcret, bConcret);
			}
		}
		else{
			if(bN.getClass() == BigInteger.class){
				BigInteger aConcret = (BigInteger.valueOf(aN.longValue()));
				BigInteger bConcret = (BigInteger) bN;
				return comp(aConcret, bConcret);
			}
			else{
				if(aN.getClass() == Long.class){
					if(bN.getClass() == Long.class){
						Long aConcret = (Long) aN;
						Long bConcret = (Long) bN;
						return comp(aConcret, bConcret);
					}
					else{
						Long aConcret = (Long) aN;
						Long bConcret = Long.valueOf(bN.longValue());
						return comp(aConcret, bConcret);
					}
				}
				else{
					if(bN.getClass() == Long.class){
						Long aConcret = Long.valueOf(aN.longValue());
						Long bConcret = (Long) bN;
						return comp(aConcret, bConcret);
					}
					else{
						Integer aConcret = Integer.valueOf(aN.intValue());
						Integer bConcret = Integer.valueOf(bN.intValue());
						return comp(aConcret, bConcret);
					}
				}
			}
		}

	}

	private <T extends Comparable<T>> Boolean comp(T aA, T aB) {
		if(this.operator == ComparisonNumber.GREATER){
			if(aA.compareTo(aB) > 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		if(this.operator == ComparisonNumber.SMALLER){
			if(aA.compareTo(aB) < 0){
				return Boolean.TRUE;
			}
			return Boolean.FALSE;
		}
		if(this.operator == ComparisonNumber.EQUAL){
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





}
