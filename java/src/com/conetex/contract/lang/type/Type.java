package com.conetex.contract.lang.type;

import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Label;
import com.conetex.contract.lang.value.implementation.Structure;

public abstract class Type<T> {

	public abstract Attribute<T> createAttribute(Label theName) throws NullLabelException, EmptyLabelException;

	public abstract Class<? extends Value<T>> getValueImplementClass();

	public abstract Class<T> getRawTypeClass();

	public abstract String getName();
	// public abstract Value<T> createValue();

	public abstract Attribute<?> getSubAttribute(String aName);

	public static <V> Attribute<V> createIdentifier(Label theName, TypePrimitive<V> thisObj) throws NullLabelException, EmptyLabelException {
		if(theName == null || theName.get() == null){
			throw new NullLabelException(Type.class.getName() + ".createIdentifier()");
		}
		if(theName.get().length() < 1){
			throw new EmptyLabelException(Type.class.getName() + ".createIdentifier()");
		}
		return AttributePrimitive.<V>create(theName, thisObj);
	}

	public static Attribute<Value<?>[]> _createAttribute2(Label theName, TypeComplex thisObj) throws NullLabelException, EmptyLabelException {
		if(theName == null || theName.get() == null){
			throw new NullLabelException(Type.class.getName() + "._createAttribute2()");
		}
		if(theName.get().length() < 1){
			throw new EmptyLabelException(Type.class.getName() + "._createAttribute2()");
		}
		return null;// AttributeComplex.create(theName, thisObj);
	}

	public static Attribute<Structure> createAttribute(Label theName, TypeComplex thisObj) throws NullLabelException, EmptyLabelException {
		if(theName == null || theName.get() == null){
			throw new NullLabelException(Type.class.getName() + ".createAttribute()");
		}
		if(theName.get().length() < 1){
			throw new EmptyLabelException(Type.class.getName() + ".createAttribute()");
		}
		return AttributeComplex.create(theName, thisObj);
	}
}
