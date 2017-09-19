package com.conetex.contract.interpreter.functions.exception;

public class TypeNotDeterminated extends Exception {

    private static final long serialVersionUID = 1L;

    public TypeNotDeterminated(String msg, Exception cause) {
        super(msg, cause);
    }

    public TypeNotDeterminated(String msg) {
        super(msg);
    }
    
}