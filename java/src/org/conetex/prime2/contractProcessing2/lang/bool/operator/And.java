package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class And extends ComputablePair<Boolean> implements _AbstractBooleanExpression<Boolean>{

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
	public Boolean get(Structure thisObject) {
		boolean a = super.getA().get(thisObject);
		boolean b = super.getB().get(thisObject);
		if( a && b ){
			return Boolean.TRUE;
		}		
		return Boolean.FALSE;
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
		// TODO Auto-generated method stub
		return false;
	}
	
}
