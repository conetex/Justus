package com.conetex.contract.build.exceptionFunction;

public class AbstractInterpreterException extends Exception{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	AbstractInterpreterException(String msg, Exception cause) {
		super(msg, cause);
	}

	protected AbstractInterpreterException(String msg) {
		super(msg);
	}

}
