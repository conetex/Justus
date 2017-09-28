package com.conetex.contract.lang.control;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public abstract class ReturnAbstract<T> extends Accessible<T> {

	public static class Result {
		public boolean toReturn = false;
	}

	public abstract T getFrom(Structure thisObject, Result r) throws ValueCastException;

}
