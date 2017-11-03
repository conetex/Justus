package com.conetex.contract.build.exceptionFunction;

public class NoAccessToValue extends AbstractInterpreterException{

	private static final long serialVersionUID = 1L;

	public NoAccessToValue(String msg, Exception cause) {
		super(msg, cause);
	}

	public NoAccessToValue(String msg) {
		super(msg);
	}

}
