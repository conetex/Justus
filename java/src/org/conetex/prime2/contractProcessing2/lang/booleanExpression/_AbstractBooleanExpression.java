package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public interface _AbstractBooleanExpression<T> extends Accessible<Boolean>{

	/*
	protected AbstractBooleanExpression(AbstractValueAccess<T> theA, AbstractValueAccess<T> theB) {
		super(theA, theB);
	}
	*/
			
	//public abstract boolean compute(Structure thisObject);
	
	@Override
	public Boolean get(Structure thisObject);

	@Override
	public void set(Structure thisObject, Boolean value) throws Invalid;
	
	@Override
	public Boolean copy(Structure thisObject);
	
}
