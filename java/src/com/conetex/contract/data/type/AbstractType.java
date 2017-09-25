package com.conetex.contract.data.type;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Attribute.EmptyLabelException;
import com.conetex.contract.data.Attribute.NullLabelException;
import com.conetex.contract.data.AttributeComplex;
import com.conetex.contract.data.AttributePrimitive;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.Label;
import com.conetex.contract.data.valueImplement.Structure;

public abstract class AbstractType<T> {

	public abstract Attribute<T> createAttribute(Label theName) throws NullLabelException, EmptyLabelException;

	public abstract Class<? extends Value<T>> getClazz();

	// public abstract Value<T> createValue();

	public abstract <U> Attribute<U> getSubAttribute(String aName);

	public static <V> Attribute<V> createIdentifier(Label theName, Primitive<V> thisObj)
			throws NullLabelException, EmptyLabelException {
		if (theName == null || theName.get() == null) {
			throw new Attribute.NullLabelException();
		}
		if (theName.get().length() < 1) {
			throw new Attribute.EmptyLabelException();
		}
		return AttributePrimitive.<V> create(theName, thisObj);
	}

	public static Attribute<Value<?>[]> _createAttribute2(Label theName, Complex thisObj)
			throws NullLabelException, EmptyLabelException {
		if (theName == null || theName.get() == null) {
			throw new Attribute.NullLabelException();
		}
		if (theName.get().length() < 1) {
			throw new Attribute.EmptyLabelException();
		}
		return null;// AttributeComplex.create(theName, thisObj);
	}

	public static Attribute<Structure> createAttribute(Label theName, Complex thisObj)
			throws NullLabelException, EmptyLabelException {
		if (theName == null || theName.get() == null) {
			throw new Attribute.NullLabelException();
		}
		if (theName.get().length() < 1) {
			throw new Attribute.EmptyLabelException();
		}
		return AttributeComplex.create(theName, thisObj);
	}
}
