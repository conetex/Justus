package com.conetex.contract.lang.type;

import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.UnknownAttribute;
import com.conetex.contract.build.exceptionFunction.UnknownType;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.SizedASCII;
import com.conetex.contract.lang.value.implementation.Structure;

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

	public abstract Type<T> getType();

	private static Attribute<?> getAttribute(String idName, TypeComplex parentTyp) throws UnknownAttribute {
		String typName = parentTyp.getName();
		TypeComplex pTyp = parentTyp;
		Attribute<?> id = pTyp.getSubAttribute(idName);
		while(id == null){// suche nach oben ...
			String[] names = TypeComplex.splitRight(typName);
			if(names[0] == null){
				break;
			}
			typName = names[0];
			pTyp = TypeComplex.getInstance(typName);
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

	public static Class<?> getRawTypeClass(String idName, TypeComplex parentTyp) throws UnknownType, UnknownAttribute {
		Attribute<?> id = getAttribute(idName, parentTyp);
		Type<?> t = id.getType();
		return t.getRawTypeClass();
	}

}
