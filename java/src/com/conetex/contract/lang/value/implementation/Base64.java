package com.conetex.contract.lang.value.implementation;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.run.exceptionValue.Invalid;

public class Base64 extends SizedASCII {

	// How to calculate the memory for Base64-encoded data? See
	// https://de.wikipedia.org/wiki/Base64
	// 4 * ( ceil (256 / 3) ) = 344
	// 4 * ( ceil (128 / 3) ) = 172
	// 4 * ( ceil (64 / 3) ) = 88
	public Base64(CodeNode theNode, int theMaxSize) {
		super(theNode, theMaxSize);
	}

	@Override
	public String set(String newValue) throws Invalid {
		String allowedChars = "A-Za-z0-9+/=";
		if (super.check(newValue, allowedChars)) {
			//if (newValue.matches("^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$")) { 
			if ( newValue.matches("[A-Za-z0-9+/]{1,}[=]{0,}") ) { // "[\\p{ASCII}]{0,}"
																	// "\\A\\p{ASCII}*\\z"
																	// "^[\\p{ASCII}]*$"
				
				// ^(?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?$
				//   [^-A-Za-z0-9+/=]|=[^=]|={3,}$
				
				super.actual = newValue;
			}
			else {
				throw new Invalid("no valid Base64! '=' is only allowed at the end!");
			}
		}
		return super.actual;
	}

	@Override
	public Base64 cloneValue() {
		Base64 re = new Base64(super.node.cloneNode(), super.maxSize);
		re.actual = this.actual;
		return re;
	}

}
