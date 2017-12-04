package com.conetex.contract.lang.type;

import java.util.LinkedList;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.UnknownAttribute;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.SizedASCII;
import com.conetex.contract.lang.value.implementation.Structure;

public abstract class Attribute<T> {

	public abstract SizedASCII getLabel();

	public abstract Value<T> createValue(Structure theParent, CodeNode theNode);

	public abstract Value<T> createNewValue(Structure theParent);
	
	public abstract Type<T> getType();

	private static Attribute<?> getAttribute(String idName, TypeComplex parentTyp) throws UnknownAttribute {
		if(parentTyp == null){
			System.err.println("was geht denn hier?");
		}
		String typName = parentTyp.getName();
		TypeComplex pTyp = parentTyp;
		Attribute<?> id = pTyp.getSubAttribute(idName);
		while(id == null){// suche nach oben ...
			String parentTypName = Symbols.getParentName(typName);
			if(parentTypName == null){
				break;
			}
			typName = parentTypName;
			/*
			String[] names = TypeComplex.splitRight(typName);
			if(names[0] == null){
				break;
			}
			typName = names[0];
			*/
			pTyp = TypeComplex.getInstance(typName);
			if(pTyp == null){
				break;
			}
			id = pTyp.getSubAttribute(idName);
		}
		if(id == null){
			throw new UnknownAttribute(idName + "@" + typName);
		}
		return id;
	}

	public static Class<?> getRawTypeClass(String idName, TypeComplex parentTyp) throws UnknownAttribute {
		Attribute<?> id = getAttribute(idName, parentTyp);
		Type<?> t = id.getType();
		return t.getRawTypeClass();
	}

	public CodeNode persist(TypeComplex parent) {
		String name = this.getLabel().get();
		String type = this.getType().getName();
		
		CodeNode cn = new CodeNode(parent, Symbols.comAttribute(), new String[] {name, type}, new LinkedList<>());
		
		return cn;
	}

}
