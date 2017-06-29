package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.values.ASCII8;

public class Identifier<T> {

	public static <V> Identifier<V> create(ASCII8 theLabel, AbstractType<V> theType) {
		if (theLabel != null && theType != null) {
			return new Identifier<V>(theLabel, theType);
		}
		return null;
	}

	private final ASCII8 label;

	// private final ValueFactory<T> factory;

	private final AbstractType<T> type;

	/*
	 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
	 * this.label = theLabel; this.factory = theFactory; }
	 */
	private Identifier(ASCII8 theLabel, AbstractType<T> theType) {
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

	public ASCII8 getLabel() {
		return this.label;
	}

	public static class DuplicateAttributeNameExeption extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class NullAttributeException extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class NullLabelException extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class EmptyLabelException extends Exception {
		private static final long serialVersionUID = 1L;

	}

}
