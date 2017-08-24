package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class IdentifierPrimitive<T> extends Identifier<T>{

	public static <V> IdentifierPrimitive<V> create(Label theLabel, Primitive<V> theType) {
		if (theLabel != null && theType != null) {
			return new IdentifierPrimitive<V>(theLabel, theType);
		}
		return null;
	}

	private final Label label;

	// private final ValueFactory<T> factory;

	private final Primitive<T> type;

	/*
	 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
	 * this.label = theLabel; this.factory = theFactory; }
	 */
	private IdentifierPrimitive(Label theLabel, Primitive<T> theType) {
		this.label = theLabel;
		// this.factory = theFactory;
		this.type = theType;
	}

	@Override
	public AbstractType<T> getType(){
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


	public Value<T> createValue(String value, Structure theParent){
		Value<T> v = this.createValue(theParent);
		try {
			v.setConverted(value);
		} catch (Inconvertible | Invalid e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			//e.printStackTrace();
			return null;
		}
		return v;
	}






	public Value<T> _createValue(String value){
		Value<T> v = this._createValue();
		try {
			v.setConverted(value);
		} catch (Inconvertible | Invalid e) {
			// TODO Auto-generated catch block
			System.err.println(e.getMessage());
			//e.printStackTrace();
			return null;
		}
		return v;
	}	
	
	public Value<T> _createValue(T theValues){
		//new PrimitiveDataType< Complex  , State >  ( Complex.class  , new ValueFactory<State>()   { public Complex   createValueImp() { return new Complex()  ; } } )
			Value<T> v = this._createValue();
			//Structure value = ct.construct(theValues);
			try {
				v.set(theValues);
			} catch (Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return v;
	}

	
	
	
}
