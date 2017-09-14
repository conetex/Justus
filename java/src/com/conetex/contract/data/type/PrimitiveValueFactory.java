package com.conetex.contract.data.type;

import com.conetex.contract.data.Value;

public interface PrimitiveValueFactory<T> {

    public Value<T> createValueImp();

}
