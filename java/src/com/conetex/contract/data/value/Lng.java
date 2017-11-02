package com.conetex.contract.data.value;

import com.conetex.contract.data.Value;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Lng implements Value<Long>{

	private Long actual;

	@Override
	public Long set(Long aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public Long setObject(Object value) throws Invalid, ValueCastException {
		return this.set(RtCast.cast(value, Long.class));
	}

	@Override
	public final Long get() {
		return this.actual;
	}

	@Override
	public Long setConverted(String value) throws Inconvertible, NumberFormatException {
		try{
			Long v = Long.valueOf(Long.parseLong(value));
			return this.set(v);
		}
		catch(NumberFormatException e){
			throw new Inconvertible("can not convert " + value + " to Long", e);
		}
	}

	@Override
	public Long getCopy() {
		return this.get();
	}

	@Override
	public Class<Long> getRawTypeClass() {
		return Long.class;
	}

	@Override
	public Value<Long> cloneValue() {
		// TODO Auto-generated method stub
		return null;
	}

}