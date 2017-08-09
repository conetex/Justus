package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public interface Value<T> {

	public T get();
	
	public T copy() throws Invalid;
	
	public T set(T value) throws Invalid;

	public T setConverted(String value) throws Inconvertible, Invalid;
	
	public Class<T> getBaseType();
	
}
