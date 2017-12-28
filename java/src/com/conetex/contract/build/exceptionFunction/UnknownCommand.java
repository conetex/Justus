package com.conetex.contract.build.exceptionFunction;

public class UnknownCommand extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public UnknownCommand(String msg, Exception cause) {
		super(msg, cause);
	}

	public UnknownCommand(String msg) {
		super(msg);
	}

}
