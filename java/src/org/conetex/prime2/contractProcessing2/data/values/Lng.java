package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.Inconvertible;

public class Lng implements Value<Long>{

	private Long actual;
	
	@Override
	public void set(Long aValue){
		this.actual = aValue;			
	}
	
	@Override
	public final Long get(){
		return this.actual;
	}

	@Override
	public void setConverted(String value) throws Inconvertible, NumberFormatException {
		try {
			Long v = Long.parseLong(value);
			this.set(v);	
		} catch (NumberFormatException e) {
			throw new Inconvertible("can not convert " + value + " to Long", e);
		}			
	}

	@Override
	public Long copy() {
		return this.get();
	}

}