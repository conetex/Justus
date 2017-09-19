package com.conetex.contract.interpreter.functions.exception;

public class NoAccessToValue extends Exception {

    private static final long serialVersionUID = 1L;

    public NoAccessToValue(String msg, Exception cause) {
        super(msg, cause);
    }

    public NoAccessToValue(String msg) {
        super(msg);
    }
    
}