package com.conetex.contract.lang.function.runtime;

import java.util.List;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.control.ReturnAbstract;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.ContractRuntime;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class Request extends Accessible<String> {
	
	private final String question;
		
	private Request(String theQuestion) {
		super();
		this.question = theQuestion;
	}
	
	@Override
	public String getFrom(Structure thisObject) throws AbstractRuntimeException {
		return ContractRuntime.getStringAnswer(this.question);
	}

	@Override
	public String copyFrom(Structure thisObject) throws AbstractRuntimeException {
		return getFrom(thisObject);
	}

	@Override
	public Class<String> getRawTypeClass() {
		return String.class;
	}

	@Override
	public String getCommand() {
		return Symbols.COM_REQUEST;
	}

	@Override
	public Accessible<?>[] getChildren() {
		return super.getChildrenDft();
	}

	@Override
	public String[] getParameter() {
		return new String[] { this.question };
	}

}
