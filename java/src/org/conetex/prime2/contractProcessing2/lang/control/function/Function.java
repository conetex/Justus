package org.conetex.prime2.contractProcessing2.lang.control.function;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class Function<V extends Value<?>> implements Accessible<V>{

	
	//private Value<?>[] values;
	//private String[] valueNames;
	private Structure data;
	
	private Accessible<?>[] steps;
	
	@Override
	public V getFrom(Structure thisObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<V> getBaseType() {
		// TODO Auto-generated method stub
		return null;
	}

}
