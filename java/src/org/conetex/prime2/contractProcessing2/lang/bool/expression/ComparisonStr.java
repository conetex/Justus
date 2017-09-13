package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleAbstract;
import org.conetex.prime2.contractProcessing2.lang.Pair;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic2;

public class ComparisonStr extends AccessibleAbstract<Boolean>{
	
	public static final int SMALLER = -1;
	public static final int EQUAL = 0;
	public static final int GREATER = 1;
	
	
	public static Accessible<Boolean> _createNew(Accessible<String> theA, Accessible<String> theB, String name) {
		return create(theA, theB, name);
	}
	
	public static ComparisonStr create(Accessible<String> theA, Accessible<String> theB, String operation){
		if(theA == null || theB == null){
			return null;
		}
		if( operation.equals(Symbol.SMALLER) ) {
			return create(theA, theB, ComparisonStr.SMALLER );
		}
		if( operation.equals(Symbol.EQUAL) ) {
			return create(theA, theB, ComparisonStr.EQUAL );
		}
		if( operation.equals(Symbol.GREATER) ) {
			return create(theA, theB, ComparisonStr.GREATER );
		}
		return null;
	}
	
	public static ComparisonStr create(Accessible<String> theA, Accessible<String> theB, int operation){
		if(theA == null || theB == null){
			return null;
		}
		if(operation < ComparisonStr.SMALLER || operation > ComparisonStr.GREATER){
			return null;
		}		
		return new ComparisonStr(theA, theB, operation);
	}	

			
	private int operator;
	
	private Accessible<String> a;
	
	private Accessible<String> b;
	
	//private Comparison(Accessible<T> theA, Accessible<T> theB, int theOperation){
	private ComparisonStr(Accessible<String> theA, Accessible<String> theB, int theOperation){
		this.a = theA;
		this.b = theB;
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
		String aN = this.a.getFrom(thisObject);
		String bN = this.b.getFrom(thisObject);
		if( aN == null ){
			if( bN == null && this.operator == ComparisonStr.EQUAL ){
				return Boolean.TRUE;
			}
			return null;
		}
		else if( bN == null ){
			return null;
		}	
		
		return comp(aN, bN);
	}

	private <T extends Comparable<T>> Boolean comp(T a, T b) {
		if( this.operator == ComparisonStr.GREATER ){
			if(a.compareTo(b) > 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
		if( this.operator == ComparisonStr.SMALLER ){
			if(a.compareTo(b) < 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
        if( this.operator == ComparisonStr.EQUAL ){
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
