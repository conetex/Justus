package com.conetex.contract.data;

import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.valueImplement.SizedASCII;
import com.conetex.contract.data.valueImplement.Structure;

public abstract class Attribute<T> {

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
