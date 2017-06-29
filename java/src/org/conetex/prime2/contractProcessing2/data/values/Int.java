package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

public class Int implements Value<Integer>{

	private Integer value;
	
	@Override
	public void set(Integer aValue){
		this.value = aValue;			
	}
	
	@Override
	public final Integer get(){
		return this.value;
	}
	
	@Override
	public void transSet(String value) throws ValueTransformException {
		try {
			Integer v = Integer.parseInt(value);
			this.set(v);
		} catch (NumberFormatException e) {
			throw new ValueTransformException("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public Integer getCopy() {
		if(this.value == null){
			return null;
		}
		return new Integer(this.value);
	}	
	
}
