package com.conetex.contract.lang.assignment;

import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.Setable;

public class Reference<T> extends AbstractAssigment<T> {

	Reference(Setable<T> trg, Accessible<T> src) {
		super(trg, src);
	}

	@Override
	public boolean doCopy() {
		return false;
	}

}
