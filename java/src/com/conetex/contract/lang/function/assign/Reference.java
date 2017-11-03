package com.conetex.contract.lang.assign;

import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.Setable;

public class Reference<T> extends AbstractAssigment<T>{

	Reference(Setable<T> trg, Accessible<T> src) {
		super(trg, src);
	}

	@Override
	public boolean doCopy() {
		return false;
	}

}
