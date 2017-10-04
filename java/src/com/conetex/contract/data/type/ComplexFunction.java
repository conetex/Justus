package com.conetex.contract.data.type;

import java.util.HashMap;
import java.util.Map;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.type.Complex.DublicateComplexException;
import com.conetex.contract.data.value.Structure;

public class ComplexFunction extends Complex{

	public Structure prototype;
	
	public void setPrototype(Structure thePrototype){
		this.prototype = thePrototype;
	}
	
	public Structure utilizeStructure(){
		return this.prototype;//unutilize
	}
	
	public void unutilizeStructure(Structure s){
		// TODO implement
	}
	
	ComplexFunction(String theName, Map<String, Integer> theIndex, Attribute<?>[] theOrderedIdentifiers, Map<String, Attribute<?>> fun) {
		super(theName, theIndex, theOrderedIdentifiers, fun);
	}

	private static Complex createImpl(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers, Map<String, Attribute<?>> fun) {
		if (theIndex != null && theOrderedIdentifiers != null) {
			return new ComplexFunction(theName, theIndex, theOrderedIdentifiers, fun);
		}
		return null;
	}

	public static Complex create(final String theName) {
		Map<String, Integer> index = new HashMap<>();
		Attribute<?>[] idents = new Attribute<?>[0];
		return ComplexFunction.createImpl(theName, index, idents, new HashMap<>());
	}

	public static Complex createInit(String typeName, final Attribute<?>[] theOrderedIdentifiers, Map<String, Attribute<?>> fun)
			throws Attribute.DuplicateIdentifierNameExeption, Attribute.NullIdentifierException, DublicateComplexException {
		if (theOrderedIdentifiers.length == 0) {
			return null;
		}
		Map<String, Integer> theIndex = new HashMap<>();
		Complex.buildIndex(theIndex, theOrderedIdentifiers);
		// return new ComplexDataType(theIndex, theOrderedAttributeTypes);
		if (Complex.instances.containsKey(typeName)) {
			throw new DublicateComplexException(typeName);
		}
		Complex re = ComplexFunction.createImpl(typeName, theIndex, theOrderedIdentifiers, fun);
		Complex.instances.put(typeName, re);
		return re;
	}
	
	public static Attribute<?> createAttribute(String attributeName, String typeName, Map<String, Complex> unformedComplexTypes) {
		// ComplexType
		if (typeName == null || typeName.length() == 0) {
			// TODO exception
			return null;
		}
		if (attributeName == null || attributeName.length() == 0) {
			// TODO exception
			return null;
		}
		Complex c = Complex.getInstance(typeName);
		if (c == null) {
			c = unformedComplexTypes.get(typeName);
			if (c == null) {
				c = ComplexFunction.create(typeName);
				unformedComplexTypes.put(typeName, c);
			}
		}
		Attribute<Structure> re = c.createComplexAttribute(attributeName);
		System.out.println("createAttributesValues " + attributeName + " " + typeName + " ==> " + re);
		return re;
	}
	
}
