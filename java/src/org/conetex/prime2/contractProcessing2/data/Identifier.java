package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.SizedASCII;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public abstract class Identifier<T> {

	public static class NullLabelException extends Exception {
		private static final long serialVersionUID = 1L;

	}
	
	public static class EmptyLabelException extends Exception {
		private static final long serialVersionUID = 1L;

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

	public abstract SizedASCII getLabel();

	public abstract Value<T> createValue(Structure theParent);

	public abstract AbstractType<T> getType();


	
}
