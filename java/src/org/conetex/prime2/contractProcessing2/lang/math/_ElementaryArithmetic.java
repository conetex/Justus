package org.conetex.prime2.contractProcessing2.lang.math;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang._x_Pair;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class _ElementaryArithmetic<I extends Number, O> extends _x_Pair<I> implements Accessible<O>{
	
	public static final int PLUS = 0;         // Addition
	public static final int MINUS = 1;        // Subtraction 
	public static final int TIMES = 2;        // Multiplication 
	public static final int DIVIDED_BY = 3;   // Division 
	public static final int REMAINS = 4;      // Remainder 
	
	public static <AI extends Number, AO> _ElementaryArithmetic<AI, AO> _create(Accessible<AI> theA, Accessible<AI> theB, String operation, Class<AO> resultTyp ){
		if(theA == null || theB == null){
			return null;
		}
		if( operation.equals(Symbol.PLUS) ) {
			return _create(theA, theB, _ElementaryArithmetic.PLUS, resultTyp );
		}
		if( operation.equals(Symbol.MINUS) ) {
			return _create(theA, theB, _ElementaryArithmetic.MINUS, resultTyp );
		}
		if( operation.equals(Symbol.TIMES) ) {
			return _create(theA, theB, _ElementaryArithmetic.TIMES, resultTyp );
		}
		if( operation.equals(Symbol.DIVIDED_BY) ) {
			return _create(theA, theB, _ElementaryArithmetic.DIVIDED_BY, resultTyp );
		}
		if( operation.equals(Symbol.REMAINS) ) {
			return _create(theA, theB, _ElementaryArithmetic.REMAINS, resultTyp );
		}		
		return null;
	}
	
	public static <AI extends Number, AO> _ElementaryArithmetic<AI, AO> _create(Accessible<AI> theA, Accessible<AI> theB, int operation, Class<AO> resultTyp ){

		if(operation < _ElementaryArithmetic.PLUS || operation > _ElementaryArithmetic.REMAINS){
			return null;
		}
		
		if(theA == null || theB == null){
			return null;
		}
		Class<AI> inputTypA = theA.getBaseType();
		Class<AI> inputTypB = theB.getBaseType();
		Class<AI> inputTyp = getBiggest(inputTypA, inputTypB);	
		if(inputTyp == null){
			// TODO Error unknown Typ
			return null;
		}
		
		if(resultTyp == Long.class){
			if(inputTyp == BigInteger.class){
				return null;
			}
		}
		else if(resultTyp == Integer.class){
			if(inputTyp == BigInteger.class || inputTyp == Long.class){
				return null;
			}
		}
		else if(resultTyp == Byte.class){
			if(inputTyp == BigInteger.class || inputTyp == Long.class || inputTyp == Integer.class){
				return null;
			}
		}	
		
		else if(resultTyp == Number.class){
			if(     inputTyp == BigInteger.class){
				return (_ElementaryArithmetic<AI, AO>) new _ElementaryArithmetic<AI, BigInteger>(theA, theB, BigInteger.class, operation);
			}
			else if(inputTyp == Long.class){
				return (_ElementaryArithmetic<AI, AO>) new _ElementaryArithmetic<AI, Long>(theA, theB, Long.class, operation);
			}
			else if(inputTyp == Integer.class){
				return (_ElementaryArithmetic<AI, AO>) new _ElementaryArithmetic<AI, Integer>(theA, theB, Integer.class, operation);
			}
			else if(inputTyp == Byte.class){
				return (_ElementaryArithmetic<AI, AO>) new _ElementaryArithmetic<AI, Byte>(theA, theB, Byte.class, operation);
			}			
			else {
				// TODO Error unknown Typ
				return null;
			}	
		}	
		
		return new _ElementaryArithmetic<AI, AO>(theA, theB, resultTyp, operation);
	}
	
	public static <AI extends Number> Class<AI> getBiggest(Class<AI> a, Class<AI> b){
		Class<?>[] classes = {
				  BigInteger.class
				, Long.class
				, Integer.class
				, Byte.class
		};
		
		for(int ai = 0; ai < classes.length; ai++){
			if(classes[ai] == a){
				for(int bi = 0; bi < classes.length; bi++){
					if(classes[bi] == b){
						if(ai < bi){
							return a;
						}
						else{
							return b;
						}
					}
				}				
			}
		}
		// TODO: ERROR unknown typ
		return a;
	}
			
	private int operator;
	
	private Class<O> resultTyp;
	
	private _ElementaryArithmetic(Accessible<I> theA, Accessible<I> theB, Class<O> theResultTyp, int theOperation){
		super(theA, theB);
		this.operator = theOperation;
		this.resultTyp = theResultTyp;
	}

	private Integer calcInt(int a, int b) throws ArithmeticException {
		if(this.operator == _ElementaryArithmetic.PLUS){
			return Math.addExact( a, b ) ;				
		}
		else if(this.operator == _ElementaryArithmetic.MINUS){
			return Math.subtractExact( a, b );				
		}
		else if(this.operator == _ElementaryArithmetic.TIMES){
			return Math.multiplyExact( a, b );				
		}
		else if(this.operator == _ElementaryArithmetic.DIVIDED_BY){
			return a / b;				
		}
		else if(this.operator == _ElementaryArithmetic.REMAINS){
			return a % b;				
		}
		return null;
	}
	
	private Long calcLong(long a, long b) throws ArithmeticException {
		if(this.operator == _ElementaryArithmetic.PLUS){
			return Math.addExact( a, b );				
		}
		else if(this.operator == _ElementaryArithmetic.MINUS){
			return Math.subtractExact( a, b );				
		}
		else if(this.operator == _ElementaryArithmetic.TIMES){
			return Math.multiplyExact( a, b );				
		}
		else if(this.operator == _ElementaryArithmetic.DIVIDED_BY){
			return a / b;				
		}
		else if(this.operator == _ElementaryArithmetic.REMAINS){
			return a % b;				
		}
		return null;
	}	
	
	private BigInteger calcBigInt(long a, long b) throws ArithmeticException {
		return this.calcBigInt( BigInteger.valueOf(a), BigInteger.valueOf(b) );
	}
	
	private BigInteger calcBigInt(BigInteger a, BigInteger b) throws ArithmeticException {
		if(this.operator == _ElementaryArithmetic.PLUS){
			return a.add(b);				
		}
		else if(this.operator == _ElementaryArithmetic.MINUS){
			return a.subtract(b);				
		}
		else if(this.operator == _ElementaryArithmetic.TIMES){
			return a.multiply(b);				
		}
		else if(this.operator == _ElementaryArithmetic.DIVIDED_BY){
			return a.divide(b);				
		}
		else if(this.operator == _ElementaryArithmetic.REMAINS){
			return a.remainder(b);				
		}
		return null;
	}	
	
	@Override
	public O getFrom(Structure thisObject) throws ArithmeticException {
		I a = super.getA().getFrom(thisObject);
		I b = super.getB().getFrom(thisObject);
		if( a == null || b == null ){
			return null;
		}
		else if(resultTyp == Integer.class){
			return (O)( this.calcInt(a.intValue(), b.intValue()) );
		}
		else if(resultTyp ==  Long.class){
			return (O)( this.calcLong(a.longValue(), b.longValue()) );
		}
		else if(resultTyp == BigInteger.class){
			if(a instanceof BigInteger){
				return (O)( this.calcBigInt((BigInteger)a, (BigInteger)b) );
			}
			return (O)( this.calcBigInt(a.longValue(), b.longValue()) );
		}		
		return null;		
	}

	public void setTo_(Structure thisObject, Number value) throws Invalid {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public O copyFrom(Structure thisObject) throws Invalid {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<O> getBaseType() {
		return this.resultTyp;
	}

	@Override
	public Accessible<O> as(Class<?> baseType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> Accessible<T> as2(Class<T> baseType) {
		// TODO Auto-generated method stub
		return null;
	}

}
