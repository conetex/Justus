package com.conetex.contract.runtime.exceptionValue;

public class Inconvertible extends AbstractValueException {

	private static final long serialVersionUID = 1L;

	public Inconvertible(String msg, Exception cause) {
		super(msg, cause);
	}

	public Inconvertible(String msg) {
		super(msg);
	}
}
