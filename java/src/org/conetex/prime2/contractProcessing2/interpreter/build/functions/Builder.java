package org.conetex.prime2.contractProcessing2.interpreter.build.functions;

import java.util.HashMap;
import java.util.Map;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.interpreter.SyntaxNode;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public abstract class Builder<T, S> {
	
	private Map<String, Builder<? extends S, ?>> childBuilder = new HashMap<String, Builder<? extends S, ?>>();
	
	public void contains(String theOperationName, Builder<? extends S, ?> b){
		this.childBuilder.put(theOperationName, b);
	}
	
	public void contains(Builder<? extends S, ?> b){
		for(String s : b.builder.keySet()){
			this.childBuilder.put(s, b);
		}
	}
	
	public Accessible<? extends S> createChild(SyntaxNode n, Complex parentTyp){
		String name = n.getTag();
		Builder<? extends S, ?> s = this.childBuilder.get(name);
		if(s == null){
			System.err.println("Child Operation " + name + " not found!");
			return null;
		}
		return s.createThis(n, parentTyp);
	}
	
	private Map<String, Builder<T, ?>> builder = new HashMap<String, Builder<T, ?>>();
	
	public void means(String theOperationName, Builder<T, ?> b){
		this.builder.put(theOperationName, b);
	}
	
	public void means(String theOperationName){
		this.builder.put(theOperationName, this);
	}
	
	private Accessible<? extends T> createThis(SyntaxNode n, Complex parentTyp){
		String name = n.getTag();
		Builder<T, ?> s = this.builder.get(name);
		if(s == null){
			System.err.println("Operation " + name + " not found!");
			return null;
		}
		return s.create(n, parentTyp);
	}	
	
	public abstract Accessible<? extends T> create(SyntaxNode n, Complex parentTyp);
	
	
	/*
	private Map<String, Builder<?>> subBuildersObj = new HashMap<String, Builder<?>>();

	public void addStructure(String theOperationName, Builder<?> b){
		this.subBuildersObj.put(theOperationName, b);
	}
	public Builder<?> getBuilderStructure(String theOperationName){
		return this.subBuildersObj.get(theOperationName);
	}	
	public Accessible<?> createSuper(SyntaxNode n, Complex parentTyp){
		String name = n.getTag();
		Builder<?> s = this.getBuilderStructure(name);
		return s.create(n, parentTyp);
	}
	
	
	
	private Map<String, Builder<Boolean>> subBuildersBoolean = new HashMap<String, Builder<Boolean>>();
	
	public void addBoolean(String theOperationName, Builder<Boolean> b){
		this.subBuildersBoolean.put(theOperationName, b);
	}
	public Builder<Boolean> getBuilderBoolean(String theOperationName){
		return this.subBuildersBoolean.get(theOperationName);
	}
	public Accessible<? extends Boolean> createBoolean(SyntaxNode n, Complex parentTyp){
		String name = n.getTag();
		Builder<Boolean> s = this.getBuilderBoolean(name);
		return s.create(n, parentTyp);
	}	
	
	
	private Map<String, Builder<String>> subBuildersString = new HashMap<String, Builder<String>>();
	
	public void addString(String theOperationName, Builder<String> b){
		this.subBuildersString.put(theOperationName, b);
	}
	public Builder<String> getBuilderString(String theOperationName){
		return this.subBuildersString.get(theOperationName);
	}
	public Accessible<? extends String> createString(SyntaxNode n, Complex parentTyp){
		String name = n.getTag();
		Builder<String> s = this.getBuilderString(name);
		return s.create(n, parentTyp);
	}

	
	private Map<String, Builder<Number>> subBuildersNumber = new HashMap<String, Builder<Number>>();
	
	public void addNumber(String theOperationName, Builder<Number> b){
		this.subBuildersNumber.put(theOperationName, b);
	}
	public Builder<Number> getBuilderNumber(String theOperationName){
		return this.subBuildersNumber.get(theOperationName);
	}
	public Accessible<? extends Number> createNumber(SyntaxNode n, Complex parentTyp){
		String name = n.getTag();
		Builder<Number> s = this.getBuilderNumber(name);
		return s.create(n, parentTyp);
	}	
	*/
	
	
	
}
