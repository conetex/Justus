package com.conetex.contract.data.value;

public class Label extends SizedASCII {

	public static final String NAME_SEPERATOR = ".";

	@Override
	public int getMaxSize() {
		return 8;
	}

}