package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleAbstract;
import org.conetex.prime2.contractProcessing2.lang._x_Pair;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic2;

public class ComparisonNumber extends AccessibleAbstract<Boolean>{
	
	public static final int SMALLER = -1;
	public static final int EQUAL = 0;
	public static final int GREATER = 1;
	
	public static ComparisonNumber create(Accessible<? extends Number> theA, Accessible<? extends Number> theB, String operation){
		if(theA == null || theB == null){
			return null;
		}
		if( operation.equals(Symbol.SMALLER) ) {
			return create(theA, theB, ComparisonNumber.SMALLER );
		}
		if( operation.equals(Symbol.EQUAL) ) {
			return create(theA, theB, ComparisonNumber.EQUAL );
		}
		if( operation.equals(Symbol.GREATER) ) {
			return create(theA, theB, ComparisonNumber.GREATER );
		}
		return null;
	}
	
	public static ComparisonNumber create(Accessible<? extends Number> theA, Accessible<? extends Number> theB, int operation){
		if(theA == null || theB == null){
			return null;
		}
		if(operation < ComparisonNumber.SMALLER || operation > ComparisonNumber.GREATER){
			return null;
		}		
		return new ComparisonNumber(theA, theB, operation);
	}	

			
	private int operator;
	
	private Accessible<? extends Number> a;
	
	private Accessible<? extends Number> b;
	
	//private Comparison(Accessible<T> theA, Accessible<T> theB, int theOperation){
	private ComparisonNumber(Accessible<? extends Number> theA, Accessible<? extends Number> theB, int theOperation){
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
		Number aN = this.a.getFrom(thisObject);
		Number bN = this.b.getFrom(thisObject);
		if( aN == null ){
			if( bN == null && this.operator == ComparisonNumber.EQUAL ){
				return Boolean.TRUE;
			}
			return null;
		}
		if( bN == null ){
			return null;
		}	

			if(aN.getClass() == BigInteger.class ){
				if(bN instanceof BigInteger){
					BigInteger a = (BigInteger)aN;
					BigInteger b = (BigInteger)bN;
					return comp(a, b);
				}
				else{
					BigInteger a = (BigInteger)aN;
					BigInteger b = (BigInteger.valueOf(bN.longValue()));
					return comp(a, b);
				}
			}
			else{
				if(bN.getClass() == BigInteger.class ){
					BigInteger a = (BigInteger.valueOf(aN.longValue()));
					BigInteger b = (BigInteger)bN;
					return comp(a, b);
				}
				else{
					if(aN.getClass() == Long.class ){
						if(bN.getClass() == Long.class ){
							Long a = (Long)aN;
							Long b = (Long)bN;
							return comp(a, b);
						}
						else{
							Long a = (Long)aN;
							Long b = bN.longValue();
							return comp(a, b);
						}
					}
					else{
						if(bN.getClass() == Long.class ){
							Long a = aN.longValue();
							Long b = (Long)bN;
							return comp(a, b);
						}
						else{
							int a = aN.intValue();
							int b = bN.intValue();
							return comp(a, b);
						}			
					}				
				}			
			}
		
	}

	private <T extends Comparable<T>> Boolean comp(T a, T b) {
		if( this.operator == ComparisonNumber.GREATER ){
			if(a.compareTo(b) > 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
		if( this.operator == ComparisonNumber.SMALLER ){
			if(a.compareTo(b) < 0){
				return Boolean.TRUE; 
			}
			return Boolean.FALSE;
		}
        if( this.operator == ComparisonNumber.EQUAL ){
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
