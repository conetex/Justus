package com.conetex.contract.runNew;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;

public abstract class Writer {

	public abstract void write(CodeNode n) throws UnknownCommandParameter, UnknownCommand;
	
}
