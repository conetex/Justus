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
		}
		TypeComplexTyped re = new TypeComplexTyped(typeName, parent, parent.index, parent.orderedAttributes);
		TypeComplex.put(re);
		return re;
	}

	@Override
	String[] createCodeNodeGetParams() {
		return new String[] {this.name, this.parent.name};
	}
	
	public static TypeComplex createInit(String typeName, TypeComplex parent, final Attribute<?>[] theOrderedIdentifiers)
			throws AbstractInterpreterException {
		
		if(theOrderedIdentifiers.length == 0){
			return createInit(typeName, parent);
		}
		if(parent == null){
			System.err.println("parent is null");//TODO Exception
		}

		Attribute<?>[] allAttributes = new Attribute<?>[parent.orderedAttributes.length + theOrderedIdentifiers.length];
		System.arraycopy(parent.orderedAttributes, 0, allAttributes, 0, parent.orderedAttributes.length);
		System.arraycopy(theOrderedIdentifiers, 0, allAttributes, parent.orderedAttributes.length, theOrderedIdentifiers.length);
	
		Map<String, Integer> theIndex = new HashMap<>();
		buildIndex(theIndex, allAttributes);

		TypeComplex re = new TypeComplexTyped(typeName, parent, theIndex, allAttributes);
		TypeComplex.put(re);
		return re;
	}

}
