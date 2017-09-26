package com.conetex.contract.interpreter.exception;

public class UnknownAttribute extends OperationInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownAttribute(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownAttribute(String msg) {
		super(msg);
	}

}
