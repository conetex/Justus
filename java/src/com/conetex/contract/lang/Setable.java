package com.conetex.contract.lang;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public interface Setable<T> {

    public T setTo(Structure thisObject, T value) throws Invalid;

}
