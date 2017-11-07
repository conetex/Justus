package com.conetex.contract.runNew;

import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class Main {

	public abstract void run(Writer w) throws AbstractRuntimeException, UnknownCommandParameter, UnknownCommand;
	
}
