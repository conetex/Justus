package com.conetex.contract.lang.math;

import java.math.BigInteger;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.interpreter.exception.TypesDoNotMatch;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.Symbol;

public class ElementaryArithmetic<Ia extends Number, Ib extends Number, R extends Number> extends Accessible<R> {

	public static final int PLUS = 0; // Addition
	public static final int MINUS = 1; // Subtraction
	public static final int TIMES = 2; // Multiplication
	public static final int DIVIDED_BY = 3; // Division
	public static final int REMAINS = 4; // Remainder

	public static <IA extends Number, IB extends Number> ElementaryArithmetic<IA, IB, ? extends Number> createNew(
			Accessible<IA> theA, Accessible<IB> theB, String operation) {
		if (theA == null || theB == null) {
			return null;
		}
		if (operation.equals(Symbol.PLUS)) {
			return createNew(theA, theB, ElementaryArithmetic.PLUS);
		}
		if (operation.equals(Symbol.MINUS)) {
			return createNew(theA, theB, ElementaryArithmetic.MINUS);
		}
		if (operation.equals(Symbol.TIMES)) {
			return createNew(theA, theB, ElementaryArithmetic.TIMES);
		}
		if (operation.equals(Symbol.DIVIDED_BY)) {
			return createNew(theA, theB, ElementaryArithmetic.DIVIDED_BY);
		}
		if (operation.equals(Symbol.REMAINS)) {
			return createNew(theA, theB, ElementaryArithmetic.REMAINS);
		}
		return null;
	}

	public static <IA extends Number, IB extends Number> ElementaryArithmetic<IA, IB, ? extends Number> createNew(
			Accessible<IA> theA, Accessible<IB> theB, int operation) {
		if (operation < ElementaryArithmetic.PLUS || operation > ElementaryArithmetic.REMAINS) {
			return null;
		}

		if (theA == null || theB == null) {
			return null;
		}
		Class<IA> inputTypA = theA.getBaseType();
		Class<IB> inputTypB = theB.getBaseType();
		Class<?> inputTyp = getBiggest(inputTypA, inputTypB);
		if (inputTyp == null) {
			// TODO Error unknown Typ
			return null;
		}

		if (inputTyp == BigInteger.class) {
			return new ElementaryArithmetic<IA, IB, BigInteger>(theA, theB, BigInteger.class, operation);
		} else if (inputTyp == Long.class) {
			return new ElementaryArithmetic<IA, IB, Long>(theA, theB, Long.class, operation);
		} else if (inputTyp == Integer.class) {
			return new ElementaryArithmetic<IA, IB, Integer>(theA, theB, Integer.class, operation);
		} else if (inputTyp == Byte.class) {
			return new ElementaryArithmetic<IA, IB, Byte>(theA, theB, Byte.class, operation);
		} else {
			// TODO Error unknown Typ
			return null;
		}

	}

	public static Class<?> getBiggest(Class<?> a, Class<?> b) {
		Class<?>[] classes = { BigInteger.class, Long.class, Integer.class, Byte.class };

		for (int ai = 0; ai < classes.length; ai++) {
			if (classes[ai] == a) {
				for (int bi = 0; bi < classes.length; bi++) {
					if (classes[bi] == b) {
						if (ai < bi) {
							return classes[ai];
						} else {
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

	private Accessible<Ia> a;

	private Accessible<Ib> b;

	private ElementaryArithmetic(Accessible<Ia> theA, Accessible<Ib> theB, Class<R> theResultTyp, int theOperation) {
		this.a = theA;
		this.b = theB;
		this.operator = theOperation;
		this.resultTyp = theResultTyp;
	}

	private Byte calcByte(byte a, byte b) throws ArithmeticException {
		Integer re = null;
		if (this.operator == ElementaryArithmetic.PLUS) {
			re = Math.addExact(a, b);
		} else if (this.operator == ElementaryArithmetic.MINUS) {
			re = Math.subtractExact(a, b);
		} else if (this.operator == ElementaryArithmetic.TIMES) {
			re = Math.multiplyExact(a, b);
		} else if (this.operator == ElementaryArithmetic.DIVIDED_BY) {
			re = a / b;
		} else if (this.operator == ElementaryArithmetic.REMAINS) {
			re = a % b;
		}
		if (re == null) {
			return null;
		}
		return re.byteValue(); // TODO: convertiere nur, wenns wirklich passt...
	}

	private Integer calcInt(int a, int b) throws ArithmeticException {
		if (this.operator == ElementaryArithmetic.PLUS) {
			return Math.addExact(a, b);
		} else if (this.operator == ElementaryArithmetic.MINUS) {
			return Math.subtractExact(a, b);
		} else if (this.operator == ElementaryArithmetic.TIMES) {
			return Math.multiplyExact(a, b);
		} else if (this.operator == ElementaryArithmetic.DIVIDED_BY) {
			return a / b;
		} else if (this.operator == ElementaryArithmetic.REMAINS) {
			return a % b;
		}
		return null;
	}

	private Long calcLong(long a, long b) throws ArithmeticException {
		if (this.operator == ElementaryArithmetic.PLUS) {
			return Math.addExact(a, b);
		} else if (this.operator == ElementaryArithmetic.MINUS) {
			return Math.subtractExact(a, b);
		} else if (this.operator == ElementaryArithmetic.TIMES) {
			return Math.multiplyExact(a, b);
		} else if (this.operator == ElementaryArithmetic.DIVIDED_BY) {
			return a / b;
		} else if (this.operator == ElementaryArithmetic.REMAINS) {
			return a % b;
		}
		return null;
	}

	private BigInteger calcBigInt(BigInteger a, BigInteger b) throws ArithmeticException {
		if (this.operator == ElementaryArithmetic.PLUS) {
			return a.add(b);
		} else if (this.operator == ElementaryArithmetic.MINUS) {
			return a.subtract(b);
		} else if (this.operator == ElementaryArithmetic.TIMES) {
			return a.multiply(b);
		} else if (this.operator == ElementaryArithmetic.DIVIDED_BY) {
			return a.divide(b);
		} else if (this.operator == ElementaryArithmetic.REMAINS) {
			return a.remainder(b);
		}
		return null;
	}

	private BigInteger calcBigIntNum(Number a, Number b) throws ArithmeticException {
		if (a instanceof BigInteger) {
			if (b instanceof BigInteger) {
				return calcBigInt((BigInteger) a, (BigInteger) b);
			} else {
				return calcBigInt((BigInteger) a, BigInteger.valueOf(b.longValue()));
			}
		} else {
			if (b instanceof BigInteger) {
				return calcBigInt(BigInteger.valueOf(a.longValue()), (BigInteger) b);
			} else {
				return calcBigInt(BigInteger.valueOf(a.longValue()), BigInteger.valueOf(b.longValue()));
			}
		}
	}

	@Override
	public R getFrom(Structure thisObject) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		if (a == null || b == null) {
			return null;
		} else if (this.resultTyp == Byte.class) {
			return (R) (this.calcByte(a.byteValue(), b.byteValue()));
		} else if (this.resultTyp == Integer.class) {
			return (R) (this.calcInt(a.intValue(), b.intValue()));
		} else if (this.resultTyp == Long.class) {
			return (R) (this.calcLong(a.longValue(), b.longValue()));
		} else if (this.resultTyp == BigInteger.class) {
			return (R) (this.calcBigIntNum(a, b));
		}
		return null;
	}

	public Integer getFromI(Structure thisObject, Class<Integer> resultTyp) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		return this.calcInt(a.intValue(), b.intValue());
	}

	public Long getFromL(Structure thisObject, Class<Long> resultTyp) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		return this.calcLong(a.longValue(), b.longValue());
	}

	public BigInteger getFromBi(Structure thisObject, Class<BigInteger> resultTyp) throws ArithmeticException {
		Ia a = this.a.getFrom(thisObject);
		Ib b = this.b.getFrom(thisObject);
		return this.calcBigIntNum(a, b);
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

	public static Class<? extends Number> getConcretNumClass(Class<?> baseType) throws TypesDoNotMatch {
		if (baseType == Integer.class) {
			return Integer.class;
		}
		if (baseType == BigInteger.class) {
			return BigInteger.class;
		}
		if (baseType == Long.class) {
			return Long.class;
		}
		if (baseType == Byte.class) {
			return Byte.class;
		}
		throw new TypesDoNotMatch(baseType.toString(), Number.class.toString());
	}

}
