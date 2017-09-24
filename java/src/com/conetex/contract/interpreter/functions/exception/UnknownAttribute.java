package com.conetex.contract.interpreter.functions.exception;

public class UnknownAttribute extends OperationInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownAttribute(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownAttribute(String msg) {
		super(msg);
	}

}
