package com.conetex.contract.lang.function;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class Accessible<T> {// implements Accessible<T> {

	String command;
	
	String[] parameter;
	
	Accessible<?>[] children;
	
	protected Accessible(String theCommand, String[] theParameter, Accessible<?>[] theChildren){
		this.command = theCommand;
		this.parameter = theParameter;
		this.children = theChildren;		
	}
	
	public abstract T getFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract T copyFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract Class<T> getRawTypeClass();

	public CodeNode createCodeNode(){
		// TODO implement
		return null;
	}
	
	//public abstract CodeNode persist();

}
