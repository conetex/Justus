package com.conetex.contract.lang;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public interface Setable<T> {

    public Class<T> getBaseType();

    public T setTo(Structure thisObject, T value) throws Invalid;

    public <X> Setable<X> asSetable(Class<X> baseType);

}
