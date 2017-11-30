package com.conetex.contract.lang.value.implementation;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Constants;

public class Label extends SizedASCII{

	public Label( CodeNode theNode ) {
		super(theNode, Constants.labelSize());
	}

	

	@Override
	public Label cloneValue() {
		Label re = new Label(super.node.cloneNode());
		re.actual = this.actual;
		return re;
	}
	
}