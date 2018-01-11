package com.conetex.contract.lang.type;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.conetex.contract.build.Constants;
import com.conetex.contract.build.exceptionFunction.DublicateComplexException;

public class TypeComplexSub extends TypeComplex {

	TypeComplex parent;

	TypeComplexSub(String theName, TypeComplex theParent, Map<String, Integer> theIndex, Attribute<?>[] theOrderedIdentifiers) {
		super(theName, theIndex, theOrderedIdentifiers);
		this.parent = theParent;
	}

	public static TypeComplexSub createInit(String typeName, TypeComplex parent) throws DublicateComplexException {
		if (parent == null) {
			System.err.println("parent is null");// TODO Exception
			return null;
		}
		TypeComplexSub re = new TypeComplexSub(typeName, parent, parent.index, Constants.noAttributes);
		TypeComplex.put(re);
		return re;
	}

	@Override
	public Set<String> getSubAttributeNames() {
		Set<String> re = new TreeSet<>();
		re.addAll(super.getSubAttributeNames());
		re.addAll(this.parent.getSubAttributeNames());
		// Set<String> re = Stream.concat(super.getSubAttributeNames().stream(),
		// parent.getSubAttributeNames().stream()).collect(Collectors.toSet());
		return re;
	}

	@Override
	public Attribute<?>[] getSubAttributes() {
		Attribute<?>[] theOrderedIdentifiers = super.getSubAttributes();
		Attribute<?>[] parentsOrderedIdentifiers = this.parent.getSubAttributes();
		Attribute<?>[] allAttributes = new Attribute<?>[parentsOrderedIdentifiers.length + theOrderedIdentifiers.length];
		System.arraycopy(theOrderedIdentifiers, 0, allAttributes, 0, theOrderedIdentifiers.length);
		System.arraycopy(parentsOrderedIdentifiers, 0, allAttributes, theOrderedIdentifiers.length, parentsOrderedIdentifiers.length);
		return allAttributes;
	}

	@Override
	public int getAttributesSize() {
		return this.orderedAttributes.length + this.parent.getAttributesSize();
	}

	@Override
	public Attribute<?> getSubAttribute(int i) {
		if (i < 0) {
			// TODO darf auf keinen fall vorkommen
			return null;
		}
		if (i >= super.orderedAttributes.length) {
			return this.parent.getSubAttribute(i - super.orderedAttributes.length);
		}
		else {
			return super.orderedAttributes[i];
		}
	}

	@Override
	public TypeComplex getSuperType() {
		return this.parent;
	}

	@Override
	String[] createCodeNodeGetParams() {
		return new String[] { this.name, this.parent.name };
	}
}
