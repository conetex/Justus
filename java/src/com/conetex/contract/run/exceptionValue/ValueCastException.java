package com.conetex.contract.run.exceptionValue;

public class ValueCastException extends AbstractRuntimeException {
	private static final long serialVersionUID = 1L;

	public ValueCastException(String msg, Exception cause) {
		super(msg, cause);
	}

	public ValueCastException(String msg) {
		super(msg);
	}
}
