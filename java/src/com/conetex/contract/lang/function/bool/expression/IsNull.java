package com.conetex.contract.lang.function.bool.expression;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.AccessibleWithChildren;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class IsNull extends AccessibleWithChildren<Boolean> {

	public static IsNull create(Accessible<?> theSub) {
		if (theSub == null) {
			return null;
		}
		return new IsNull(theSub);
	}

	private final Accessible<?> sub;

	private IsNull(Accessible<?> theSub) {
		super(new Accessible<?>[] { theSub });
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

	@Override
	public String getCommand() {
		return Symbols.comIsNull();
	}

}