package com.conetex.contract.build.exceptionFunction;

public class NullLabelException extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public NullLabelException(String msg, Exception cause) {
		super(msg, cause);
	}

	public NullLabelException(String msg) {
		super(msg);
	}

}
