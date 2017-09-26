package com.conetex.contract.interpreter.exception;

public class TypeNotDeterminated extends OperationInterpreterException {

	private static final long serialVersionUID = 1L;

	public TypeNotDeterminated(String msg, Exception cause) {
		super(msg, cause);
	}

	public TypeNotDeterminated(String msg) {
		super(msg);
	}

}
