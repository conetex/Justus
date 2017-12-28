package com.conetex.contract.build.exceptionFunction;

public class OperationMeansNotCalled extends AbstractInterpreterException {

	private static final long serialVersionUID = 1L;

	public OperationMeansNotCalled(String msg, Exception cause) {
		super(msg, cause);
	}

	public OperationMeansNotCalled(String msg) {
		super(msg);
	}

}
