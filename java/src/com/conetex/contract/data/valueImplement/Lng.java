package com.conetex.contract.data.valueImplement;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.exception.Inconvertible;

public class Lng implements Value<Long> {

	private Long actual;

	@Override
	public Long set(Long aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public final Long get() {
		return this.actual;
	}

	@Override
	public Long setConverted(String value) throws Inconvertible, NumberFormatException {
		try {
			Long v = Long.parseLong(value);
			return this.set(v);
		} catch (NumberFormatException e) {
			throw new Inconvertible("can not convert " + value + " to Long", e);
		}
	}

	@Override
	public Long copy() {
		return this.get();
	}

	@Override
	public Class<Long> getBaseType() {
		return Long.class;
	}

}