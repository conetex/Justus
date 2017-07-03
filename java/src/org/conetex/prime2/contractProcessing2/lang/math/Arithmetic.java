package org.conetex.prime2.contractProcessing2.lang.math;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class Arithmetic<I extends Number, O extends Number> extends ComputablePair<I> implements Accessible<O>{
	
	private static final int PLUS = 0;         // Addition
	private static final int MINUS = 1;        // Subtraction 
	private static final int TIMES = 2;        // Multiplication 
	private static final int DIVIDED_BY = 3;   // Division 
	private static final int REMAINS = 4;      // Remainder 
	
	public static <AI extends Number, AO extends Number> Arithmetic<AI, AO> create(Accessible<AI> theA, Accessible<AI> theB, Class<AO> resultTyp, int operation){
		// TODO: was ist bei result Integer, wenn a/b Long oder BigInt ist?
		// TODO: was istbei result Long, wenn a/b BigInt ist?
		if(theA == null || theB == null){
			return null;
		}
		if(operation < Arithmetic.PLUS || operation > Arithmetic.REMAINS){
			return null;
		}
		return new Arithmetic<AI, AO>(theA, theB, resultTyp, operation);
	}
			
	private int operator;
	private Class<O> resultTyp;
	
	private Arithmetic(Accessible<I> theA, Accessible<I> theB, Class<O> theResultTyp, int theOperation){
		super(theA, theB);
		this.operator = theOperation;
		this.resultTyp = theResultTyp;
	}

	private Integer getInt(Number a, Number b) throws ArithmeticException {
		if(this.operator == Arithmetic.PLUS){
			return Math.addExact( a.intValue(), b.intValue()) ;				
		}
		else if(this.operator == Arithmetic.MINUS){
			return Math.subtractExact(a.intValue(), b.intValue());				
		}
		else if(this.operator == Arithmetic.TIMES){
			return Math.multiplyExact(a.intValue(), b.intValue());				
		}
		else if(this.operator == Arithmetic.DIVIDED_BY){
			return a.intValue() / b.intValue();				
		}
		else if(this.operator == Arithmetic.REMAINS){
			return a.intValue() % b.intValue();				
		}
		return null;
	}
	
	private Long getLong(Number a, Number b) throws ArithmeticException {
		if(this.operator == Arithmetic.PLUS){
			return Math.addExact(a.longValue(), b.longValue());				
		}
		else if(this.operator == Arithmetic.MINUS){
			return Math.subtractExact(a.longValue(), b.longValue());				
		}
		else if(this.operator == Arithmetic.TIMES){
			return Math.multiplyExact(a.longValue(), b.longValue());				
		}
		else if(this.operator == Arithmetic.DIVIDED_BY){
			return a.longValue() / b.longValue();				
		}
		else if(this.operator == Arithmetic.REMAINS){
			return a.longValue() % b.longValue();				
		}
		return null;
	}	
	
	private BigInteger getBigInteger(Number a, Number b) throws ArithmeticException {
		BigInteger bigA = null;
		if(a instanceof BigInteger){
			bigA = (BigInteger)a;
		}
		else{
			bigA = BigInteger.valueOf( a.longValue() ); 
		}

		BigInteger bigB = null;			
		if(b instanceof BigInteger){
			bigB = (BigInteger)b;
		}
		else{
			bigB = BigInteger.valueOf( b.longValue() ); 
		}
		
		if(this.operator == Arithmetic.PLUS){
			return bigA.add(bigB);				
		}
		else if(this.operator == Arithmetic.MINUS){
			return bigA.subtract(bigB);				
		}
		else if(this.operator == Arithmetic.TIMES){
			return bigA.multiply(bigB);				
		}
		else if(this.operator == Arithmetic.DIVIDED_BY){
			return bigA.divide(bigB);				
		}
		else if(this.operator == Arithmetic.REMAINS){
			return bigA.remainder(bigB);				
		}
		return null;
	}	
	
	@Override
	public O getFrom(Structure thisObject) throws ArithmeticException {
		Number a = super.getA().getFrom(thisObject);
		Number b = super.getB().getFrom(thisObject);
		if( a == null || b == null ){
			return null;
		}
		if(resultTyp == Integer.class){
			return (O) this.getInt(a, b);
		}
		else if(resultTyp ==  Long.class){
			return (O) this.getLong(a, b);
		}
		else if(resultTyp == BigInteger.class){
			return (O) this.getBigInteger(a, b);
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





	
}
