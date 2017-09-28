package com.conetex.contract.build.exceptionLang;

public class AbstractInterpreterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractInterpreterException(String msg, Exception cause) {
		super(msg, cause);
	}

	public AbstractInterpreterException(String msg) {
		super(msg);
	}

}
