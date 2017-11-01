package com.conetex.contract.lang.access;

import com.conetex.contract.build.BuildFunctions;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.math.ElementaryArithmetic;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class AccessibleValue<T> extends Accessible<T> {

	public static <T> AccessibleValue<T> create(String thePath, Class<T> theClass) {
		if (thePath == null) {
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
		if (value == null) {
			return null;
		}
		return value.get();
	}

	public T setTo(Structure thisObject, T newValue) throws Invalid, ValueCastException {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		if (value == null) {
			return null;
		}
		return value.set(newValue);
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid, ValueCastException {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		if (value == null) {
			return null;
		}
		return value.copy();
	}

	@Override
	public Class<T> getRawTypeClass() {
		return this.clazz;
	}

	public static <R> Accessible<R> createFunctionRef(String path, Complex parentTyp, Class<R> expected) throws AbstractInterpreterException {
		BuildFunctions.checkType(Primitive.getRawTypeClass(Attribute.getID(path, parentTyp)), expected);
		AccessibleValue<R> re = AccessibleValue.create(path, expected);
		return re;
	}

	public static Accessible<?> createFunctionRefWhatEver(String path, Complex parentTyp) throws AbstractInterpreterException {
		Class<?> rawType = Primitive.getRawTypeClass(Attribute.getID(path, parentTyp));
		AccessibleValue<?> re = AccessibleValue.create(path, rawType);
		return re;
	}

	public static Accessible<? extends Number> createFunctionRefNum(String path, Complex parentTyp) throws AbstractInterpreterException {
		Attribute<?> id = Attribute.getID(path, parentTyp);
		Class<? extends Number> rawType = ElementaryArithmetic.getConcretNumRawType(Primitive.getRawTypeClass(id));
		AccessibleValue<? extends Number> re = AccessibleValue.create(path, rawType);
		return re;
	}

}
