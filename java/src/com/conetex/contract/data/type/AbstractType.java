package com.conetex.contract.data.type;

import com.conetex.contract.build.exceptionLang.EmptyLabelException;
import com.conetex.contract.build.exceptionLang.NullLabelException;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.AttributeComplex;
import com.conetex.contract.data.AttributePrimitive;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.value.Label;
import com.conetex.contract.data.value.Structure;

public abstract class AbstractType<T> {

	public abstract Attribute<T> createAttribute(Label theName) throws NullLabelException, EmptyLabelException;

	public abstract Class<? extends Value<T>> getValueImplementClass();
	
	public abstract Class<T> getRawTypeClass();

	public abstract String getName();
	// public abstract Value<T> createValue();

	public abstract Attribute<?> getSubAttribute(String aName);

	public static <V> Attribute<V> createIdentifier(Label theName, Primitive<V> thisObj) throws NullLabelException, EmptyLabelException {
		if(theName == null || theName.get() == null){
			throw new NullLabelException(AbstractType.class.getName() + ".createIdentifier()");
		}
		if(theName.get().length() < 1){
			throw new EmptyLabelException(AbstractType.class.getName() + ".createIdentifier()");
		}
		return AttributePrimitive.<V>create(theName, thisObj);
	}

	public static Attribute<Value<?>[]> _createAttribute2(Label theName, Complex thisObj) throws NullLabelException, EmptyLabelException {
		if(theName == null || theName.get() == null){
			throw new NullLabelException(AbstractType.class.getName() + "._createAttribute2()");
		}
		if(theName.get().length() < 1){
			throw new EmptyLabelException(AbstractType.class.getName() + "._createAttribute2()");
		}
		return null;// AttributeComplex.create(theName, thisObj);
	}

	public static Attribute<Structure> createAttribute(Label theName, Complex thisObj) throws NullLabelException, EmptyLabelException {
		if(theName == null || theName.get() == null){
			throw new NullLabelException(AbstractType.class.getName() + ".createAttribute()");
		}
		if(theName.get().length() < 1){
			throw new EmptyLabelException(AbstractType.class.getName() + ".createAttribute()");
		}
		return AttributeComplex.create(theName, thisObj);
	}
}
