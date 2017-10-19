package com.conetex.contract.build.exceptionLang;

public class NullIdentifierException extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public NullIdentifierException(String msg, Exception cause) {
		super(msg, cause);
	}

	public NullIdentifierException(String msg) {
		super(msg);
	}

}
