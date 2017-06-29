package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.data.values.exception.Inconvertible;

public interface Value <T>{

	public abstract T get();
	
	public abstract T copy() throws Invalid;
	
	public abstract void set(T value) throws Invalid;

	public abstract void setConverted(String value) throws Inconvertible, Invalid;
	
}
