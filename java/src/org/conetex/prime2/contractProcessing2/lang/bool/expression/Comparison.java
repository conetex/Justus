package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class Comparison<T extends Number & Comparable<T>> extends ComputablePair<T> implements Accessible<Boolean>{
	
	private static final int SMALLER = -1;
	private static final int EQUAL = 0;
	private static final int GREATER = 1;
	
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
	
	public static <V extends Number & Comparable<V>> Comparison<V> create(Accessible<V> theA, Accessible<V> theB, int operation){
		if(theA == null || theB == null){
			return null;
		}
		if(operation < Comparison.SMALLER || operation > Comparison.GREATER){
			return null;
		}		
		return new Comparison<V>(theA, theB, operation);
	}
			
	private int operator;
	
	private Comparison(Accessible<T> theA, Accessible<T> theB, int theOperation){
		super(theA, theB);
		this.operator = theOperation;
	}

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
		
		if( this.operator == Comparison.GREATER && a.compareTo(b) > 0  ){
			return Boolean.TRUE; 
		}
		if( this.operator == Comparison.SMALLER && a.compareTo(b) < 0  ){
			return Boolean.TRUE; 
		}
        if( this.operator == Comparison.EQUAL   && a.compareTo(b) == 0 ){
        	return Boolean.TRUE; 
		}		
		return Boolean.FALSE;
		
	}

	@Override
	public void setTo(Structure thisObject, Boolean value) throws Invalid {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public boolean compute(Structure thisObject) {
		getFrom(thisObject); // TODO compute ist nur fürs debuggen ... ansonsten ist das ja sinnlos hier!
		return true;
	}

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}
	
}
