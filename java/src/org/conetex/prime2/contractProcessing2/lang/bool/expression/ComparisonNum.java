package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Pair;
import org.conetex.prime2.contractProcessing2.lang.Symbol;

public class ComparisonNum extends Pair<Number> implements Accessible<Boolean>{
	
	public static final int SMALLER = -1;
	public static final int EQUAL = 0;
	public static final int GREATER = 1;
	
	/*
	public static <V extends Number & Comparable<V>> Comparison<V> createSmaller(Accessible<V> theA, Accessible<V> theB){
		return Comparison.<V>create(theA, theB, Comparison.SMALLER);
	}
	
	public static <V extends Number & Comparable<V>> Comparison<V> createEqual(Accessible<V> theA, Accessible<V> theB){
		return Comparison.<V>create(theA, theB, Comparison.GREATER);
	}
	
	public static <V extends Number & Comparable<V>> Comparison<V> createGreater(Accessible<V> theA, Accessible<V> theB){
		return Comparison.<V>create(theA, theB, Comparison.GREATER);
	}
	*/
	
	public static ComparisonNum create(Accessible<Number> theA, Accessible<Number> theB, String operation){
		Class c = theA.getBaseType();
		if(theA == null || theB == null){
			return null;
		}
		if( operation.equals(Symbol.SMALLER) ) {
			return create(theA, theB, ComparisonNum.SMALLER );
		}
		if( operation.equals(Symbol.EQUAL) ) {
			return create(theA, theB, ComparisonNum.EQUAL );
		}
		if( operation.equals(Symbol.GREATER) ) {
			return create(theA, theB, ComparisonNum.GREATER );
		}
		return null;
	}
	
	public static ComparisonNum create(Accessible<Number> theA, Accessible<Number> theB, int operation){
		if(theA == null || theB == null){
			return null;
		}
		if(operation < ComparisonNum.SMALLER || operation > ComparisonNum.GREATER){
			return null;
		}		
		return new ComparisonNum(theA, theB, operation);
	}	


			
	private int operator;
	
	//private Comparison(Accessible<T> theA, Accessible<T> theB, int theOperation){
	private ComparisonNum(Accessible<Number> a, Accessible<Number> b, int theOperation){
		super(a, b);
		this.operator = theOperation;
	}

	//public Comparison(Accessible<Comparable<?>> theA, Accessible<Comparable<?>> theB, int theOperation) {
	/*
	public Comparison(Accessible<Comparable<?>> theA, Accessible<Comparable<?>> theB, int theOperation) {
		super(theA, theB);
		this.operator = theOperation;
	}
	*/

	@Override
	public Boolean getFrom(Structure thisObject) {
		Number aN = super.getA().getFrom(thisObject);
		Number bN = super.getB().getFrom(thisObject);
		if( aN == null ){
			if( bN == null && this.operator == ComparisonNum.EQUAL ){
				return Boolean.TRUE;
			}
			return null;
		}
		if( bN == null ){
			return null;
		}	
		
		Integer a = aN.intValue();
		Integer b = bN.intValue();		
		if( this.operator == ComparisonNum.GREATER ){
			if(a.compareTo(b) > 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
		if( this.operator == ComparisonNum.SMALLER ){
			if(a.compareTo(b) < 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
        if( this.operator == ComparisonNum.EQUAL ){
			if(a.compareTo(b) == 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}		
		return null;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}


	
}
