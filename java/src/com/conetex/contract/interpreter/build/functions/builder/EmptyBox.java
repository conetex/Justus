package com.conetex.contract.interpreter.build.functions.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.SyntaxNode;
import com.conetex.contract.lang.Accessible;

public abstract class EmptyBox<T> extends Abstract<T, Object> {

    private Map<String, EmptyBox<T>> builder = new HashMap<>();

    public final void means(String theOperationName, EmptyBox<T> b) {
        this.builder.put(theOperationName, b);
    }

    public final void means(String theOperationName) {
        this.builder.put(theOperationName, this);
    }

    final Accessible<? extends T> createThis(SyntaxNode n, Complex parentTyp) {
        String name = n.getTag();
        EmptyBox<T> s = this.builder.get(name);
        if (s == null) {
            System.err.println("Operation " + name + " not found!");
            return null;
        }
        return s.create(n, parentTyp);
    }

    final Set<String> keySet(){
    	return this.builder.keySet();
    }
    
    public abstract Accessible<? extends T> create(SyntaxNode n, Complex parentTyp);

}
