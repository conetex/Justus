package com.conetex.contract.lang.type;

import java.util.HashMap;
import java.util.Map;

import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.DublicateComplexException;

public class TypeComplexTyped extends TypeComplex {

	TypeComplex parent;
	
	TypeComplexTyped(String theName, TypeComplex theParent, Map<String, Integer> theIndex, Attribute<?>[] theOrderedIdentifiers) {
		super(theName, theIndex, theOrderedIdentifiers);
		this.parent = theParent;
	}
	
	public static TypeComplexTyped createInit(String typeName, TypeComplex parent) throws DublicateComplexException{
		if(parent == null){
			System.err.println("parent is null");//TODO Exception
			return null;
		}
		TypeComplexTyped re = new TypeComplexTyped(typeName, parent, parent.index, parent.orderedAttributes);
		TypeComplex.put(re);
		return re;
	}

	@Override
	public TypeComplex getSuperType() {
		return this.parent;
	}
	
	@Override
	String[] createCodeNodeGetParams() {
		return new String[] {this.name, this.parent.name};
	}
	


}
