package com.conetex.contract.interpreter.functions.exception;

public class UnknownType extends OperationInterpreterException {

    private static final long serialVersionUID = 1L;

    public UnknownType(String msg, Exception cause) {
        super(msg, cause);
    }

    public UnknownType(String msg) {
        super(msg);
    }
    
}
