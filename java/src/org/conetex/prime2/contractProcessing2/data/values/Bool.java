package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

public class Bool implements Value<Boolean>{
	
	private Boolean value;
	
	@Override
	public void set(Boolean aValue){
		this.value = aValue;
	}
	
	@Override
	public final Boolean get(){
		return this.value;
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
	public void transSet(String value) throws ValueTransformException {
		Boolean v = getTrans(value);
		if( v == null ){
			throw new ValueTransformException("can not convert '" + value + "' to Boolean!");
		}
		this.set(v);
	}

	@Override
	public Boolean getCopy() {
		if(this.value == null){
			return null;
		}
		return new Boolean(this.value);
	}		
	
}
