package com.conetex.contract.interpreter.functions.exception;

public class OperationMeansNotCalled extends OperationInterpreterException {

    private static final long serialVersionUID = 1L;

    public OperationMeansNotCalled(String msg, Exception cause) {
        super(msg, cause);
    }

    public OperationMeansNotCalled(String msg) {
        super(msg);
    }
    
}
