package com.conetex.contract.lang.type;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.DublicateComplexException;
import com.conetex.contract.lang.value.implementation.Structure;

public class TypeComplexTemp extends TypeComplex {

	public List<AttributeComplex> listOfUnformedAttributes = new LinkedList<>(); 
	
	TypeComplexTemp(String theName, Map<String, Integer> theIndex, Attribute<?>[] theOrderedIdentifiers) {
		super(theName, theIndex, theOrderedIdentifiers);
	}

	
	private static TypeComplexTemp createImpl(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		if(theIndex != null && theOrderedIdentifiers != null){
			return new TypeComplexTemp(theName, theIndex, theOrderedIdentifiers);
		}
		return null;
	}

	static TypeComplexTemp create(final String theName) {
		Map<String, Integer> index = new HashMap<>();
		Attribute<?>[] idents = new Attribute<?>[0];
		return TypeComplexTemp.createImpl(theName, index, idents);
	}

}
