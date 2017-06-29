package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

public interface Value <T>{

	public abstract T get();
	
	public abstract T getCopy();
	
	public abstract void set(T value) throws ValueException;

	public abstract void transSet(String value) throws ValueTransformException, ValueException;
	
	public abstract Value<T> createValue();
}
