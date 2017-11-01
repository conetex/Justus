package com.conetex.contract.build.exceptionLang;

public class PrototypeWasInitialized extends AbstractInterpreterException{

	private static final long serialVersionUID = 1L;

	public PrototypeWasInitialized(String msg, Exception cause) {
		super(msg, cause);
	}

	public PrototypeWasInitialized(String msg) {
		super(msg);
	}

}
