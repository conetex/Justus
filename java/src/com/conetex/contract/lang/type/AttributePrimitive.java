package com.conetex.contract.lang.type;

import java.util.LinkedList;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Label;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public class AttributePrimitive<T> extends Attribute<T>{

	public static <V> AttributePrimitive<V> create(Label theLabel, TypePrimitive<V> theType) {
		if(theLabel != null && theType != null){
			return new AttributePrimitive<>(theLabel, theType);
		}
		return null;
	}

	private final Label label;

	// private final ValueFactory<T> factory;

	private final TypePrimitive<T> type;

	/*
	 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){ this.label =
	 * theLabel; this.factory = theFactory; }
	 */
	private AttributePrimitive(Label theLabel, TypePrimitive<T> theType) {
		this.label = theLabel;
		// this.factory = theFactory;
		this.type = theType;
	}

	@Override
	public Type<T> getType() {
		return this.type;
	}

	@Override
	public Value<T> createValue(Structure theParent, CodeNode theNode) {
		return this.type.createValue(theNode);
	}

	public Value<T> createNewValue(Structure theParent) {
		String name = this.label.get();
		String typeN = this.type.getName();
		String[] p = new String[] { name, null, typeN };
		CodeNode cn = new CodeNode(theParent.getComplex(), Symbols.comValue(), p, new LinkedList<>());// TODO so korrekt?
		return this.type.createValue(cn);
	}
	
	@Override
	public Label getLabel() {
		return this.label;
	}

	public Value<T> createValue(CodeNode theNode, String value, Structure theParent) {
		Value<T> v = this.createValue(theParent, theNode);
		try{
			v.setConverted(value);
		}
		catch(Inconvertible | Invalid e){
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			// e.printStackTrace();
			return null;
		}
		return v;
	}

}
