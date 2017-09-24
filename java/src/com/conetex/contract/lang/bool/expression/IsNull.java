package com.conetex.contract.lang.bool.expression;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.Accessible;

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
	public Boolean getFrom(Structure thisObject) {
		Object b = this.sub.getFrom(thisObject);
		if (b == null) {
			return Boolean.TRUE;
		}
		return Boolean.FALSE;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}

}