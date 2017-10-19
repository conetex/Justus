package com.conetex.contract.build.exceptionLang;

public class EmptyLabelException extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public EmptyLabelException(String msg, Exception cause) {
		super(msg, cause);
	}

	public EmptyLabelException(String msg) {
		super(msg);
	}

}
