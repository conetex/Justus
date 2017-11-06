package com.conetex.contract.lang.function.access;

import com.conetex.contract.build.BuildFunctions;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
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

	protected String path;

	public String getPath() {
		return this.path;
	}

	protected final Class<T> clazz;

	protected AccessibleValue(String thePath, Class<T> theClass) {
		this.path = thePath;
		this.clazz = theClass;
	}

	@Override
	public T getFrom(Structure thisObject) throws ValueCastException {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		if(value == null){
			return null;
		}
		return value.get();
	}

	public T setTo(Structure thisObject, T newValue) throws Invalid, ValueCastException {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		if(value == null){
			return null;
		}
		return value.set(newValue);
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid, ValueCastException {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
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
		AccessibleValue<R> re = AccessibleValue.create(path, expected);
		return re;
	}

	public static Accessible<?> createFunctionRefWhatEver(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		Class<?> rawType = Attribute.getRawTypeClass(path, parentTyp);
		AccessibleValue<?> re = AccessibleValue.create(path, rawType);
		return re;
	}

	public static Accessible<? extends Number> createFunctionRefNum(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		Class<? extends Number> rawType = ElementaryArithmetic.getConcretNumRawType(Attribute.getRawTypeClass(path, parentTyp));
		AccessibleValue<? extends Number> re = AccessibleValue.create(path, rawType);
		return re;
	}

}