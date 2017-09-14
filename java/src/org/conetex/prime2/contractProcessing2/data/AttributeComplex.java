package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class AttributeComplex extends Attribute<Structure>{//Attribute<Value<?>[]>

	public static AttributeComplex create(Label theLabel, Complex theType) {
		if (theLabel != null && theType != null) {
			return new AttributeComplex(theLabel, theType);
		}
		return null;
	}

	private final Label label;

	// private final ValueFactory<T> factory;

	private final Complex type;

	/*
	 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
	 * this.label = theLabel; this.factory = theFactory; }
	 */
	private AttributeComplex(Label theLabel, Complex theType) {
		this.label = theLabel;
		// this.factory = theFactory;
		this.type = theType;
	}

	@Override
	public AbstractType<Structure> getType(){
		return this.type;
	}
	
	@Override
	public Structure createValue(Structure parent) {
		return this.type.createValue(parent);
	}

	@Override
	public Label getLabel() {
		return this.label;
	}



	/*
	public Value<T> createValue(String value){
		Value<T> v = this.createValue();
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
	
	public Value<T> createValue(T theValues){
		//new PrimitiveDataType< Complex  , State >  ( Complex.class  , new ValueFactory<State>()   { public Complex   createValueImp() { return new Complex()  ; } } )
			Value<T> v = this.createValue();
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
	*/
	
}
