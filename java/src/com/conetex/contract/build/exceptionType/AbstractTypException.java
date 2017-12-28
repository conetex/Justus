package com.conetex.contract.build.exceptionType;

public abstract class AbstractTypException extends Exception {

	private static final long serialVersionUID = 1L;

	AbstractTypException(String msg, Exception cause) {
		super(msg, cause);
	}

	AbstractTypException(String msg) {
		super(msg);
	}

}
