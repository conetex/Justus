package org.conetex.prime2.contractProcessing2.lang.bool.operator;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

//Unary operation
public class Not implements Accessible<Boolean>{

	public static Not create(Accessible<Boolean> theSub){
		if(theSub == null){
			return null;
		}
		return new Not(theSub);
	}
		
	private Accessible<Boolean> sub;
	
	private Not(Accessible<Boolean> theSub){
		this.sub = theSub;
	}

	@Override
	public Boolean getFrom(Structure thisObject) {
		Boolean b = this.sub.getFrom(thisObject);
		if( b == null ){
			return null;
		}		
		if( b ){
			return Boolean.FALSE;
		}		
		return Boolean.TRUE;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}

}