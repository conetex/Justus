package com.conetex.contract.build.exceptionType;

public abstract class AbstractTypException extends Exception {

	private static final long serialVersionUID = 1L;

	public AbstractTypException(String msg, Exception cause) {
		super(msg, cause);
	}

	public AbstractTypException(String msg) {
		super(msg);
	}

}
