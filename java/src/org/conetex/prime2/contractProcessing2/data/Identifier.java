package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.values.Label;

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

}
