package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.Type.PrimitiveDataType;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.ASCII8;

public class Identifier<T> {

	static <T> Identifier<T> create(ASCII8 theLabel, PrimitiveDataType<T> theType) {
		if (theLabel != null && theType != null) {
			return new Identifier<T>(theLabel, theType);
		}
		return null;
	}

	private final ASCII8 label;

	// private final ValueFactory<T> factory;

	private final PrimitiveDataType<T> type;

	/*
	 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
	 * this.label = theLabel; this.factory = theFactory; }
	 */
	private Identifier(ASCII8 theLabel, PrimitiveDataType<T> theType) {
		this.label = theLabel;
		// this.factory = theFactory;
		this.type = theType;
	}

	public Value.Interface<T> createValue() {
		// return this.factory.createValueImp();
		return this.type.factory.createValueImp();
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
