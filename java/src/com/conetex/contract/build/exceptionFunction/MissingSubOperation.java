package com.conetex.contract.build.exceptionFunction;

public class MissingSubOperation extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public MissingSubOperation(String msg, Exception cause) {
		super(msg, cause);
	}

	public MissingSubOperation(String msg) {
		super(msg);
	}

}
