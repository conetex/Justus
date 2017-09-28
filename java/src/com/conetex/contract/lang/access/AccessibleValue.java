package com.conetex.contract.lang.access;

import com.conetex.contract.build.BuildFunctions;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.math.ElementaryArithmetic;
import com.conetex.contract.runtime.exceptionValue.Invalid;
import com.conetex.contract.runtime.exceptionValue.ValueCastException;

public class AccessibleValue<T> extends Accessible<T> {

	public static <T> AccessibleValue<T> create(String thePath, Class<T> theClass) {
		if (thePath == null) {
			return null;
		}
		return new AccessibleValue<>(thePath, theClass);
	}

	public static <T extends Number> AccessibleValue<T> _createNum(String thePath, Class<T> theClass) {
		if (thePath == null) {
			System.err.println("thePath is null");
			return null;
		}
		return new AccessibleValue<>(thePath, theClass);
	}

	protected String			path;

	protected final Class<T>	clazz;

	protected AccessibleValue(String thePath, Class<T> theClass) {
		this.path = thePath;
		this.clazz = theClass;
	}

	@Override
	public T getFrom(Structure thisObject) throws ValueCastException {

		// return thisObject.getValueNewNew(this.path, this.clazz);

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
		// just 4 debug:
		// T valueOld = value.get();
		return value.set(newValue);
		// just 4 debug:
		// value = thisObject.getValueNew(this.path, this.clazz);
		// System.out.println(valueOld + " setTo " + newValue + " -> " +
		// value.get());
		// return newValue;
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
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		BuildFunctions.checkType(Primitive.getRawTypeClass(Attribute.getID(path, parentTyp)), expected);
		// String path = n.getValue();
		AccessibleValue<R> re = AccessibleValue.create(path, expected);
		return re;
	}

	public static Accessible<?> createFunctionRefWhatEver(String path, Complex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Class<?> rawType = Primitive.getRawTypeClass(Attribute.getID(path, parentTyp));
		// String path = n.getValue();
		AccessibleValue<?> re = AccessibleValue.create(path, rawType);
		return re;
	}

	public static Accessible<? extends Number> createFunctionRefNum(String path, Complex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Attribute<?> id = Attribute.getID(path, parentTyp);
		Class<? extends Number> rawType = ElementaryArithmetic.getConcretNumRawType(Primitive.getRawTypeClass(id));
		AccessibleValue<? extends Number> re = AccessibleValue.create(path, rawType);
		return re;
	}

}
