package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public interface Accessible<T> {//accessible

	public T getFrom(Structure thisObject);

	public T copyFrom(Structure thisObject) throws Invalid;
		
    public Class<T> getBaseType();
    
}
