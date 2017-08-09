package org.conetex.prime2.contractProcessing2.data.valueImplement;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;

public class Int implements Value<Integer>{

	private Integer actual;
	
	@Override
	public Integer set(Integer aValue){
		this.actual = aValue;
		return this.actual;
	}
	
	@Override
	public final Integer get(){
		return this.actual;
	}
	
	@Override
	public Integer setConverted(String value) throws Inconvertible {
		try {
			Integer v = Integer.parseInt(value);
			return this.set(v);
		} catch (NumberFormatException e) {
			throw new Inconvertible("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public Integer copy() {
		return this.get();
	}

	@Override
	public Class<Integer> getBaseType() {
		return Integer.class;
	}	
	
}