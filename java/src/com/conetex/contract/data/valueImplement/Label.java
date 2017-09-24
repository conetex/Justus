package com.conetex.contract.data.valueImplement;

public class Label extends SizedASCII {

	public static final String NAME_SEPERATOR = ".";

	@Override
	public int getMaxSize() {
		return 8;
	}

}