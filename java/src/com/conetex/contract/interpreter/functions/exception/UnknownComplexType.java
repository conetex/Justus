package com.conetex.contract.interpreter.functions.exception;

public class UnknownComplexType extends OperationInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownComplexType(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownComplexType(String msg) {
		super(msg);
	}

}
