package com.conetex.contract.lang.value.implementation;

import com.conetex.contract.build.Constants;
import com.conetex.contract.lang.value.Value;

public class Label extends SizedASCII{

	public Label( ) {
		super(Constants.labelSize());
	}

	public static final String NAME_SEPERATOR = ".";

	@Override
	public Value<String> cloneValue() {
		// TODO Auto-generated method stub
		return null;
	}

}