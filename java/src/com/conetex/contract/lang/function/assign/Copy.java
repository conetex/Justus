package com.conetex.contract.lang.function.assign;

import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.Setable;

public class Copy<T> extends AbstractAssigment<T>{

	Copy(Setable<T> trg, Accessible<T> src) {
		super(trg, src);
	}

	@Override
	public boolean doCopy() {
		return true;
	}

}
