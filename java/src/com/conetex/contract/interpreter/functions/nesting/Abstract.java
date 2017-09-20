package com.conetex.contract.interpreter.functions.nesting;

import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.exception.FunctionNotFound;
import com.conetex.contract.interpreter.functions.exception.MissingSubOperation;
import com.conetex.contract.interpreter.functions.exception.NoAccessToValue;
import com.conetex.contract.interpreter.functions.exception.OperationInterpreterException;
import com.conetex.contract.interpreter.functions.exception.TypeNotDeterminated;
import com.conetex.contract.interpreter.functions.exception.TypesDoNotMatch;
import com.conetex.contract.interpreter.functions.exception.UnexpectedSubOperation;
import com.conetex.contract.interpreter.functions.exception.UnknownComplexType;
import com.conetex.contract.interpreter.functions.exception.UnknownType;
import com.conetex.contract.lang.Accessible;

public abstract class Abstract<T, S> {

    private String name;

    protected Abstract(String theName) {
        this.name = theName;
    }

    public final String getName() {
        return name;
    }

    abstract Accessible<? extends T> createThis(CodeNode n, Complex parentTyp) throws OperationInterpreterException;

    public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException;

    abstract Set<String> keySet();

    public abstract void means(String theOperationName);

    public final void means(String[] theOperationNames) {
        for (String theOperationName : theOperationNames) {
            this.means(theOperationName);
        }
    }

}
