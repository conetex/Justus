package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Pair;
import org.conetex.prime2.contractProcessing2.lang.Symbol;

public class Comparison<T extends Comparable<T>> extends Pair<T> implements Accessible<Boolean>{
	
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
	
	public static <V extends Comparable<V>> Comparison<V> create(Accessible<V> theA, Accessible<V> theB, String operation){
		if(theA == null || theB == null){
			return null;
		}
		if( operation.equals(Symbol.SMALLER) ) {
			return create(theA, theB, Comparison.SMALLER );
		}
		if( operation.equals(Symbol.EQUAL) ) {
			return create(theA, theB, Comparison.EQUAL );
		}
		if( operation.equals(Symbol.GREATER) ) {
			return create(theA, theB, Comparison.GREATER );
		}
		return null;
	}
	
	public static <V extends Comparable<V>> Comparison<V> create(Accessible<V> theA, Accessible<V> theB, int operation){
		if(theA == null || theB == null){
			return null;
		}
		if(operation < Comparison.SMALLER || operation > Comparison.GREATER){
			return null;
		}		
		return new Comparison<V>(theA, theB, operation);
	}	

	public static <V extends Comparable<V>> Comparison<V> createXX(Accessible<V> a, Accessible<V> b, String operation){
		if(a == null || b == null){
			return null;
		}
		if( operation.equals(Symbol.SMALLER) ) {
			return createXX(a, b, Comparison.SMALLER );
		}
		if( operation.equals(Symbol.EQUAL) ) {
			return createXX(a, b, Comparison.EQUAL );
		}
		if( operation.equals(Symbol.GREATER) ) {
			return createXX(a, b, Comparison.GREATER );
		}
		return null;
	}

	public static <V extends Comparable<V>> Comparison<V> createXX(Accessible<V> a, Accessible<V> b, int operation){
		if(a == null || b == null){
			return null;
		}
		if(operation < Comparison.SMALLER || operation > Comparison.GREATER){
			return null;
		}		
		return new Comparison<V>(a, b, operation);
	}
			
	private int operator;
	
	//private Comparison(Accessible<T> theA, Accessible<T> theB, int theOperation){
	private Comparison(Accessible<T> a, Accessible<T> b, int theOperation){
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
		T a = super.getA().getFrom(thisObject);
		T b = super.getB().getFrom(thisObject);
		if( a == null ){
			if( b == null && this.operator == Comparison.EQUAL ){
				return Boolean.TRUE;
			}
			return null;
		}
		if( b == null ){
			return null;
		}	
		
		if( this.operator == Comparison.GREATER ){
			if(a.compareTo(b) > 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
		if( this.operator == Comparison.SMALLER ){
			if(a.compareTo(b) < 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
        if( this.operator == Comparison.EQUAL ){
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
