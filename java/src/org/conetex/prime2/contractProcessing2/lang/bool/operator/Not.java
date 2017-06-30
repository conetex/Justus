package org.conetex.prime2.contractProcessing2.lang.bool.operator;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Computable;

public class Not implements Accessible<Boolean>, Computable{

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

	@Override
	public boolean compute(Structure thisObject) {
		get(thisObject); // TODO compute ist nur fürs debuggen ... ansonsten ist das ja sinnlos hier!
		return true;
	}

}