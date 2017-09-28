package com.conetex.contract.build.exceptionLang;

public class UnknownType extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownType(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownType(String msg) {
		super(msg);
	}

}
