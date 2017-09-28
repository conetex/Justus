package com.conetex.contract.data.valueImplement;

import com.conetex.contract.data.Value;
import com.conetex.contract.run.exceptionValue.Inconvertible;

public class Bool implements Value<Boolean> {

	private Boolean actual;

	@Override
	public Boolean set(Boolean aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public final Boolean get() {
		return this.actual;
	}

	public static Boolean getTrans(String value) {
		if (value.equalsIgnoreCase("true")) {
			return Boolean.TRUE;
		}
		else if (value.equalsIgnoreCase("false")) {
			return Boolean.FALSE;
		}
		else if (value.equals("1")) {
			return Boolean.TRUE;
		}
		else if (value.equals("0")) {
			return Boolean.FALSE;
		}
		else {
			return null;
		}
	}

	@Override
	public Boolean setConverted(String value) throws Inconvertible {
		Boolean v = getTrans(value);
		if (v == null) {
			throw new Inconvertible("can not convert '" + value + "' to Boolean!");
		}
		return this.set(v);
	}

	@Override
	public Boolean copy() {
		return this.get();
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

}
