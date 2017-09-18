package com.conetex.contract.interpreter.functions.nesting;

import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.lang.Accessible;

public abstract class Abstract<T, S> {

    private String name;

    protected Abstract(String theName) {
        this.name = theName;
    }

    public final String getName() {
        return name;
    }

    abstract Accessible<? extends T> createThis(CodeNode n, Complex parentTyp);

    public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp);

    abstract Set<String> keySet();

    public abstract void means(String theOperationName);

    public final void means(String[] theOperationNames) {
        for (String theOperationName : theOperationNames) {
            this.means(theOperationName);
        }
    }

}
