package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;

public interface AbstractValueAccess<T> {

	public T get(Structure thisObject);

	public void set(Structure thisObject, T value) throws ValueException;
	
	public T getCopy(Structure thisObject);
	
}
