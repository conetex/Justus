package org.conetex.prime2.contractProcessing2.lang.bool.operator;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class And extends ComputablePair<Boolean> implements Accessible<Boolean>{

	public static And create(Accessible<Boolean> theA, Accessible<Boolean> theB){
		if(theA == null || theB == null){
			return null;
		}
		return new And(theA, theB);
	}
			
	private And(Accessible<Boolean> theA, Accessible<Boolean> theB){
		super(theA, theB);
	}

	@Override
	public Boolean getFrom(Structure thisObject) {
		Boolean a = super.getA().getFrom(thisObject);
		Boolean b = super.getB().getFrom(thisObject);
		if( a == null || b == null ){
			return null;
		}
		if( a && b ){
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

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}
	
}
