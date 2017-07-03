package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.Inconvertible;

public class Int implements Value<Integer>{

	private Integer actual;
	
	@Override
	public void set(Integer aValue){
		this.actual = aValue;
	}
	
	@Override
	public final Integer get(){
		return this.actual;
	}
	
	@Override
	public void setConverted(String value) throws Inconvertible {
		try {
			Integer v = Integer.parseInt(value);
			this.set(v);
		} catch (NumberFormatException e) {
			throw new Inconvertible("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public Integer copy() {
		return this.get();
	}	
	
}
