package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Computable;

public class IsNull implements Accessible<Boolean>, Computable{

	public static IsNull create(Accessible<Boolean> theSub){
		if(theSub == null){
			return null;
		}
		return new IsNull(theSub);
	}
		
	private Accessible<Boolean> sub;
	
	private IsNull(Accessible<Boolean> theSub){
		this.sub = theSub;
	}

	@Override
	public Boolean getFrom(Structure thisObject) {
		Boolean b = this.sub.getFrom(thisObject);
		if( b == null ){
			return Boolean.TRUE;
		}		
		return Boolean.FALSE;
	}

	@Override
	public void setTo(Structure thisObject, Boolean value) throws Invalid {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public boolean compute(Structure thisObject) {
		getFrom(thisObject); // TODO compute ist nur fürs debuggen ... ansonsten ist das ja sinnlos hier!
		return true;
	}

}