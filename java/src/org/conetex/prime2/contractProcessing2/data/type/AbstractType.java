package org.conetex.prime2.contractProcessing2.data.type;

import org.conetex.prime2.contractProcessing2.data.Attribute;
import org.conetex.prime2.contractProcessing2.data.Attribute.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Attribute.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.AttributeComplex;
import org.conetex.prime2.contractProcessing2.data.AttributePrimitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.Value;

public abstract class AbstractType<T> {

	public abstract Attribute<T> createAttribute(Label theName)
			throws NullLabelException, EmptyLabelException;

	public abstract Class<? extends Value<T>> getClazz();

	//public abstract Value<T> createValue();

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
		return null;//AttributeComplex.create(theName, thisObj);
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
