package com.conetex.contract.lang.function;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.function.control.Function;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
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
		if(theCommand.equals("")){
			System.out.println("SHIT");
		}
	}
	
	public String getCommand(){
		return this.command;
	}
	
	public abstract T getFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract T copyFrom(Structure thisObject) throws AbstractRuntimeException;

	public abstract Class<T> getRawTypeClass();

	//public CodeNode createCodeNode(TypeComplexOfFunction parent) {
	public CodeNode createCodeNode(TypeComplex parent){
		
		List<CodeNode> NodeChildren = new LinkedList<>();
		for(Accessible<?> a : this.children){
			CodeNode x = a.createCodeNode(parent);
			if(x == null) {
				System.out.println("SHIT");
			}
			NodeChildren.add( x );
		}		
		
		return new CodeNode(parent, this.command, this.parameter, NodeChildren);
	}


	
	//public abstract CodeNode persist();

}
