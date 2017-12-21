package com.conetex.contract.lang.value.implementation;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.value.PrimitiveValue;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;
import com.conetex.contract.runOld.RtCast;

public class Bool extends PrimitiveValue<Boolean>{

	private Boolean actual;

	public Bool(CodeNode theNode) {
		super(theNode);
	}

	@Override
	public Boolean set(Boolean aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public void setObject(Object value) throws Invalid, ValueCastException {
        this.set(RtCast.cast(value, Boolean.class));
    }

	@Override
	public final Boolean get() {
		return this.actual;
	}

	private static Boolean getTrans(String value) {
		// TODO make "true"... a symbol...
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
	public void setConverted(String value) throws Inconvertible {
		Boolean v = getTrans(value);
		if(v == null){
			throw new Inconvertible("can not convert '" + value + "' to Boolean!");
		}
        this.set(v);
    }

	@Override
	public Boolean getCopy() {
		return this.get();
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

	@Override
	public Bool cloneValue() {
		Bool re = new Bool(super.node.cloneNode());
		re.actual = this.actual;
		return re;
	}
	
}
