package com.conetex.contract.lang.type;

import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Label;

public abstract class Type<T> {

	public abstract Attribute<T> createAttribute(Label theName) throws NullLabelException, EmptyLabelException;

	public abstract Class<? extends Value<T>> getValueImplementClass();

	public abstract Class<T> getRawTypeClass();

	public abstract String getName();
	// public abstract Value<T> createValue();

	public abstract boolean isComplex();

	protected abstract Attribute<?> getSubAttribute(String aName);

	static <V> Attribute<V> createIdentifier(Label theName, TypePrimitive<V> thisObj) throws NullLabelException, EmptyLabelException {
		if (theName == null || theName.get() == null) {
			throw new NullLabelException(Type.class.getName() + ".createIdentifier()");
		}
		if (theName.get().length() < 1) {
			throw new EmptyLabelException(Type.class.getName() + ".createIdentifier()");
		}
		return AttributePrimitive.create(theName, thisObj);
	}

	static AttributeComplex createAttribute(Label theName, TypeComplex thisObj) throws NullLabelException, EmptyLabelException {
		if (theName == null || theName.get() == null) {
			throw new NullLabelException(Type.class.getName() + ".createAttribute()");
		}
		if (theName.get().length() < 1) {
			throw new EmptyLabelException(Type.class.getName() + ".createAttribute()");
		}
		return AttributeComplex.create(theName, thisObj);
	}
}
