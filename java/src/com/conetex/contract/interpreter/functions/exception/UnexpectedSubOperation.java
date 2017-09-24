package com.conetex.contract.interpreter.functions.exception;

public class UnexpectedSubOperation extends OperationInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnexpectedSubOperation(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnexpectedSubOperation(String msg) {
		super(msg);
	}

}
