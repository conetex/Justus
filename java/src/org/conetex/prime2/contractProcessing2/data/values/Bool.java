package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.Inconvertible;

public class Bool implements Value<Boolean>{
	
	private Boolean actual;
	
	@Override
	public void set(Boolean aValue){
		this.actual = aValue;
	}
	
	@Override
	public final Boolean get(){
		return this.actual;
	}

	public static Boolean getTrans(String value) {
		if(value.equalsIgnoreCase("true")){
			return Boolean.TRUE;
		}
		else if(value.equalsIgnoreCase("false")){
			return Boolean.FALSE;
		}
		else if(value.equals("1")){
			return Boolean.TRUE;
		}
		else if(value.equals("0")){
			return Boolean.FALSE;
		}
		else {
			return null;
		}
	}
	
	@Override
	public void setConverted(String value) throws Inconvertible {
		Boolean v = getTrans(value);
		if( v == null ){
			throw new Inconvertible("can not convert '" + value + "' to Boolean!");
		}
		this.set(v);
	}

	@Override
	public Boolean copy() {
		return this.get();
	}		
	
}