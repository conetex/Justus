package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang._x_Pair;
import org.conetex.prime2.contractProcessing2.lang.math._ElementaryArithmetic;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class _ComparisonOrg<T extends Number & Comparable<T>> extends _x_Pair<T> implements Accessible<Boolean>{
	
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
	
	public static <V extends Number & Comparable<V>> _ComparisonOrg<V> _create(Accessible<V> theA, Accessible<V> theB, int operation){
		if(theA == null || theB == null){
			return null;
		}
		if(operation < _ComparisonOrg.SMALLER || operation > _ComparisonOrg.GREATER){
			return null;
		}		
		return new _ComparisonOrg<V>(theA, theB, operation);
	}
			
	private int operator;
	
	private _ComparisonOrg(Accessible<T> theA, Accessible<T> theB, int theOperation){
		super(theA, theB);
		this.operator = theOperation;
	}

	@Override
	public Boolean getFrom(Structure thisObject) {
		T a = super.getA().getFrom(thisObject);
		T b = super.getB().getFrom(thisObject);
		if( a == null ){
			if( b == null && this.operator == _ComparisonOrg.EQUAL ){
				return Boolean.TRUE;
			}
			return null;
		}
		if( b == null ){
			return null;
		}	
		
		if( this.operator == _ComparisonOrg.GREATER && a.compareTo(b) > 0  ){
			return Boolean.TRUE; 
		}
		if( this.operator == _ComparisonOrg.SMALLER && a.compareTo(b) < 0  ){
			return Boolean.TRUE; 
		}
        if( this.operator == _ComparisonOrg.EQUAL   && a.compareTo(b) == 0 ){
        	return Boolean.TRUE; 
		}		
		return Boolean.FALSE;
		
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}

	@Override
	public Accessible<Boolean> as(Class<?> baseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Accessible<T> as2(Class<T> baseType) {
		// TODO Auto-generated method stub
		return null;
	}
	
}
