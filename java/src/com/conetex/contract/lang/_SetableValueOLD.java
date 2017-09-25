package com.conetex.contract.lang;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Inconvertible;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public class _SetableValueOLD<T> extends Accessible<T> implements Setable<T> {

	public static <T> _SetableValueOLD<T> create2(String thePath, Class<? extends Value<T>> theClass, Class<T> clazz) {
		if (thePath == null) {
			return null;
		}
		return new _SetableValueOLD<>(thePath, theClass);
	}

	public static <T> _SetableValueOLD<T> create(String thePath, Class<? extends Value<T>> theClass) {
		if (thePath == null) {
			return null;
		}
		return new _SetableValueOLD<>(thePath, theClass);
	}

	private String path;

	private final Class<? extends Value<T>> valueClazz;

	private _SetableValueOLD(String thePath, Class<? extends Value<T>> theClass) {
		this.path = thePath;
		this.valueClazz = theClass;
	}

	@Override
	public T getFrom(Structure thisObject) {
		Value<T> value = thisObject.getValue(this.path, this.valueClazz);
		return value.get();
	}

	@Override
	public T setTo(Structure thisObject, T newValue) throws Invalid {
		Value<T> value = thisObject.getValue(this.path, this.valueClazz);
		if (value == null) {
			return null;
		}
		T valueOld = value.get();
		value.set(newValue);
		value = thisObject.getValue(this.path, this.valueClazz);// TODO delete
																// this. was
																// just 4 debug
		System.out.println(valueOld + " setTo " + newValue + " -> " + value.get());
		return newValue;
	}

	public void transSet(Structure thisObject, String newValue) throws Inconvertible, Invalid {
		Value<T> value = thisObject.getValue(this.path, this.valueClazz);
		value.setConverted(newValue);
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		Value<T> value = thisObject.getValue(this.path, this.valueClazz);
		return value.copy();
	}

	@Override
	public Class<T> getBaseType() {
		Primitive<T> pri = Primitive.<T> getInstance(this.valueClazz);// TODO
																		// das
																		// muss
																		// getestet
																		// werden
		if (pri == null) {
			return null;
		}
		return pri.getBaseType();
	}

	@Override
	public <X> Setable<X> asSetable(Class<X> baseType) {
		// TODO Auto-generated method stub
		return null;
	}

}
