package com.conetex.contract.lang.value;

import com.conetex.contract.build.CodeNode;

public interface PrimitiveValueFactory<T> {

	Value<T> createValueImp(CodeNode theNode);

}
