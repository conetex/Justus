package com.conetex.contract.interpreter.exceptionLang;

public class TypeNotDeterminated extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public TypeNotDeterminated(String msg, Exception cause) {
		super(msg, cause);
	}

	public TypeNotDeterminated(String msg) {
		super(msg);
	}

}
