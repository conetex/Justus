package com.conetex.contract.lang.function.assign;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.AccessibleValue;

public class Reference<T> extends AbstractAssigment<T>{

	Reference(AccessibleValue<T> trg, Accessible<T> src) {
		super(Symbols.comRefer(), trg, src);
	}

	@Override
	public boolean doCopy() {
		return false;
	}

}
