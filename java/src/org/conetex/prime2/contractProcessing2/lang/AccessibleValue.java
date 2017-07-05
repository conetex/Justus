package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class AccessibleValue<T> implements Accessible<T> {

	public static <T> AccessibleValue<T> create(String thePath, Class<? extends Value<T>> theClass) {
		if (thePath == null) {
			return null;
		}
		return new AccessibleValue<T>(thePath, theClass);
	}

	private String path;

	private final Class<? extends Value<T>> valueClazz;

	private AccessibleValue(String thePath, Class<? extends Value<T>> theClass) {
		this.path = thePath;
		this.valueClazz = theClass;
	}

	@Override
	public T getFrom(Structure thisObject) {
		Value<T> value = thisObject.getValue(this.path, this.valueClazz);
		return value.get();
	}

	@Override
	public void setTo(Structure thisObject, T newValue) throws Invalid {
		Value<T> value = thisObject.getValue(this.path, this.valueClazz);
		value.set(newValue);
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
