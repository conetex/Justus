package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

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
	public Boolean get(Structure thisObject) {
		Boolean b = this.sub.get(thisObject);
		if( b ){
			return Boolean.FALSE;
		}		
		return Boolean.TRUE;
	}

	@Override
	public void set(Structure thisObject, Boolean value) throws Invalid {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean copy(Structure thisObject) {
		return this.get(thisObject);
	}

}