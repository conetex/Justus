package com.conetex.contract.build.exceptionFunction;

public class TypesDoNotMatch extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public TypesDoNotMatch(String type1, String type2, Exception cause) {
		super(type1 + " <> " + type2, cause);
	}

	public TypesDoNotMatch(String msg, Exception cause) {
		super(msg, cause);
	}

	public TypesDoNotMatch(String type1, String type2) {
		super(type1 + " <> " + type2);
	}

	public TypesDoNotMatch(String msg) {
		super(msg);
	}

}
