package com.conetex.contract.lang;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public abstract class Accessible<T> {// implements Accessible<T> {

	public abstract T getFrom(Structure thisObject);

	public abstract T copyFrom(Structure thisObject) throws Invalid;

	public abstract Class<T> getBaseType();

	public final Accessible<T> _is(Class<?> baseType) {
		if (baseType == this.getBaseType()) {
			return this;
		}
		return null;
	}

	public final <X> Accessible<X> as(Class<X> baseType) {
		if (baseType.isAssignableFrom(this.getBaseType())) {
			return (Accessible<X>) this;
		}
		return null;
	}

}
