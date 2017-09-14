package com.conetex.contract.lang;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public class AccessibleValue<T> extends Accessible<T> {

    public static <T> AccessibleValue<T> create(String thePath, Class<T> theClass) {
        if (thePath == null) {
            return null;
        }
        return new AccessibleValue<>(thePath, theClass);
    }

    public static <T extends Number> AccessibleValue<T> createNum(String thePath, Class<T> theClass) {
        if (thePath == null) {
            System.err.println("thePath is null");
            return null;
        }
        return new AccessibleValue<>(thePath, theClass);
    }

    private String path;

    private final Class<T> clazz;

    private AccessibleValue(String thePath, Class<T> theClass) {
        this.path = thePath;
        this.clazz = theClass;
    }

    @Override
    public T getFrom(Structure thisObject) {

        // return thisObject.getValueNewNew(this.path, this.clazz);

        Value<T> value = thisObject.getValueNew(this.path, this.clazz);
        if (value == null) {
            return null;
        }
        return value.get();
    }

    /*
     * @Override public void setTo(Structure thisObject, T newValue) throws Invalid
     * { Value<T> value = thisObject.getValueNew(this.path, this.clazz);
     * value.set(newValue); }
     * 
     * public void transSet(Structure thisObject, String newValue) throws
     * Inconvertible, Invalid { Value<T> value = thisObject.getValueNew(this.path,
     * this.clazz); value.setConverted(newValue); }
     */

    @Override
    public T copyFrom(Structure thisObject) throws Invalid {
        Value<T> value = thisObject.getValueNew(this.path, this.clazz);
        if (value == null) {
            return null;
        }
        return value.copy();
    }

    @Override
    public Class<T> getBaseType() {
        return this.clazz;
    }

}
