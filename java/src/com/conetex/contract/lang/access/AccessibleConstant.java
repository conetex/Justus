package com.conetex.contract.lang.access;

import java.math.BigInteger;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbol;
import com.conetex.contract.build.exceptionLang.TypeNotDeterminated;
import com.conetex.contract.build.exceptionLang.UnknownCommand;
import com.conetex.contract.build.exceptionLang.UnknownCommandParameter;
import com.conetex.contract.build.exceptionLang.UnknownType;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.value.BigInt;
import com.conetex.contract.data.value.Bool;
import com.conetex.contract.data.value.Int;
import com.conetex.contract.data.value.Lng;
import com.conetex.contract.data.value.SizedASCII;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public class AccessibleConstant<T> extends Accessible<T> {

	public static Accessible<? extends Number> createNumConst(CodeNode n, Complex parentTyp) throws UnknownType, TypeNotDeterminated, UnknownCommandParameter, UnknownCommand {
		Accessible<? extends Number> re = try2CreateNumConst(n, parentTyp);
		if (re == null) {
			throw new TypeNotDeterminated("number const-Type: " + n.getName());
		}
		return re;
	}

	public static Accessible<? extends Number> try2CreateNumConst(CodeNode n, Complex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
		String name = n.getCommand();
		if (name.equals(Symbol.BINT)) {
			return AccessibleConstant.<BigInteger>create2(BigInteger.class, n.getValue());
		}
		else if (name.equals(Symbol.INT)) {
			return AccessibleConstant.<Integer>create2(Integer.class, n.getValue());
		}
		else if (name.equals(Symbol.LNG)) {
			return AccessibleConstant.<Long>create2(Long.class, n.getValue());
		}
		return null;
	}

	public static Accessible<Boolean> try2CreateBoolConst(CodeNode n, Complex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
		String name = n.getCommand();
		if (name.equals(Symbol.BOOL)) {
			return AccessibleConstant.<Boolean>create2(Boolean.class, n.getValue());
		}
		return null;
	}

	public static Accessible<Structure> try2CreateStructureConst(CodeNode n, Complex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
		String name = n.getCommand();
		if (name.equals(Symbol.STRUCT)) {
			return AccessibleConstant.<Structure>create2(Structure.class, n.getValue());
		}
		return null;
	}

	public static <RE> AccessibleConstant<RE> create2(Class<RE> expectedBaseTyp, String value) throws UnknownType {
		Primitive<RE> theClass = null;
		try {

			if (expectedBaseTyp == BigInteger.class) {
				theClass = Primitive.<RE>getInstance(BigInt.class, expectedBaseTyp);
			}
			else if (expectedBaseTyp == Long.class) {
				theClass = Primitive.<RE>getInstance(Lng.class, expectedBaseTyp);
			}
			else if (expectedBaseTyp == Integer.class) {
				theClass = Primitive.<RE>getInstance(Int.class, expectedBaseTyp);
			}
			else if (expectedBaseTyp == Byte.class) {
				// TODO Typen klären ...
				return null;
			}
			else if (expectedBaseTyp == String.class) {
				theClass = Primitive.<RE>getInstance(SizedASCII.class, expectedBaseTyp);
			}
			else if (expectedBaseTyp == Boolean.class) {
				theClass = Primitive.<RE>getInstance(Bool.class, expectedBaseTyp);
			}

		}
		catch (AbstractTypException e) {
			// convert TypeCastException InterpreterException
			throw new UnknownType(expectedBaseTyp.getName());
		}

		if (theClass != null) {
			Value<RE> constVal = theClass.createValue();
			try {
				constVal.setConverted(value);
			}
			catch (Inconvertible | Invalid e) {
				// TODO convert runtime exceptions to build Lang Exception
				e.printStackTrace();
				return null;
			}
			AccessibleConstant<RE> re = AccessibleConstant.<RE>create(constVal);
			return re;
		}
		else {
			// TODO error ... Primitive.<RE>getInstance can return null ...
		}
		return null;
	}

	private static <T> AccessibleConstant<T> create(Value<T> theValue) {
		if (theValue == null) {
			return null;
		}
		return new AccessibleConstant<>(theValue);
	}

	private Value<T> value;

	private AccessibleConstant(Value<T> theValue) {
		this.value = theValue;
	}

	@Override
	public T getFrom(Structure thisObject) {
		return this.value.get();
	}

	/*
	 * @Override public void setTo(Structure thisObject, T newValue) throws Invalid
	 * { this.value.set(newValue); }
	 * 
	 * public void transSet(Structure thisObject, String newValue) throws
	 * Inconvertible, Invalid { this.value.setConverted(newValue); }
	 */

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		return this.value.copy();
	}

	@Override
	public Class<T> getRawTypeClass() {
		return this.value.getRawTypeClass();
	}

}
