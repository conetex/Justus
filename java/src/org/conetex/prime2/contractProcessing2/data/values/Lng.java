package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

public class Lng implements Value<Long>{

	private Long value;
	
	@Override
	public void set(Long aValue){
		this.value = aValue;			
	}
	
	@Override
	public final Long get(){
		return this.value;
	}

	@Override
	public void transSet(String value) throws ValueTransformException, NumberFormatException {
		try {
			Long v = Long.parseLong(value);
			this.set(v);	
		} catch (NumberFormatException e) {
			throw new ValueTransformException("can not convert " + value + " to Long", e);
		}			
	}

	@Override
	public Long getCopy() {
		if(this.value == null){
			return null;
		}
		return new Long(this.value);
	}
	
}
