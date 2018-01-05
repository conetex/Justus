package com.conetex.contract.run.exeption;

import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class NoInformantSubcribed extends AbstractRuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NoInformantSubcribed(String msg, Exception cause) {
		super(msg, cause);
	}
	
	public NoInformantSubcribed(String msg) {
		super(msg);
		// TODO Auto-generated constructor stub
	}

}
