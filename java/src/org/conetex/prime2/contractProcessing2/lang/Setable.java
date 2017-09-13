package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public interface Setable<T> {
	
	public T setTo(Structure thisObject, T value) throws Invalid;

}
