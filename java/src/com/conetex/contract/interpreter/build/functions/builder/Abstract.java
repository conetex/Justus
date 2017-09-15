package com.conetex.contract.interpreter.build.functions.builder;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.SyntaxNode;
import com.conetex.contract.lang.Accessible;

public abstract class Abstract<T, S> {

    abstract Accessible<? extends T> createThis(SyntaxNode n, Complex parentTyp);
    
    public abstract Accessible<? extends T> create(SyntaxNode n, Complex parentTyp);

	abstract Set<String> keySet();

}
