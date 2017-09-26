package com.conetex.contract.interpreter.exception;

public class NoAccessToValue extends OperationInterpreterException {

	private static final long serialVersionUID = 1L;

	public NoAccessToValue(String msg, Exception cause) {
		super(msg, cause);
	}

	public NoAccessToValue(String msg) {
		super(msg);
	}

}
