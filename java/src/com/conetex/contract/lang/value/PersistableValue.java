package com.conetex.contract.lang.value;

import java.math.BigInteger;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public abstract class PersistableValue<T> implements Value<T>{

	protected CodeNode node;
	
	protected PersistableValue(CodeNode theNode){
		this.node = theNode;
	}
	
	public void persist() throws UnknownCommandParameter, UnknownCommand {
		this.node.setParameter(Symbols.comValue(), this.get());
	}

}
