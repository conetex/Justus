package com.conetex.contract.lang.function.assign;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.AccessibleValue;

public class Param<T> extends AbstractAssigment<T>{

	Param(AccessibleValue<T> trg, Accessible<T> src) {
		super(Symbols.comParam(), trg, src);
	}

	@Override
	public boolean doCopy() {
		return false;
	}

}
