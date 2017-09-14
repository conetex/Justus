package org.conetex.prime2.contractProcessing2.lang.math;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang._x_Pair;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleAbstract;

public class ElementaryArithmetic2<Ia extends Number, Ib extends Number, R extends Number> extends AccessibleAbstract<R>{
	
	public static final int PLUS = 0;         // Addition
	public static final int MINUS = 1;        // Subtraction 
	public static final int TIMES = 2;        // Multiplication 
	public static final int DIVIDED_BY = 3;   // Division 
	public static final int REMAINS = 4;      // Remainder 
	
	
	public static <IA extends Number, IB extends Number, O extends Number> ElementaryArithmetic2<IA, IB, O> createNewX(Accessible<IA> theA, Accessible<IB> theB, String operation, Class<O> resultTyp ){
		if(theA == null || theB == null){
			return null;
		}
		if( operation.equals(Symbol.PLUS) ) {
			return createNewX(theA, theB, ElementaryArithmetic2.PLUS, resultTyp );
		}
		if( operation.equals(Symbol.MINUS) ) {
			return createNewX(theA, theB, ElementaryArithmetic2.MINUS, resultTyp );
		}
		if( operation.equals(Symbol.TIMES) ) {
			return createNewX(theA, theB, ElementaryArithmetic2.TIMES, resultTyp );
		}
		if( operation.equals(Symbol.DIVIDED_BY) ) {
			return createNewX(theA, theB, ElementaryArithmetic2.DIVIDED_BY, resultTyp );
		}
		if( operation.equals(Symbol.REMAINS) ) {
			return createNewX(theA, theB, ElementaryArithmetic2.REMAINS, resultTyp );
		}		
		return null;
	}
		
	public static <IA extends Number, IB extends Number, O extends Number> ElementaryArithmetic2<IA, IB, O> createNewX(Accessible<IA> theA, Accessible<IB> theB, int operation, Class<O> resultTyp ){
		if(operation < ElementaryArithmetic2.PLUS || operation > ElementaryArithmetic2.REMAINS){
			return null;
		}
		
		if(theA == null || theB == null){
			return null;
		}
		Class<IA> inputTypA = theA.getBaseType();
		Class<IB> inputTypB = theB.getBaseType();
		Class<? extends Number> inputTyp = getBiggest(inputTypA, inputTypB);	
		if(inputTyp == null){
			// TODO Error unknown Typ
			return null;
		}
		
		if(resultTyp == Long.class){
			if(inputTyp == BigInteger.class){
				// TODO Error Typ to big
				return null;
			}
		}
		else if(resultTyp == Integer.class){
			if(inputTyp == BigInteger.class || inputTyp == Long.class){
				// TODO Error Typ to big
				return null;
			}
		}
		else if(resultTyp == Byte.class){
			if(inputTyp == BigInteger.class || inputTyp == Long.class || inputTyp == Integer.class){
				// TODO Error Typ to big
				return null;
			}
		}	
		
		else if(resultTyp == Number.class){
			if(     inputTyp == BigInteger.class){
				return (ElementaryArithmetic2<IA, IB, O>) new ElementaryArithmetic2<IA, IB, BigInteger>(theA, theB, BigInteger.class, operation);
			}
			else if(inputTyp == Long.class){
				return (ElementaryArithmetic2<IA, IB, O>) new ElementaryArithmetic2<IA, IB, Long>(theA, theB, Long.class, operation);
			}
			else if(inputTyp == Integer.class){
				return (ElementaryArithmetic2<IA, IB, O>) new ElementaryArithmetic2<IA, IB, Integer>(theA, theB, Integer.class, operation);
			}
			else if(inputTyp == Byte.class){
				return (ElementaryArithmetic2<IA, IB, O>) new ElementaryArithmetic2<IA, IB, Byte>(theA, theB, Byte.class, operation);
			}			
			else {
				// TODO Error unknown Typ
				return null;
			}	
		}	
		
		return new ElementaryArithmetic2<IA, IB, O>(theA, theB, resultTyp, operation);
	}
	
	public static <IA extends Number, IB extends Number> ElementaryArithmetic2<IA, IB, ? extends Number> createNew(Accessible<IA> theA, Accessible<IB> theB, String operation ){
		if(theA == null || theB == null){
			return null;
		}
		if( operation.equals(Symbol.PLUS) ) {
			return createNew(theA, theB, ElementaryArithmetic2.PLUS );
		}
		if( operation.equals(Symbol.MINUS) ) {
			return createNew(theA, theB, ElementaryArithmetic2.MINUS );
		}
		if( operation.equals(Symbol.TIMES) ) {
			return createNew(theA, theB, ElementaryArithmetic2.TIMES );
		}
		if( operation.equals(Symbol.DIVIDED_BY) ) {
			return createNew(theA, theB, ElementaryArithmetic2.DIVIDED_BY );
		}
		if( operation.equals(Symbol.REMAINS) ) {
			return createNew(theA, theB, ElementaryArithmetic2.REMAINS );
		}		
		return null;
	}
	
	public static <IA extends Number, IB extends Number> ElementaryArithmetic2<IA, IB, ? extends Number> createNew(Accessible<IA> theA, Accessible<IB> theB, int operation ){
		if(operation < ElementaryArithmetic2.PLUS || operation > ElementaryArithmetic2.REMAINS){
			return null;
		}
		
		if(theA == null || theB == null){
			return null;
		}
		Class<IA> inputTypA = theA.getBaseType();
		Class<IB> inputTypB = theB.getBaseType();
		Class<? extends Number> inputTyp = getBiggest(inputTypA, inputTypB);	
		if(inputTyp == null){
			// TODO Error unknown Typ
			return null;
		}
		
		if(     inputTyp == BigInteger.class){
			return new ElementaryArithmetic2<IA, IB, BigInteger>(theA, theB, BigInteger.class, operation);
		}
		else if(inputTyp == Long.class){
			return new ElementaryArithmetic2<IA, IB, Long>(theA, theB, Long.class, operation);
		}
		else if(inputTyp == Integer.class){
			return new ElementaryArithmetic2<IA, IB, Integer>(theA, theB, Integer.class, operation);
		}
		else if(inputTyp == Byte.class){
			return new ElementaryArithmetic2<IA, IB, Byte>(theA, theB, Byte.class, operation);
		}			
		else {
			// TODO Error unknown Typ
			return null;
		}	
		
	}
	
	public static Class<? extends Number> getBiggest(Class<? extends Number> a, Class<? extends Number> b){
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
	
	private Class<R> resultTyp;
	
	private Accessible<Ia> a;
	
	private Accessible<Ib> b;	
	
	private ElementaryArithmetic2(Accessible<Ia> theA, Accessible<Ib> theB, Class<R> theResultTyp, int theOperation){
		this.a = theA;
		this.b = theB;
		this.operator = theOperation;
		this.resultTyp = theResultTyp;
	}

	
	private Byte calcByte(byte a, byte b) throws ArithmeticException {
		Integer re = null; 
		if(this.operator == ElementaryArithmetic2.PLUS){
			re = Math.addExact( a, b ) ;				
		}
		else if(this.operator == ElementaryArithmetic2.MINUS){
			re = Math.subtractExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic2.TIMES){
			re = Math.multiplyExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic2.DIVIDED_BY){
			re = a / b;				
		}
		else if(this.operator == ElementaryArithmetic2.REMAINS){
			re = a % b;				
		}
		if(re == null){
			return null;
		}
		return re.byteValue(); // TODO: convertiere nur, wenns wirklich passt...
	}
	
	private Integer calcInt(int a, int b) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic2.PLUS){
			return Math.addExact( a, b ) ;				
		}
		else if(this.operator == ElementaryArithmetic2.MINUS){
			return Math.subtractExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic2.TIMES){
			return Math.multiplyExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic2.DIVIDED_BY){
			return a / b;				
		}
		else if(this.operator == ElementaryArithmetic2.REMAINS){
			return a % b;				
		}
		return null;
	}
	
	private Long calcLong(long a, long b) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic2.PLUS){
			return Math.addExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic2.MINUS){
			return Math.subtractExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic2.TIMES){
			return Math.multiplyExact( a, b );				
		}
		else if(this.operator == ElementaryArithmetic2.DIVIDED_BY){
			return a / b;				
		}
		else if(this.operator == ElementaryArithmetic2.REMAINS){
			return a % b;				
		}
		return null;
	}	
	
	private BigInteger calcBigInt(BigInteger a, BigInteger b) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic2.PLUS){
			return a.add(b);				
		}
		else if(this.operator == ElementaryArithmetic2.MINUS){
			return a.subtract(b);				
		}
		else if(this.operator == ElementaryArithmetic2.TIMES){
			return a.multiply(b);				
		}
		else if(this.operator == ElementaryArithmetic2.DIVIDED_BY){
			return a.divide(b);				
		}
		else if(this.operator == ElementaryArithmetic2.REMAINS){
			return a.remainder(b);				
		}
		return null;
	}	
	
	private BigInteger calcBigIntNum(Number a, Number b) throws ArithmeticException {
		if(a instanceof BigInteger){
			if(b instanceof BigInteger){
				return calcBigInt( (BigInteger)a, (BigInteger)b );
			}
			else{
				return calcBigInt( (BigInteger)a, BigInteger.valueOf(b.longValue()) );				
			}			
		}
		else{
			if(b instanceof BigInteger){
				return calcBigInt( BigInteger.valueOf(a.longValue()), (BigInteger)b );
			}
			else{
				return calcBigInt( BigInteger.valueOf(a.longValue()), BigInteger.valueOf(b.longValue()) );
			}			
		}
	}		
	
	
	@Override
	public R getFrom(Structure thisObject) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		if( a == null || b == null ){
			return null;
		}
		else if(this.resultTyp == Byte.class){
			return (R)( this.calcByte(a.byteValue(), b.byteValue()) );
		}		
		else if(this.resultTyp == Integer.class){
			return (R)( this.calcInt(a.intValue(), b.intValue()) );
		}
		else if(this.resultTyp ==  Long.class){
			return (R)( this.calcLong(a.longValue(), b.longValue()) );
		}
		else if(this.resultTyp == BigInteger.class){
			return (R)( this.calcBigIntNum(a, b) );
		}		
		return null;		
	}

	public Integer getFromI(Structure thisObject, Class<Integer> resultTyp) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		return this.calcInt(a.intValue(), b.intValue()) ;
	}
	
	public Long getFromL(Structure thisObject, Class<Long> resultTyp) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		return this.calcLong(a.longValue(), b.longValue()) ;
	}
	
	public BigInteger getFromBi(Structure thisObject, Class<BigInteger> resultTyp) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		return this.calcBigIntNum(a, b) ;
	}
	
	public void setTo_(Structure thisObject, Number value) throws Invalid {
		// TODO Auto-generated method stub
		
	}	

	@Override
	public R copyFrom(Structure thisObject) throws Invalid {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<R> getBaseType() {
		return this.resultTyp;
	}

}
