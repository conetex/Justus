package com.conetex.contract.interpreter.functions.exception;

public class MissingSubOperation extends OperationInterpreterException {

	private static final long serialVersionUID = 1L;

	public MissingSubOperation(String msg, Exception cause) {
		super(msg, cause);
	}

	public MissingSubOperation(String msg) {
		super(msg);
	}

}
