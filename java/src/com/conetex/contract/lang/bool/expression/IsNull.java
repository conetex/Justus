package com.conetex.contract.lang.bool.expression;

import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class IsNull extends Accessible<Boolean> {

	public static IsNull create(Accessible<?> theSub) {
		if (theSub == null) {
			return null;
		}
		return new IsNull(theSub);
	}

	private Accessible<?> sub;

	private IsNull(Accessible<?> theSub) {
		this.sub = theSub;
	}

	@Override
	public Boolean getFrom(Structure thisObject) throws AbstractRuntimeException {
		Object b = this.sub.getFrom(thisObject);
		if (b == null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) throws AbstractRuntimeException {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

}