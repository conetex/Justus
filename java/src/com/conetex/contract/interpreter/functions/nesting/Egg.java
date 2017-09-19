package com.conetex.contract.interpreter.functions.nesting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.exception.FunctionNotFound;
import com.conetex.contract.interpreter.functions.exception.NoAccessToValue;
import com.conetex.contract.interpreter.functions.exception.TypeNotDeterminated;
import com.conetex.contract.lang.Accessible;

public abstract class Egg<T> extends Abstract<T, Object> {

    private Map<String, Egg<T>> builder = new HashMap<>();

    public Egg(String name) {
        super(name);
    }

    public final void means(String theOperationName, Egg<T> b) {
        if (this.builder.containsKey(theOperationName)) {
            System.err.println("duplicate operation '" + theOperationName + "' in " + this.getName());
        }
        this.builder.put(theOperationName, b);
    }

    public final void means(String theOperationName) {
        this.means(theOperationName, this);
    }

    final Accessible<? extends T> createThis(CodeNode n, Complex parentTyp) throws FunctionNotFound, NoAccessToValue, TypeNotDeterminated {
        String name = n.getTag();
        Egg<T> s = this.builder.get(name);
        if (s == null) {
            System.err.println("Operation " + name + " not found!");
            return null;
        }
        return s.create(n, parentTyp);
    }

    final Set<String> keySet() {
        return this.builder.keySet();
    }

    public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws FunctionNotFound, NoAccessToValue, TypeNotDeterminated;

}
