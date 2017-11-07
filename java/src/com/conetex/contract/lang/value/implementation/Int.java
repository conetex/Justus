package com.conetex.contract.lang.value.implementation;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.value.PrimitiveValue;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Int extends PrimitiveValue<Integer>{

	private Integer actual;

	public Int(CodeNode theNode) {
		super(theNode);
	}

	@Override
	public Integer set(Integer aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public void setObject(Object value) throws Invalid, ValueCastException {
        this.set(RtCast.cast(value, Integer.class));
    }

	@Override
	public final Integer get() {
		return this.actual;
	}

	@Override
	public void setConverted(String value) throws Inconvertible {
		try{
			Integer v = Integer.valueOf(Integer.parseInt(value));
            this.set(v);
        }
		catch(NumberFormatException e){
			throw new Inconvertible("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public Integer getCopy() {
		return this.get();
	}

	@Override
	public Class<Integer> getRawTypeClass() {
		return Integer.class;
	}

	@Override
	public Int cloneValue() {
		Int re = new Int(super.node.cloneNode());
		re.actual = this.actual;
		return re;
	}
	
}
