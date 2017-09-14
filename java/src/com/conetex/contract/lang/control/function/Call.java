package com.conetex.contract.lang.control.function;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleValue;

public class Call<V> extends Accessible<V> { // V extends Value<?>

    public static <SV> Call<SV> create(Accessible<SV> theFunction, AccessibleValue<Structure> theReference) {
        // TODO drop this
        if (theFunction == null) {
            System.err.println("theFunction is null");
            return null;
        }
        if (theReference == null) {
            System.err.println("theReference is null");
            return null;
        }
        return new Call<SV>(theFunction, theReference);
    }

    private Accessible<V> function;

    private AccessibleValue<Structure> reference;

    @Override
    public String toString() {
        return "call function " + this.function;
    }

    private Call(Accessible<V> theExpression, AccessibleValue<Structure> theReference) {
        this.function = theExpression;
        this.reference = theReference;
    }

    @Override
    public V getFrom(Structure thisObject) {

        Structure obj = this.reference.getFrom(thisObject);
        if (obj == null) {
            System.err.println("Call getFrom ERROR");
            return null;
        }
        return this.function.getFrom(obj);

        // return this.function.getFrom(thisObject);
    }

    @Override
    public V copyFrom(Structure thisObject) throws Invalid {
        // TODO copyFrom is obsolet
        return null;
    }

    @Override
    public Class<V> getBaseType() {
        return this.function.getBaseType();
    }

}
