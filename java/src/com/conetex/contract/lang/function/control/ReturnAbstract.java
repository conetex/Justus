package com.conetex.contract.lang.control;

import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class ReturnAbstract<T> extends Accessible<T>{

	public static class Result{
		public boolean toReturn = false;
	}

	public abstract T getFrom(Structure thisObject, Result r) throws AbstractRuntimeException;

}
