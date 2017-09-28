package com.conetex.contract.interpreter.exceptionLang;

public class UnknownComplexType extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownComplexType(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownComplexType(String msg) {
		super(msg);
	}

}
