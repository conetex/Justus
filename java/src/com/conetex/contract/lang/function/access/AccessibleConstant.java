package com.conetex.contract.lang.function.access;

import java.math.BigInteger;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.TypeNotDeterminated;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.build.exceptionFunction.UnknownType;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypePrimitive;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public class AccessibleConstant<T> extends Accessible<T>{

	public static Accessible<? extends Number> createNumConst(CodeNode n, TypeComplex parentTyp)
			throws UnknownType, TypeNotDeterminated, UnknownCommandParameter, UnknownCommand {
		Accessible<? extends Number> re = try2CreateNumConst(n, parentTyp);
		if(re == null){
			throw new TypeNotDeterminated("number const-Type: " + n.getParameter(Symbols.paramName()));
		}
		return re;
	}

	public static Accessible<? extends Number> try2CreateNumConst(CodeNode n, TypeComplex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
		String name = n.getCommand();
		if(name.equals(Symbols.comBigInt())){
			return AccessibleConstant.create2(BigInteger.class, n);
		}
		else if(name.equals(Symbols.comInt())){
			return AccessibleConstant.create2(Integer.class, n);
		}
		else if(name.equals(Symbols.comLng())){
			return AccessibleConstant.create2(Long.class, n);
		}
		return null;
	}

	public static Accessible<Boolean> try2CreateBoolConst(CodeNode n, TypeComplex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
		String name = n.getCommand();
		if(name.equals(Symbols.comBool())){
			return AccessibleConstant.create2(Boolean.class, n);
		}
		return null;
	}

	public static Accessible<Structure> try2CreateStructureConst(CodeNode n, TypeComplex parentTyp) throws UnknownType, UnknownCommandParameter, UnknownCommand {
		String name = n.getCommand();
		if(name.equals(Symbols.comStructure())){
			return AccessibleConstant.create2(Structure.class, n);
		}
		return null;
	}

	public static <RE> AccessibleConstant<RE> create2(Class<RE> expectedBaseTyp, CodeNode thisNode) throws UnknownType, UnknownCommandParameter, UnknownCommand {
		TypePrimitive<RE> theClass = null;
		try{

			if(expectedBaseTyp == BigInteger.class){
				theClass = TypePrimitive.getInstance(Symbols.CLASS_BINT, expectedBaseTyp);
			}
			else if(expectedBaseTyp == Long.class){
				theClass = TypePrimitive.getInstance(Symbols.CLASS_LNG, expectedBaseTyp);
			}
			else if(expectedBaseTyp == Integer.class){
				theClass = TypePrimitive.getInstance(Symbols.CLASS_INT, expectedBaseTyp);
			}
			else if(expectedBaseTyp == Byte.class){
				// TODO Typen klaeren ...
				return null;
			}
			else if(expectedBaseTyp == String.class){
				theClass = TypePrimitive.getInstance(Symbols.CLASS_SIZED_ASCII, expectedBaseTyp);
			}
			else if(expectedBaseTyp == Boolean.class){
				theClass = TypePrimitive.getInstance(Symbols.CLASS_BOOL, expectedBaseTyp);
			}

		}
		catch(AbstractTypException e){
			// convert TypeCastException InterpreterException
			throw new UnknownType(expectedBaseTyp.getName());
		}

		if(theClass != null){
			Value<RE> constVal = theClass.createValue(thisNode);
			
			try{
				constVal.setConverted( thisNode.getParameter(Symbols.paramValue()) );
			}
			catch(Inconvertible | Invalid e){
				// TODO convert runtime exceptions to build Lang Exception
				e.printStackTrace();
				return null;
			}
			return AccessibleConstant.<RE>create(constVal);
		}
		else{
			// TODO error ... Primitive.<RE>getInstance can return null ...
		}
		return null;
	}

	private static <T> AccessibleConstant<T> create(Value<T> theValue) {
		if(theValue == null){
			return null;
		}
		return new AccessibleConstant<>(theValue);
	}

	private final Value<T> value;

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
		return this.value.getCopy();
	}

	@Override
	public Class<T> getRawTypeClass() {
		return this.value.getRawTypeClass();
	}

}
