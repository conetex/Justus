package com.conetex.contract.build.exceptionLang;

public class UnknownCommandParameter extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownCommandParameter(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownCommandParameter(String msg) {
		super(msg);
	}

}
