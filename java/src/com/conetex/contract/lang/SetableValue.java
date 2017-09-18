package com.conetex.contract.lang;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public class SetableValue<T> extends AccessibleValue<T> implements Setable<T> {

    public static <T> SetableValue<T> create(String thePath, Class<T> theClass) {
        if (thePath == null) {
            return null;
        }
        return new SetableValue<>(thePath, theClass);
    }

    private SetableValue(String thePath, Class<T> theClass) {
        super(thePath, theClass);
    }

    public T setTo(Structure thisObject, T newValue) throws Invalid {
        Value<T> value = thisObject.getValueNew(this.path, this.clazz);
        if (value == null) {
            return null;
        }
        // just 4 debug:
        // T valueOld = value.get();
        return value.set(newValue);
        // just 4 debug:
        // value = thisObject.getValueNew(this.path, this.clazz);
        // System.out.println(valueOld + " setTo " + newValue + " -> " + value.get());
        // return newValue;
    }

    @Override
    public <X> Setable<X> asSetable(Class<X> baseType) {
        if (baseType.isAssignableFrom(this.getBaseType())) {
            return (SetableValue<X>) this;
        }
        return null;
    }

}
