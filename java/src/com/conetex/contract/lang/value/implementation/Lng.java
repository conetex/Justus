package com.conetex.contract.lang.value.implementation;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.value.PrimitiveValue;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Lng extends PrimitiveValue<Long>{

	private Long actual;
	
	public Lng(CodeNode theNode) {
		super(theNode);
	}

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
	public Lng cloneValue() {
		Lng re = new Lng(super.node.cloneNode());
		re.actual = this.actual;
		return re;
	}

}