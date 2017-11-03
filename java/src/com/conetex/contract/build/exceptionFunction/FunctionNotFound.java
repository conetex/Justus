package com.conetex.contract.build.exceptionLang;

public class FunctionNotFound extends AbstractInterpreterException{

	private static final long serialVersionUID = 1L;

	public FunctionNotFound(String msg, Exception cause) {
		super(msg, cause);
	}

	public FunctionNotFound(String msg) {
		super(msg);
	}

}
