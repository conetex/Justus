package com.conetex.contract.build.exceptionType;

public class TypException extends AbstractTypException {
	private static final long serialVersionUID = 1L;

	public TypException(String msg, Exception cause) {
		super(msg, cause);
	}

	public TypException(String msg) {
		super(msg);
	}
}
