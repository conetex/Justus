package com.conetex.contract.data.value;

import com.conetex.contract.data.Value;

public class ASCII8 extends SizedASCII{

	@Override
	public int getMaxSize() {
		return 8;
	}

	@Override
	public Value<String> cloneValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
