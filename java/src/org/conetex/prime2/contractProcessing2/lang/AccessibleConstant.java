package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class AccessibleConstant<T> implements Accessible<T> {

	public static <T> AccessibleConstant<T> create(Value<T> theValue) {
		if (theValue == null) {
			return null;
		}
		return new AccessibleConstant<T>(theValue);
	}

	private Value<T> value;


	private AccessibleConstant(Value<T> theValue) {
		this.value = theValue;
	}

	@Override
	public T getFrom(Structure thisObject) {
		return this.value.get();
	}

	@Override
	public void setTo(Structure thisObject, T newValue) throws Invalid {
		this.value.set(newValue);
	}

	public void transSet(Structure thisObject, String newValue) throws Inconvertible, Invalid {
		this.value.setConverted(newValue);
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		return this.value.copy();
	}

	@Override
	public Class<T> getBaseType() {
		return this.value.getBaseType();
	}

}
