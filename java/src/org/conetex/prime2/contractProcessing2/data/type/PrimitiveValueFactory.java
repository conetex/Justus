package org.conetex.prime2.contractProcessing2.data.type;

import org.conetex.prime2.contractProcessing2.data.Value;

public interface PrimitiveValueFactory<T> {

	public Value<T> createValueImp();
	
}
