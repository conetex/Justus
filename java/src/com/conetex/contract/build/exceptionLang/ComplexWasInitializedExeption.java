package com.conetex.contract.build.exceptionLang;

public class ComplexWasInitializedExeption extends AbstractInterpreterException{

	private static final long serialVersionUID = 1L;

	public ComplexWasInitializedExeption(String msg, Exception cause) {
		super(msg, cause);
	}

	public ComplexWasInitializedExeption(String msg) {
		super(msg);
	}

}
