package com.conetex.contract.build.exceptionFunction;

public class FunctionNotFound extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public FunctionNotFound(String msg, Exception cause) {
		super(msg, cause);
	}

	public FunctionNotFound(String msg) {
		super(msg);
	}

}
