package com.conetex.contract.lang.function.access;

import com.conetex.contract.build.BuildFunctions;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.math.ElementaryArithmetic;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class AccessibleValue<T> extends Accessible<T>{

	public static <T> AccessibleValue<T> create(String thePath, Class<T> theClass) {
		if(thePath == null){
			return null;
		}
		return new AccessibleValue<>(thePath, theClass);
	}

	public static <R> AccessibleValue<R> createFunctionSetable(String path, TypeComplex parentTyp, Class<R> expected) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		BuildFunctions.checkType(Attribute.getRawTypeClass(path, parentTyp), expected);
		// String path = n.getValue();
        return AccessibleValue.create(path, expected);
	}

	public static AccessibleValue<?> createFunctionSetableWhatEver(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Class<?> rawType = Attribute.getRawTypeClass(path, parentTyp);
		// String path = n.getValue();
        return AccessibleValue.create(path, rawType);
	}

	public static AccessibleValue<? extends Number> createFunctionSetableNumber(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Class<? extends Number> rawType = ElementaryArithmetic.getConcretNumRawType(Attribute.getRawTypeClass(path, parentTyp));
        return AccessibleValue.create(path, rawType);
	}
	
	final String[] path;

	public String getPath() {
		return this.path[0];
	}

	final Class<T> clazz;

	AccessibleValue(String thePath, Class<T> theClass) {
		super();
		// TODO no command!!! das ist ein design-problem
		this.path = new String[] {thePath};
		this.clazz = theClass;
	}

	@Override
	public String getCommand() {
		return Symbols.comReference();
	}
	
	@Override
	public T getFrom(Structure thisObject) throws ValueCastException {
		Value<T> value = thisObject.getValue(this.getPath(), this.clazz);
		if(value == null){
			return null;
		}
		return value.get();
	}

	public T setTo(Structure thisObject, T newValue) throws Invalid, ValueCastException {
		Value<T> value = thisObject.getValue(this.getPath(), this.clazz);
		if(value == null){
			return null;
		}
		return value.set(newValue);
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid, ValueCastException {
		Value<T> value = thisObject.getValue(this.getPath(), this.clazz);
		if(value == null){
			return null;
		}
		return value.getCopy();
	}

	@Override
	public Class<T> getRawTypeClass() {
		return this.clazz;
	}

	public static <R> Accessible<R> createFunctionRef(String path, TypeComplex parentTyp, Class<R> expected) throws AbstractInterpreterException {
		BuildFunctions.checkType(Attribute.getRawTypeClass(path, parentTyp), expected);
        return AccessibleValue.create(path, expected);
	}

	public static Accessible<?> createFunctionRefWhatEver(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		Class<?> rawType = Attribute.getRawTypeClass(path, parentTyp);
		return AccessibleValue.create(path, rawType);
	}

	public static Accessible<? extends Number> createFunctionRefNum(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		Class<? extends Number> rawType = ElementaryArithmetic.getConcretNumRawType(Attribute.getRawTypeClass(path, parentTyp));
		return AccessibleValue.create(path, rawType);
	}

	@Override
	public Accessible<?>[] getChildren() {
		return super.getChildrenDft();
	}

	@Override
	public String[] getParameter() {
		return this.path;
	}



}
