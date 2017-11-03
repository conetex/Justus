package com.conetex.contract.build.exceptionFunction;

public class DublicateOperation extends AbstractInterpreterException{

	private static final long serialVersionUID = 1L;

	public DublicateOperation(String msg, Exception cause) {
		super(msg, cause);
	}

	public DublicateOperation(String msg) {
		super(msg);
	}

}
