package org.conetex.prime2.contractProcessing2.lang.control.function;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class Function<V extends Value<?>> implements Accessible<V>{

	public static <SV extends Value<?>> Function<SV> create(Structure theData, Accessible<?>[] theSteps){
		if(theData == null || theSteps == null){
			return null;
		}
		return new Function<SV>(theData, theSteps);
	}
	//private Value<?>[] values;
	//private String[] valueNames;
	private Structure data;
	
	private Accessible<?>[] steps;
	
	private Function(Structure theData, Accessible<?>[] theSteps){
		this.data = theData;
		this.steps = theSteps;
	}
	
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