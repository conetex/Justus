package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.lang.AbstractPairComputer;
import org.conetex.prime2.contractProcessing2.lang.AbstractValueAccess;

public abstract class AbstractBooleanExpression<T> extends AbstractPairComputer<T> implements AbstractValueAccess<Boolean>{

	protected AbstractBooleanExpression(AbstractValueAccess<T> theA, AbstractValueAccess<T> theB) {
		super(theA, theB);
	}
			
	@Override
	public abstract boolean compute(Structure thisObject);
	
	@Override
	public abstract Boolean get(Structure thisObject);

	@Override
	public abstract void set(Structure thisObject, Boolean value) throws ValueException;
	
	@Override
	public abstract Boolean getCopy(Structure thisObject);
	
}
