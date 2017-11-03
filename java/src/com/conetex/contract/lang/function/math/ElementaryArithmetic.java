package com.conetex.contract.lang.function.math;

import java.math.BigInteger;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.TypesDoNotMatch;
import com.conetex.contract.lang.function.access.Accessible;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class ElementaryArithmetic<Ia extends Number, Ib extends Number, R extends Number> extends Accessible<R>{

	public static final int	PLUS		= 0;	// Addition
	public static final int	MINUS		= 1;	// Subtraction
	public static final int	TIMES		= 2;	// Multiplication
	public static final int	DIVIDED_BY	= 3;	// Division
	public static final int	REMAINS		= 4;	// Remainder

	public static <IA extends Number, IB extends Number> ElementaryArithmetic<IA, IB, ? extends Number> createNew(Accessible<IA> theA, Accessible<IB> theB,
			String operation) {
		if(theA == null || theB == null){
			return null;
		}
		if(operation.equals(Symbols.comPlus())){
			return createNew(theA, theB, ElementaryArithmetic.PLUS);
		}
		if(operation.equals(Symbols.comMinus())){
			return createNew(theA, theB, ElementaryArithmetic.MINUS);
		}
		if(operation.equals(Symbols.comTimes())){
			return createNew(theA, theB, ElementaryArithmetic.TIMES);
		}
		if(operation.equals(Symbols.comDividedBy())){
			return createNew(theA, theB, ElementaryArithmetic.DIVIDED_BY);
		}
		if(operation.equals(Symbols.comRemains())){
			return createNew(theA, theB, ElementaryArithmetic.REMAINS);
		}
		return null;
	}

	private static <IA extends Number, IB extends Number> ElementaryArithmetic<IA, IB, ? extends Number> createNew(Accessible<IA> theA, Accessible<IB> theB,
			int operation) {
		if(operation < ElementaryArithmetic.PLUS || operation > ElementaryArithmetic.REMAINS){
			return null;
		}

		if(theA == null || theB == null){
			return null;
		}
		Class<IA> inputTypA = theA.getRawTypeClass();
		Class<IB> inputTypB = theB.getRawTypeClass();
		Class<?> inputTyp = getBiggest(inputTypA, inputTypB);
		if(inputTyp == null){
			// TODO Error unknown Typ
			return null;
		}

		if(inputTyp == BigInteger.class){
			return new ElementaryArithmetic<IA, IB, BigInteger>(theA, theB, BigInteger.class, operation){
				@Override
				public BigInteger getFrom(Structure thisObject) throws AbstractRuntimeException {
					IA aA = super.a.getFrom(thisObject);
					IB aB = super.b.getFrom(thisObject);
					if(aA == null || aB == null){
						// TODO Error
						return null;
					}
					return super.calcBigIntNum(aA, aB);
				}
			};
		}
		else if(inputTyp == Long.class){
			return new ElementaryArithmetic<IA, IB, Long>(theA, theB, Long.class, operation){
				@Override
				public Long getFrom(Structure thisObject) throws AbstractRuntimeException {
					IA aA = super.a.getFrom(thisObject);
					IB aB = super.b.getFrom(thisObject);
					if(aA == null || aB == null){
						// TODO Error
						return null;
					}
					return super.calcLong(aA.longValue(), aB.longValue());
				}
			};
		}
		else if(inputTyp == Integer.class){
			return new ElementaryArithmetic<IA, IB, Integer>(theA, theB, Integer.class, operation){
				@Override
				public Integer getFrom(Structure thisObject) throws AbstractRuntimeException {
					IA aA = super.a.getFrom(thisObject);
					IB aB = super.b.getFrom(thisObject);
					if(aA == null || aB == null){
						// TODO Error
						return null;
					}
					return super.calcInt(aA.intValue(), aB.intValue());
				}
			};
		}
		else if(inputTyp == Byte.class){
			return new ElementaryArithmetic<IA, IB, Byte>(theA, theB, Byte.class, operation){
				@Override
				public Byte getFrom(Structure thisObject) throws AbstractRuntimeException {
					IA aA = super.a.getFrom(thisObject);
					IB aB = super.b.getFrom(thisObject);
					if(aA == null || aB == null){
						// TODO Error
						return null;
					}
					return super.calcByte(aA, aB);
				}
			};
		}
		else{
			// TODO Error unknown Typ
			return null;
		}

	}

	public static Class<?> getBiggest(Class<?> a, Class<?> b) {
		Class<?>[] classes = { BigInteger.class, Long.class, Integer.class, Byte.class };

		for(int ai = 0; ai < classes.length; ai++){
			if(classes[ai] == a){
				for(int bi = 0; bi < classes.length; bi++){
					if(classes[bi] == b){
						if(ai < bi){
							return classes[ai];
						}
						else{
							return classes[bi];
						}
					}
				}
			}
		}
		// TODO: ERROR unknown typ
		return null;
	}

	private int operator;

	private Class<R> resultTyp;

	Accessible<Ia> a;

	Accessible<Ib> b;

	protected ElementaryArithmetic(Accessible<Ia> theA, Accessible<Ib> theB, Class<R> theResultTyp, int theOperation) {
		this.a = theA;
		this.b = theB;
		this.operator = theOperation;
		this.resultTyp = theResultTyp;
	}

	Byte calcByte(Number aA, Number aB) throws ArithmeticException {
		Integer re = null;
		if(this.operator == ElementaryArithmetic.PLUS){
			re = Integer.valueOf(Math.addExact(aA.intValue(), aB.intValue()));
		}
		else if(this.operator == ElementaryArithmetic.MINUS){
			re = Integer.valueOf(Math.subtractExact(aA.intValue(), aB.intValue()));
		}
		else if(this.operator == ElementaryArithmetic.TIMES){
			re = Integer.valueOf(Math.multiplyExact(aA.intValue(), aB.intValue()));
		}
		else if(this.operator == ElementaryArithmetic.DIVIDED_BY){
			re = Integer.valueOf(aA.intValue() / aB.intValue());
		}
		else if(this.operator == ElementaryArithmetic.REMAINS){
			re = Integer.valueOf(aA.intValue() % aB.intValue());
		}
		else{
			return null;
		}
		return Byte.valueOf(re.byteValue()); // TODO: convertiere nur, wenns
												// wirklich passt...
	}

	Integer calcInt(int aA, int aB) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic.PLUS){
			return Integer.valueOf(Math.addExact(aA, aB));
		}
		else if(this.operator == ElementaryArithmetic.MINUS){
			return Integer.valueOf(Math.subtractExact(aA, aB));
		}
		else if(this.operator == ElementaryArithmetic.TIMES){
			return Integer.valueOf(Math.multiplyExact(aA, aB));
		}
		else if(this.operator == ElementaryArithmetic.DIVIDED_BY){
			return Integer.valueOf(aA / aB);
		}
		else if(this.operator == ElementaryArithmetic.REMAINS){
			return Integer.valueOf(aA % aB);
		}
		return null;
	}

	Long calcLong(long aA, long aB) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic.PLUS){
			return Long.valueOf(Math.addExact(aA, aB));
		}
		else if(this.operator == ElementaryArithmetic.MINUS){
			return Long.valueOf(Math.subtractExact(aA, aB));
		}
		else if(this.operator == ElementaryArithmetic.TIMES){
			return Long.valueOf(Math.multiplyExact(aA, aB));
		}
		else if(this.operator == ElementaryArithmetic.DIVIDED_BY){
			return Long.valueOf(aA / aB);
		}
		else if(this.operator == ElementaryArithmetic.REMAINS){
			return Long.valueOf(aA % aB);
		}
		return null;
	}

	BigInteger calcBigInt(BigInteger aA, BigInteger aB) throws ArithmeticException {
		if(this.operator == ElementaryArithmetic.PLUS){
			return aA.add(aB);
		}
		else if(this.operator == ElementaryArithmetic.MINUS){
			return aA.subtract(aB);
		}
		else if(this.operator == ElementaryArithmetic.TIMES){
			return aA.multiply(aB);
		}
		else if(this.operator == ElementaryArithmetic.DIVIDED_BY){
			return aA.divide(aB);
		}
		else if(this.operator == ElementaryArithmetic.REMAINS){
			return aA.remainder(aB);
		}
		return null;
	}

	BigInteger calcBigIntNum(Number aA, Number aB) throws ArithmeticException {
		if(aA instanceof BigInteger){
			if(aB instanceof BigInteger){
				return calcBigInt((BigInteger) aA, (BigInteger) aB);
			}
			else{
				return calcBigInt((BigInteger) aA, BigInteger.valueOf(aB.longValue()));
			}
		}
		else{
			if(aB instanceof BigInteger){
				return calcBigInt(BigInteger.valueOf(aA.longValue()), (BigInteger) aB);
			}
			else{
				return calcBigInt(BigInteger.valueOf(aA.longValue()), BigInteger.valueOf(aB.longValue()));
			}
		}
	}

	@Override
	public R copyFrom(Structure thisObject) throws AbstractRuntimeException {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<R> getRawTypeClass() {
		return this.resultTyp;
	}

	public static Class<? extends Number> getConcretNumRawType(Class<?> rawType) throws TypesDoNotMatch {
		if(rawType == Integer.class){
			return Integer.class;
		}
		if(rawType == BigInteger.class){
			return BigInteger.class;
		}
		if(rawType == Long.class){
			return Long.class;
		}
		if(rawType == Byte.class){
			return Byte.class;
		}
		throw new TypesDoNotMatch(rawType.toString(), Number.class.toString());
	}

}
