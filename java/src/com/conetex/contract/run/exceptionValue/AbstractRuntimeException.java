package com.conetex.contract.run.exceptionValue;

public abstract class AbstractRuntimeException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public AbstractRuntimeException(String msg, Exception cause) {
		super(msg, cause);
	}

	public AbstractRuntimeException(String msg) {
		super(msg);
	}
}
