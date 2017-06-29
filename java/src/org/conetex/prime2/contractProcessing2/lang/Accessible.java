package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;

public interface Accessible<T> {//accessible

	public T get(Structure thisObject);

	public T copy(Structure thisObject) throws Invalid;
	
	public void set(Structure thisObject, T value) throws Invalid;
	
}
