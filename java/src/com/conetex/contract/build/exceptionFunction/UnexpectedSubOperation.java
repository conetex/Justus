package com.conetex.contract.build.exceptionFunction;

public class UnexpectedSubOperation extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnexpectedSubOperation(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnexpectedSubOperation(String msg) {
		super(msg);
	}

}
