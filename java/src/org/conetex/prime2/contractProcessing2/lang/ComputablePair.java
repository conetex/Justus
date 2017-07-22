package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;

public abstract class ComputablePair<T> implements Computable{

	private Accessible<T> a;
	
	private Accessible<T> b;

	//protected ComputablePair(Accessible<T> theA, Accessible<T> theB){
	protected ComputablePair(Accessible<T> theA, Accessible<T> theB){
		this.a = theA;
		this.b = theB;
	}
	
	/*
	public ComputablePair(Accessible<Comparable> theA, Accessible<Comparable> theB) {
		this.a = theA;
		this.b = theB;
	}
	*/
	protected Accessible<T> getA(){
		return this.a;
	}
	
	protected Accessible<T> getB(){
		return this.b;
	}
	
	@Override
	public abstract boolean compute(Structure thisObject);
	
}
