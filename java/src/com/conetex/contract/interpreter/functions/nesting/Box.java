package com.conetex.contract.interpreter.functions.nesting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.exception.FunctionNotFound;
import com.conetex.contract.interpreter.functions.exception.MissingSubOperation;
import com.conetex.contract.interpreter.functions.exception.NoAccessToValue;
import com.conetex.contract.interpreter.functions.exception.TypesDoNotMatch;
import com.conetex.contract.interpreter.functions.exception.UnexpectedSubOperation;
import com.conetex.contract.interpreter.functions.exception.UnknownComplexType;
import com.conetex.contract.lang.Accessible;

public abstract class Box<T, S> extends Abstract<T, S> {

    private Map<String, Abstract<? extends S, ?>> childBuilder = new HashMap<>();

    public Box(String name) {
        super(name);
    }

    public final void contains(String theOperationName, Abstract<? extends S, ?> b) {
        if (this.childBuilder.containsKey(theOperationName)) {
            System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
        }
        this.childBuilder.put(theOperationName, b);
    }

    public final void contains(Abstract<? extends S, ?> b) {
        for (String s : b.keySet()) {
            this.contains(s, b);
        }
    }

    final Set<String> keySet() {
        return this.builder.keySet();
    }

    public final Accessible<? extends S> createChild(CodeNode n, Complex parentTyp) throws UnexpectedSubOperation, FunctionNotFound, NoAccessToValue, UnknownComplexType, TypesDoNotMatch, MissingSubOperation {
        String name = n.getTag();
        Abstract<? extends S, ?> s = this.childBuilder.get(name);
        if (s == null) {
            System.err.println("inner Operation '" + name + "' not found in " + this.getName());
            return null;
        }
        System.out.println("createChild " + name + " " + n.getName());
        return s.createThis(n, parentTyp);
    }

    private Map<String, Abstract<T, ?>> builder = new HashMap<>();

    public final void means(String theOperationName, Abstract<T, ?> b) {
        if (this.builder.containsKey(theOperationName)) {
            System.err.println("duplicate operation '" + theOperationName + "' in " + this.getName());
        }
        this.builder.put(theOperationName, b);
    }

    public final void means(String theOperationName) {
        this.means(theOperationName, this);
    }

    final Accessible<? extends T> createThis(CodeNode n, Complex parentTyp) throws UnexpectedSubOperation, FunctionNotFound, NoAccessToValue, UnknownComplexType, TypesDoNotMatch, MissingSubOperation {
        String name = n.getTag();
        Abstract<T, ?> s = this.builder.get(name);
        if (s == null) {
            System.err.println("Operation " + name + " not found!");
            return null;
        }
        return s.create(n, parentTyp);
    }

    public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws UnexpectedSubOperation, FunctionNotFound, NoAccessToValue, UnknownComplexType, TypesDoNotMatch, MissingSubOperation;

    /*
     * private Map<String, Builder<?>> subBuildersObj = new HashMap<String,
     * Builder<?>>();
     * 
     * public void addStructure(String theOperationName, Builder<?> b){
     * this.subBuildersObj.put(theOperationName, b); } public Builder<?>
     * getBuilderStructure(String theOperationName){ return
     * this.subBuildersObj.get(theOperationName); } public Accessible<?>
     * createSuper(SyntaxNode n, Complex parentTyp){ String name = n.getTag();
     * Builder<?> s = this.getBuilderStructure(name); return s.create(n, parentTyp);
     * }
     * 
     * 
     * 
     * private Map<String, Builder<Boolean>> subBuildersBoolean = new
     * HashMap<String, Builder<Boolean>>();
     * 
     * public void addBoolean(String theOperationName, Builder<Boolean> b){
     * this.subBuildersBoolean.put(theOperationName, b); } public Builder<Boolean>
     * getBuilderBoolean(String theOperationName){ return
     * this.subBuildersBoolean.get(theOperationName); } public Accessible<? extends
     * Boolean> createBoolean(SyntaxNode n, Complex parentTyp){ String name =
     * n.getTag(); Builder<Boolean> s = this.getBuilderBoolean(name); return
     * s.create(n, parentTyp); }
     * 
     * 
     * private Map<String, Builder<String>> subBuildersString = new HashMap<String,
     * Builder<String>>();
     * 
     * public void addString(String theOperationName, Builder<String> b){
     * this.subBuildersString.put(theOperationName, b); } public Builder<String>
     * getBuilderString(String theOperationName){ return
     * this.subBuildersString.get(theOperationName); } public Accessible<? extends
     * String> createString(SyntaxNode n, Complex parentTyp){ String name =
     * n.getTag(); Builder<String> s = this.getBuilderString(name); return
     * s.create(n, parentTyp); }
     * 
     * 
     * private Map<String, Builder<Number>> subBuildersNumber = new HashMap<String,
     * Builder<Number>>();
     * 
     * public void addNumber(String theOperationName, Builder<Number> b){
     * this.subBuildersNumber.put(theOperationName, b); } public Builder<Number>
     * getBuilderNumber(String theOperationName){ return
     * this.subBuildersNumber.get(theOperationName); } public Accessible<? extends
     * Number> createNumber(SyntaxNode n, Complex parentTyp){ String name =
     * n.getTag(); Builder<Number> s = this.getBuilderNumber(name); return
     * s.create(n, parentTyp); }
     */

}
