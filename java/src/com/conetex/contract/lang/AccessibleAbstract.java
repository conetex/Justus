package com.conetex.contract.lang;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public abstract class AccessibleAbstract<T> {//implements Accessible<T> {

	public abstract T getFrom(Structure thisObject);

	public abstract T copyFrom(Structure thisObject) throws Invalid;

	public abstract Class<T> getBaseType();

	public final AccessibleAbstract<T> _is(Class<?> baseType) {
		if (baseType == this.getBaseType()) {
			return this;
		}
		return null;
	}

	public final <X> AccessibleAbstract<X> as(Class<X> baseType) {
		if (baseType.isAssignableFrom(this.getBaseType())) {
			return (AccessibleAbstract<X>) this;
		}
		return null;
	}

}
