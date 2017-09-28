package com.conetex.contract.runtime.exceptionValue;

public abstract class AbstractValueException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractValueException(String msg, Exception cause) {
		super(msg, cause);
	}

	public AbstractValueException(String msg) {
		super(msg);
	}
}
