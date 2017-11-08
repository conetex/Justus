package com.conetex.contract.lang.function.bool.operator;

import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

//Unary operation
public class Not extends Accessible<Boolean>{

	public static Not create(Accessible<? extends Boolean> theSub) {
		if(theSub == null){
			return null;
		}
		return new Not(theSub);
	}

	private final Accessible<? extends Boolean> sub;

	private Not(Accessible<? extends Boolean> theSub) {
		this.sub = theSub;
	}

	@Override
	public Boolean getFrom(Structure thisObject) throws AbstractRuntimeException {
		Boolean b = this.sub.getFrom(thisObject);
		if(b == null){
			return null;
		}
		if(b.booleanValue()){
			return Boolean.FALSE;
		}
		return Boolean.TRUE;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) throws AbstractRuntimeException {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

}