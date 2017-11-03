package com.conetex.contract.build.exceptionFunction;

public class DuplicateIdentifierNameExeption extends AbstractInterpreterException{

	private static final long serialVersionUID = 1L;

	public DuplicateIdentifierNameExeption(String msg, Exception cause) {
		super(msg, cause);
	}

	public DuplicateIdentifierNameExeption(String msg) {
		super(msg);
	}

}
