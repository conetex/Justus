package com.conetex.contract.interpreter.functions.nesting;

import java.util.HashSet;
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

public abstract class _Abstract<T> {

    private String name;
    
    private Set<String> meaning = new HashSet<>();

    protected _Abstract(String theName) {
        this.name = theName;
    }

    public final String getName() {
        return name;
    }

    final Accessible<? extends T> createThis(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
        if (! this.meaning.contains(n.getTag())) {
            System.err.println("Operation " + n.getTag() + " not found!");
            return null;
        }
        return this.create(n, parentTyp);
    }
    
    public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException;

    final Set<String> keySet() {
    	return this.meaning;
    }    

    public final void means(String theOperationName) {
        if (this.meaning.contains(theOperationName)) {
            System.err.println("duplicate operation '" + theOperationName + "' in " + this.getName());
        }
        this.meaning.add(theOperationName);
    }

    public final void means(String[] theOperationNames) {
        for (String theOperationName : theOperationNames) {
            this.means(theOperationName);
        }
    }

}
