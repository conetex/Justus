package com.conetex.contract.interpreter.functions.exception;

public class OperationInterpreterException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OperationInterpreterException(String msg, Exception cause) {
		super(msg, cause);
	}

	public OperationInterpreterException(String msg) {
		super(msg);
	}

}
