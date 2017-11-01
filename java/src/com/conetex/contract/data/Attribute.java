package com.conetex.contract.data;

import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.build.exceptionLang.UnknownAttribute;
import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.value.SizedASCII;
import com.conetex.contract.data.value.Structure;

public abstract class Attribute<T> {

	public static class _NullLabelException extends AbstractInterpreterException{
		public _NullLabelException(String msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		private static final long serialVersionUID = 1L;

	}

	public static class _EmptyLabelException extends AbstractInterpreterException{
		public _EmptyLabelException(String msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		private static final long serialVersionUID = 1L;

	}

	public static class _DuplicateIdentifierNameExeption extends AbstractInterpreterException{
		public _DuplicateIdentifierNameExeption(String msg) {
			super(msg);
		}

		private static final long serialVersionUID = 1L;

	}

	public static class _NullIdentifierException extends AbstractInterpreterException{
		public _NullIdentifierException(String msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		private static final long serialVersionUID = 1L;

	}

	public abstract SizedASCII getLabel();

	public abstract Value<T> createValue(Structure theParent);

	public abstract AbstractType<T> getType();

	public static Attribute<?> getID(String idName, Complex parentTyp) throws UnknownAttribute {
		String typName = parentTyp.getName();
		Complex pTyp = parentTyp;
		Attribute<?> id = pTyp.getSubAttribute(idName);
		while(id == null){// suche nach oben ...
			String[] names = Complex.splitRight(typName);
			if(names[0] == null){
				break;
			}
			typName = names[0];
			pTyp = Complex.getInstance(typName);
			if(pTyp == null){
				break;
			}
			id = pTyp.getSubAttribute(idName);
		}
		if(id == null){
			throw new UnknownAttribute(idName + "@" + typName);
		}
		return id;
	}

}
