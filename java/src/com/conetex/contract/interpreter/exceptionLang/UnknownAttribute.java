package com.conetex.contract.interpreter.exceptionLang;

public class UnknownAttribute extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownAttribute(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownAttribute(String msg) {
		super(msg);
	}

}
