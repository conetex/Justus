package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;

public class And extends AbstractBooleanExpression<Boolean>{

	public static And create(AbstractBooleanExpression<Boolean> theA, AbstractBooleanExpression<Boolean> theB){
		if(theA == null || theB == null){
			return null;
		}
		return new And(theA, theB);
	}
			
	private And(AbstractBooleanExpression<Boolean> theA, AbstractBooleanExpression<Boolean> theB){
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
	public void set(Structure thisObject, Boolean value) throws ValueException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean getCopy(Structure thisObject) {
		return this.get(thisObject);
	}

	@Override
	public boolean compute(Structure thisObject) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
