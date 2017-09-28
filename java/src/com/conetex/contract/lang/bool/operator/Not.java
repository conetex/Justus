package com.conetex.contract.lang.bool.operator;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.runtime.exceptionValue.ValueCastException;

//Unary operation
public class Not extends Accessible<Boolean> {

	public static Not create(Accessible<? extends Boolean> theSub) {
		if (theSub == null) {
			return null;
		}
		return new Not(theSub);
	}

	private Accessible<? extends Boolean> sub;

	private Not(Accessible<? extends Boolean> theSub) {
		this.sub = theSub;
	}

	@Override
	public Boolean getFrom(Structure thisObject) throws ValueCastException {
		Boolean b = this.sub.getFrom(thisObject);
		if (b == null) {
			return null;
		}
		if (b.booleanValue()) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) throws ValueCastException {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

}