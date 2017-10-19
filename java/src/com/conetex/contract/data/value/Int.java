package com.conetex.contract.data.value;

import com.conetex.contract.data.Value;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Int implements Value<Integer> {

	private Integer actual;

	@Override
	public Integer set(Integer aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public Integer setObject(Object value) throws Invalid, ValueCastException {
		return this.set( RtCast.cast(value, Integer.class) );			
	}
	
	@Override
	public final Integer get() {
		return this.actual;
	}

	@Override
	public Integer setConverted(String value) throws Inconvertible {
		try {
			Integer v = Integer.valueOf(Integer.parseInt(value));
			return this.set(v);
		}
		catch (NumberFormatException e) {
			throw new Inconvertible("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public Integer copy() {
		return this.get();
	}

	@Override
	public Class<Integer> getRawTypeClass() {
		return Integer.class;
	}

}
