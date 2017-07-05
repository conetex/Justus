package org.conetex.prime2.contractProcessing2.lang.math;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class ElementaryArithmetic<I extends Number, O extends Number> extends ComputablePair<I> implements Accessible<O>{
	
	public static final int PLUS = 0;         // Addition
	public static final int MINUS = 1;        // Subtraction 
	public static final int TIMES = 2;        // Multiplication 
	public static final int DIVIDED_BY = 3;   // Division 
	public static final int REMAINS = 4;      // Remainder 
	
	public static <AI extends Number, AO extends Number> ElementaryArithmetic<AI, AO> create(Accessible<AI> theA, Accessible<AI> theB, int operation, Class<AO> resultTyp ){
		if(theA == null || theB == null){
			return null;
		}
		if(resultTyp == Long.class){
			if(theA.getBaseType() == BigInteger.class){
				return null;
			}
		}
		else if(resultTyp == Integer.class){
			if(theA.getBaseType() == BigInteger.class || theA.getBaseType() == Long.class){
				return null;
			}
		}
		else if(resultTyp == Byte.class){
			if(theA.getBaseType() == BigInteger.class || theA.getBaseType() == Long.class  || theA.getBaseType() == Integer.class){
				return null;
			}
		}		
		if(operation < ElementaryArithmetic.PLUS || operation > ElementaryArithmetic.REMAINS){
			return null;
		}
		return new ElementaryArithmetic<AI, AO>(theA, theB, resultTyp, operation);
	}
			
	private int operator;
	
	private Class<O> resultTyp;
	
	private ElementaryArithmetic(Accessible<I> theA, Accessible<I> theB, Class<O> theResultTyp, int theOperation){
		super(theA, theB);
		this.operator = theOperation;
		this.resultTyp = theResultTyp;
	}

	private Integer calcInt(int a, int b) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic.PLUS){
			return Math.addExact( a, b ) ;				
		}
		else if(this.operator == ElementaryArithmetic.MINUS){
			return Math.subtractExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic.TIMES){
			return Math.multiplyExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic.DIVIDED_BY){
			return a / b;				
		}
		else if(this.operator == ElementaryArithmetic.REMAINS){
			return a % b;				
		}
		return null;
	}
	
	private Long calcLong(long a, long b) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic.PLUS){
			return Math.addExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic.MINUS){
			return Math.subtractExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic.TIMES){
			return Math.multiplyExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic.DIVIDED_BY){
			return a / b;				
		}
		else if(this.operator == ElementaryArithmetic.REMAINS){
			return a % b;				
		}
		return null;
	}	
	
	private BigInteger calcBigInt(long a, long b) throws ArithmeticException {
		return this.calcBigInt( BigInteger.valueOf(a), BigInteger.valueOf(b) );
	}
	
	private BigInteger calcBigInt(BigInteger a, BigInteger b) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic.PLUS){
			return a.add(b);				
		}
		else if(this.operator == ElementaryArithmetic.MINUS){
			return a.subtract(b);				
		}
		else if(this.operator == ElementaryArithmetic.TIMES){
			return a.multiply(b);				
		}
		else if(this.operator == ElementaryArithmetic.DIVIDED_BY){
			return a.divide(b);				
		}
		else if(this.operator == ElementaryArithmetic.REMAINS){
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

	@Override
	public void setTo(Structure thisObject, Number value) throws Invalid {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public O copyFrom(Structure thisObject) throws Invalid {
		return this.getFrom(thisObject);
	}

	@Override
	public boolean compute(Structure thisObject) {
		getFrom(thisObject); // TODO compute ist nur fürs debuggen ... ansonsten ist das ja sinnlos hier!
		return true;
	}

	@Override
	public Class<O> getBaseType() {
		return this.resultTyp;
	}

}
