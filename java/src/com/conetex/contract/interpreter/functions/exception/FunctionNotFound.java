package com.conetex.contract.interpreter.functions.exception;

public class FunctionNotFound extends Exception {

    private static final long serialVersionUID = 1L;

    public FunctionNotFound(String msg, Exception cause) {
        super(msg, cause);
    }

    public FunctionNotFound(String msg) {
        super(msg);
    }
    
}
