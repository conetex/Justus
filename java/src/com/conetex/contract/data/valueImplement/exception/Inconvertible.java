package com.conetex.contract.data.valueImplement.exception;

public class Inconvertible extends Exception {

	private static final long serialVersionUID = 1L;

	public Inconvertible(String msg, Exception cause) {
		super(msg, cause);
	}

	public Inconvertible(String msg) {
		super(msg);
	}
}
