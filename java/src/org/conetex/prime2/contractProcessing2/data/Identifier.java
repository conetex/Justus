package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class Identifier<T> {

	public static <V> Identifier<V> create(Label theLabel, AbstractType<V> theType) {
		if (theLabel != null && theType != null) {
			return new Identifier<V>(theLabel, theType);
		}
		return null;
	}

	private final Label label;

	// private final ValueFactory<T> factory;

	private final AbstractType<T> type;

	/*
	 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
	 * this.label = theLabel; this.factory = theFactory; }
	 */
	private Identifier(Label theLabel, AbstractType<T> theType) {
		this.label = theLabel;
		// this.factory = theFactory;
		this.type = theType;
	}

	public AbstractType<T> getType(){
		return this.type;
	}
	
	public Value<T> createValue() {
		return this.type.createValue();
	}

	public Label getLabel() {
		return this.label;
	}

	public static class DuplicateIdentifierNameExeption extends Exception {
		public DuplicateIdentifierNameExeption(String msg) {
			super(msg);
		}

		private static final long serialVersionUID = 1L;

	}

	public static class NullIdentifierException extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class NullLabelException extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class EmptyLabelException extends Exception {
		private static final long serialVersionUID = 1L;

	}

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
	
	
}
