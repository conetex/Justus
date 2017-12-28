package com.conetex.contract.lang.function.access;

import java.math.BigInteger;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.TypeNotDeterminated;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypePrimitive;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public abstract class AccessibleConstant<T> extends Accessible<T> {

	public static Accessible<? extends Number> createNumConst(CodeNode n, TypeComplex parentTyp)
			throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
		Accessible<? extends Number> re = try2CreateNumConst(n, parentTyp);
		if (re == null) {
			throw new TypeNotDeterminated("number const-Type: " + n.getParameter(Symbols.paramName()));
		}
		return re;
	}

	@Override
	public abstract String getCommand();

	public static Accessible<? extends Number> try2CreateNumConst(CodeNode n, TypeComplex parentTyp)
			throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
		String name = n.getCommand();
		if (name.equals(Symbols.comBigInt())) {
			return AccessibleConstant.create2(BigInteger.class, n);
		}
		else if (name.equals(Symbols.comInt())) {
			return AccessibleConstant.create2(Integer.class, n);
		}
		else if (name.equals(Symbols.comLng())) {
			return AccessibleConstant.create2(Long.class, n);
		}
		return null;
	}

	public static Accessible<Boolean> try2CreateBoolConst(CodeNode n, TypeComplex parentTyp)
			throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
		String name = n.getCommand();
		if (name.equals(Symbols.comBool())) {
			return AccessibleConstant.create2(Boolean.class, n);
		}
		return null;
	}

	public static Accessible<Structure> try2CreateStructureConst(CodeNode n, TypeComplex parentTyp)
			throws Inconvertible, Invalid, AbstractInterpreterException, AbstractTypException {
		String name = n.getCommand();
		if (name.equals(Symbols.comStructure())) {
			return AccessibleConstant.create2(Structure.class, n);
		}
		return null;
	}

	// public static <RE> AccessibleConstant<RE> create2(Class<RE>
	// expectedBaseTyp, CodeNode thisNode)

	private static <RE> Value<RE> getValue(TypePrimitive<RE> theClass, CodeNode thisNode) throws AbstractInterpreterException, Inconvertible, Invalid {
		Value<RE> constVal = theClass.createValue(thisNode);
		if (constVal == null) {
			throw new AbstractInterpreterException("can not create value");
		}
		constVal.setConverted(thisNode.getParameter(Symbols.paramValue()));
		return constVal;
	}

	public static <RE> AccessibleConstant<RE> create2(Class<RE> expectedBaseTyp, CodeNode thisNode)
			throws AbstractInterpreterException, Inconvertible, Invalid, AbstractTypException {
		if (expectedBaseTyp == BigInteger.class) {
			TypePrimitive<RE> theClass = TypePrimitive.getInstanceNotNull(Symbols.CLASS_BINT, expectedBaseTyp);
			return new AccessibleConstant<RE>(getValue(theClass, thisNode)) {
				@Override
				public String getCommand() {
					return Symbols.comBigInt();
				}
			};
		}
		else if (expectedBaseTyp == Long.class) {
			TypePrimitive<RE> theClass = TypePrimitive.getInstanceNotNull(Symbols.CLASS_LNG, expectedBaseTyp);
			return new AccessibleConstant<RE>(getValue(theClass, thisNode)) {
				@Override
				public String getCommand() {
					return Symbols.comLng();
				}
			};
		}
		else if (expectedBaseTyp == Integer.class) {
			TypePrimitive<RE> theClass = TypePrimitive.getInstanceNotNull(Symbols.CLASS_INT, expectedBaseTyp);
			return new AccessibleConstant<RE>(getValue(theClass, thisNode)) {
				@Override
				public String getCommand() {
					return Symbols.comInt();
				}
			};
		}
		else if (expectedBaseTyp == Byte.class) {
			// TODO Typen klaeren ...
			return null;
		}
		else if (expectedBaseTyp == String.class) {// TODO ascii klaeren...
			TypePrimitive<RE> theClass = TypePrimitive.getInstanceNotNull(Symbols.CLASS_SIZED_ASCII, expectedBaseTyp);
			return new AccessibleConstant<RE>(getValue(theClass, thisNode)) {
				@Override
				public String getCommand() {
					return Symbols.comStr();
				}
			};
		}
		else if (expectedBaseTyp == Boolean.class) {
			TypePrimitive<RE> theClass = TypePrimitive.getInstanceNotNull(Symbols.CLASS_BOOL, expectedBaseTyp);
			return new AccessibleConstant<RE>(getValue(theClass, thisNode)) {
				@Override
				public String getCommand() {
					return Symbols.comBool();
				}
			};
		}
		// TODO error ... Primitive.<RE>getInstance can return null ...
		return null;
	}

	private final Value<T> value;

	AccessibleConstant(Value<T> theValue) {
		super();// TODO no command!!! das ist ein design-problem
		this.value = theValue;
	}

	@Override
	public Accessible<?>[] getChildren() {
		return super.getChildrenDft();
	}

	@Override
	public String[] getParameter() {
		return new String[] { this.value.get().toString() };
	}

	@Override
	public T getFrom(Structure thisObject) {
		return this.value.get();
	}

	/*
	 * @Override public void setTo(Structure thisObject, T newValue) throws
	 * Invalid { this.value.set(newValue); }
	 * 
	 * public void transSet(Structure thisObject, String newValue) throws
	 * Inconvertible, Invalid { this.value.setConverted(newValue); }
	 */

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		return this.value.getCopy();
	}

	@Override
	public Class<T> getRawTypeClass() {
		return this.value.getRawTypeClass();
	}

}
