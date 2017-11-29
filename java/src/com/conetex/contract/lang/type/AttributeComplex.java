package com.conetex.contract.lang.type;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.value.implementation.Label;
import com.conetex.contract.lang.value.implementation.Structure;

public class AttributeComplex extends Attribute<Structure>{// Attribute<Value<?>[]>

	public static AttributeComplex create(Label theLabel, TypeComplex theType) {
		if(theLabel != null && theType != null){
			return new AttributeComplex(theLabel, theType);
		}
		return null;
	}

	private final Label label;

	// private final ValueFactory<T> factory;

	private final TypeComplex type;

	/*
	 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){ this.label =
	 * theLabel; this.factory = theFactory; }
	 */
	private AttributeComplex(Label theLabel, TypeComplex theType) {
		this.label = theLabel;
		// this.factory = theFactory;
		this.type = theType;
	}

	@Override
	public Type<Structure> getType() {
		return this.type;
	}

	@Override
	public Structure createValue(Structure parent, CodeNode theNode) {
		return this.type.createValue(parent);
	}

	public Structure createNewValue(Structure parent) {
		// TODO implement
		return null;
		//return this.type.createValue(parent);
	}
	
	@Override
	public Label getLabel() {
		return this.label;
	}

	/*
	 * public Value<T> createValue(String value){ Value<T> v = this.createValue();
	 * try { v.setConverted(value); } catch (Inconvertible | Invalid e) { // TODO
	 * Auto-generated catch block System.err.println(e.getMessage());
	 * //e.printStackTrace(); return null; } return v; }
	 * 
	 * public Value<T> createValue(T theValues){ //new PrimitiveDataType< Complex ,
	 * State > ( Complex.class , new ValueFactory<State>() { public Complex
	 * createValueImp() { return new Complex() ; } } ) Value<T> v =
	 * this.createValue(); //Structure value = ct.construct(theValues); try {
	 * v.set(theValues); } catch (Invalid e) { // TODO Auto-generated catch block
	 * e.printStackTrace(); return null; } return v; }
	 */

}
