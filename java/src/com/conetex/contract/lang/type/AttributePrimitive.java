package com.conetex.contract.lang.type;

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
	public Value<T> createValue(Structure theParent) {
		return this.type.createValue();
	}

	public Value<T> _createValue() {
		return this.type.createValue();
	}

	@Override
	public Label getLabel() {
		return this.label;
	}

	public Value<T> createValue(String value, Structure theParent) {
		Value<T> v = this.createValue(theParent);
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

	public Value<T> _createValue(String value) {
		Value<T> v = this._createValue();
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

	public Value<T> _createValue(T theValues) {
		// new PrimitiveDataType< Complex , State > ( Complex.class , new
		// ValueFactory<State>() { public Complex createValueImp() { return new
		// Complex() ; } } )
		Value<T> v = this._createValue();
		// Structure value = ct.construct(theValues);
		try{
			v.set(theValues);
		}
		catch(Invalid e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return v;
	}

}
