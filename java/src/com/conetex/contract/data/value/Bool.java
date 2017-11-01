package com.conetex.contract.data.value;

import com.conetex.contract.data.Value;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Bool implements Value<Boolean>{

	private Boolean actual;

	@Override
	public Boolean set(Boolean aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public Boolean setObject(Object value) throws Invalid, ValueCastException {
		return this.set(RtCast.cast(value, Boolean.class));
	}

	@Override
	public final Boolean get() {
		return this.actual;
	}

	public static Boolean getTrans(String value) {
		if(value.equalsIgnoreCase("true")){
			return Boolean.TRUE;
		}
		else if(value.equalsIgnoreCase("false")){
			return Boolean.FALSE;
		}
		else if(value.equals("1")){
			return Boolean.TRUE;
		}
		else if(value.equals("0")){
			return Boolean.FALSE;
		}
		else{
			return null;
		}
	}

	@Override
	public Boolean setConverted(String value) throws Inconvertible {
		Boolean v = getTrans(value);
		if(v == null){
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
