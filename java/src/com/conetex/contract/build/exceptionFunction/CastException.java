package com.conetex.contract.build.exceptionFunction;

public class CastException extends AbstractInterpreterException{

	private static final long serialVersionUID = 1L;

	public CastException(String msg, Exception cause) {
		super(msg, cause);
	}

	public CastException(String msg) {
		super(msg);
	}

}
