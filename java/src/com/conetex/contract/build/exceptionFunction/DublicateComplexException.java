package com.conetex.contract.build.exceptionFunction;

public class DublicateComplexException extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public DublicateComplexException(String msg, Exception cause) {
		super(msg, cause);
	}

	public DublicateComplexException(String msg) {
		super(msg);
	}

}
