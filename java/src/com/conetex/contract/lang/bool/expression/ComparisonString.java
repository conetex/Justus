package com.conetex.contract.lang.bool.expression;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.Symbol;

public class ComparisonString extends Accessible<Boolean> {

    public static final int SMALLER = -1;
    public static final int EQUAL   = 0;
    public static final int GREATER = 1;

    public static Accessible<Boolean> _createNew(Accessible<String> theA, Accessible<String> theB, String name) {
        return create(theA, theB, name);
    }

    public static ComparisonString create(Accessible<String> theA, Accessible<String> theB, String operation) {
        if (theA == null || theB == null) {
            return null;
        }
        if (operation.equals(Symbol.SMALLER)) {
            return create(theA, theB, ComparisonString.SMALLER);
        }
        if (operation.equals(Symbol.EQUAL)) {
            return create(theA, theB, ComparisonString.EQUAL);
        }
        if (operation.equals(Symbol.GREATER)) {
            return create(theA, theB, ComparisonString.GREATER);
        }
        return null;
    }

    public static ComparisonString create(Accessible<String> theA, Accessible<String> theB, int operation) {
        if (theA == null || theB == null) {
            return null;
        }
        if (operation < ComparisonString.SMALLER || operation > ComparisonString.GREATER) {
            return null;
        }
        return new ComparisonString(theA, theB, operation);
    }

    private int operator;

    private Accessible<String> a;

    private Accessible<String> b;

    // private Comparison(Accessible<T> theA, Accessible<T> theB, int
    // theOperation){
    private ComparisonString(Accessible<String> theA, Accessible<String> theB, int theOperation) {
        this.a = theA;
        this.b = theB;
        this.operator = theOperation;
    }

    // public Comparison(Accessible<Comparable<?>> theA,
    // Accessible<Comparable<?>>
    // theB, int theOperation) {
    /*
     * public Comparison(Accessible<Comparable<?>> theA, Accessible<Comparable<?>>
     * theB, int theOperation) { super(theA, theB); this.operator = theOperation; }
     */

    @Override
    public Boolean getFrom(Structure thisObject) {
        String aN = this.a.getFrom(thisObject);
        String bN = this.b.getFrom(thisObject);
        if (aN == null) {
            if (bN == null && this.operator == ComparisonString.EQUAL) {
                return Boolean.TRUE;
            }
            return null;
        }
        else if (bN == null) {
            return null;
        }

        return comp(aN, bN);
    }

    private <T extends Comparable<T>> Boolean comp(T a, T b) {
        if (this.operator == ComparisonString.GREATER) {
            if (a.compareTo(b) > 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        if (this.operator == ComparisonString.SMALLER) {
            if (a.compareTo(b) < 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        if (this.operator == ComparisonString.EQUAL) {
            if (a.compareTo(b) == 0) {
                return Boolean.TRUE;
            }
            return Boolean.FALSE;
        }
        return null;
    }

    @Override
    public Boolean copyFrom(Structure thisObject) {
        return this.getFrom(thisObject);
    }

    @Override
    public Class<Boolean> getBaseType() {
        return Boolean.class;
    }

}
