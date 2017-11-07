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

public class SetableValue<T> extends AccessibleValue<T> implements Setable<T>{

	public static <T> SetableValue<T> create(String thePath, Class<T> theClass) {
		if(thePath == null){
			return null;
		}
		return new SetableValue<>(thePath, theClass);
	}

	private SetableValue(String thePath, Class<T> theClass) {
		super(thePath, theClass);
	}

	public T setTo(Structure thisObject, T newValue) throws Invalid, ValueCastException {
		Value<T> value = thisObject.getValue(this.path, this.clazz);
		if(value == null){
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

	public static <R> SetableValue<R> createFunctionSetable(String path, TypeComplex parentTyp, Class<R> expected) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		BuildFunctions.checkType(Attribute.getRawTypeClass(path, parentTyp), expected);
		// String path = n.getValue();
        return SetableValue.create(path, expected);
	}

	public static SetableValue<?> createFunctionSetableWhatEver(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Class<?> rawType = Attribute.getRawTypeClass(path, parentTyp);
		// String path = n.getValue();
        return SetableValue.create(path, rawType);
	}

	public static SetableValue<? extends Number> createFunctionSetableNumber(String path, TypeComplex parentTyp) throws AbstractInterpreterException {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		Class<? extends Number> rawType = ElementaryArithmetic.getConcretNumRawType(Attribute.getRawTypeClass(path, parentTyp));
        return SetableValue.create(path, rawType);
	}
}
