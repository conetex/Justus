package com.conetex.contract.lang.bool.operator;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleAbstract;

//Unary operation
public class Not extends AccessibleAbstract<Boolean> {

	public static Not create(AccessibleAbstract<Boolean> theSub) {
		if (theSub == null) {
			return null;
		}
		return new Not(theSub);
	}

	private AccessibleAbstract<Boolean> sub;

	private Not(AccessibleAbstract<Boolean> theSub) {
		this.sub = theSub;
	}

	@Override
	public Boolean getFrom(Structure thisObject) {
		Boolean b = this.sub.getFrom(thisObject);
		if (b == null) {
			return null;
		}
		if (b) {
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
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