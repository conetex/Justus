package com.conetex.contract.run;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public abstract class Main {

	public abstract void run(Writer w) throws AbstractRuntimeException, UnknownCommandParameter, UnknownCommand, NullLabelException, EmptyLabelException;
	
	public abstract TypeComplex getRootTyp();
	
	public static List<CodeNode> getFunctionNodes(CodeNode n) throws AbstractInterpreterException {
		List<CodeNode> re = new LinkedList<>();
		List<CodeNode> children = n.getChildNodes();
		for(CodeNode c : children){
			// TODO implement
			c.getCommand();
		}
		return re;
	}
	
}
