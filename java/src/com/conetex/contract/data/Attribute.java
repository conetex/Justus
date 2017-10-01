package com.conetex.contract.data;

import com.conetex.contract.build.exceptionLang.UnknownAttribute;
import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.value.SizedASCII;
import com.conetex.contract.data.value.Structure;

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

	public static Attribute<?> getID(String idName, Complex parentTyp) throws UnknownAttribute {
		// System.out.println("get_id from " + n.getTag() + " (" + n.getValue()
		// + ")");
		String typName = parentTyp.getName();
		Complex pTyp = parentTyp;
		Attribute<?> id = pTyp.getSubAttribute(idName);
		while (id == null) {// suche nach oben ...
			String[] names = Complex.splitRight(typName);
			if (names[0] == null) {
				break;
			}
			typName = names[0];
			pTyp = Complex.getInstance(typName);
			if (pTyp == null) {
				break;
			}
			id = pTyp.getSubAttribute(idName);
		}
		if (id == null) {
			// System.err.println("ERR: can not find '" + idName + "'");
			throw new UnknownAttribute(idName + "@" + typName);
		}
		return id;
	}

}
