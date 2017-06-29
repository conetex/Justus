package org.conetex.prime2.contractProcessing2.data.type;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.Label;

public abstract class AbstractType<T> {

	public abstract Identifier<T> createIdentifier(Label theName)
			throws NullLabelException, EmptyLabelException;

	public abstract Class<? extends Value<T>> getClazz();

	public abstract Value<T> createValue();

	public abstract <U> Identifier<U> getSubIdentifier(String aName);

	public static <V> Identifier<V> createIdentifier(Label theName, AbstractType<V> thisObj)
			throws NullLabelException, EmptyLabelException {
		if (theName == null || theName.get() == null) {
			throw new Identifier.NullLabelException();
		}
		if (theName.get().length() < 1) {
			throw new Identifier.EmptyLabelException();
		}
		return Identifier.<V> create(theName, thisObj);
	}
}