package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public interface Value<T> {

	public T get();
	
	public T copy() throws Invalid;
	
	public void set(T value) throws Invalid;

	public void setConverted(String value) throws Inconvertible, Invalid;
	
	public Class<T> getBaseType();
	
}
