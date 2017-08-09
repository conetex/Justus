package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class SetableValue<T> implements Setable<T> {

	public static <T> SetableValue<T> create2(String thePath, Class<? extends Value<T>> theClass, Class<T> clazz) {
		if (thePath == null) {
			return null;
		}
		return new SetableValue<T>(thePath, theClass);
	}
	
	public static <T> SetableValue<T> create(String thePath, Class<? extends Value<T>> theClass) {
		if (thePath == null) {
			return null;
		}
		return new SetableValue<T>(thePath, theClass);
	}

	private String path;

	private final Class<? extends Value<T>> valueClazz;

	private SetableValue(String thePath, Class<? extends Value<T>> theClass) {
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
		T valueOld = value.get();
		value.set(newValue);
		System.out.println( valueOld + " setTo " + newValue + " -> " + value.get());
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
		Primitive<T> pri = Primitive.<T>getInstance( this.valueClazz );// TODO das muss getestet werden
		if(pri == null){
			return null;
		}
		return pri.getBaseType();
	}



}
