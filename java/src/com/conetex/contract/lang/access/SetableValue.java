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

public class SetableValue<T> extends AccessibleValue<T> implements Setable<T> {

	public static <T> SetableValue<T> create(String thePath, Class<T> theClass) {
		if (thePath == null) {
			return null;
		}
		return new SetableValue<>(thePath, theClass);
	}

	private SetableValue(String thePath, Class<T> theClass) {
		super(thePath, theClass);
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

	public static <R> SetableValue<R> createFunctionSetable(String path, Complex parentTyp, Class<R> expected) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		BuildFunctions.checkType(Primitive.getRawTypeClass(Attribute.getID(path, parentTyp)), expected);
		// String path = n.getValue();
		SetableValue<R> re = SetableValue.create(path, expected);
		return re;
	}

	public static SetableValue<?> createFunctionSetableWhatEver(String path, Complex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Class<?> rawType = Primitive.getRawTypeClass(Attribute.getID(path, parentTyp));
		// String path = n.getValue();
		SetableValue<?> re = SetableValue.create(path, rawType);
		return re;
	}

	public static SetableValue<? extends Number> createFunctionSetableNumber(String path, Complex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Attribute<?> id = Attribute.getID(path, parentTyp);
		Class<? extends Number> rawType = ElementaryArithmetic.getConcretNumRawType(Primitive.getRawTypeClass(id));
		SetableValue<? extends Number> re = SetableValue.create(path, rawType);
		return re;
	}
}
