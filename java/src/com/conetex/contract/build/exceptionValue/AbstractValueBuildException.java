package com.conetex.contract.build.exceptionValue;

abstract class AbstractValueBuildException extends Exception {

	private static final long serialVersionUID = 1L;

	public AbstractValueBuildException(String msg, Exception cause) {
		super(msg, cause);
	}

	public AbstractValueBuildException(String msg) {
		super(msg);
	}

}
