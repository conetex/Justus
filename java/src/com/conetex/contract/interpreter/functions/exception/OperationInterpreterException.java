package com.conetex.contract.interpreter.functions.exception;

public class OperationInterpreterException extends Exception {

	public OperationInterpreterException(String msg, Exception cause) {
		super(msg, cause);
	}

	public OperationInterpreterException(String msg) {
		super(msg);
	}

}
