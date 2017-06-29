package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.values.Structure;

public abstract class AbstractPairComputer<T> {

	private AbstractValueAccess<T> a;
	
	private AbstractValueAccess<T> b;

	protected AbstractPairComputer(AbstractValueAccess<T> theA, AbstractValueAccess<T> theB){
		this.a = theA;
		this.b = theB;
	}
	
	protected AbstractValueAccess<T> getA(){
		return this.a;
	}
	
	protected AbstractValueAccess<T> getB(){
		return this.b;
	}
	
	public abstract boolean compute(Structure thisObject);
	
}
