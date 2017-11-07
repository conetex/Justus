package com.conetex.contract.lang.value.implementation;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.run.exceptionValue.Invalid;

public class MailAddress extends SizedASCII{

	// See
	// http://stackoverflow.com/questions/7717573/what-is-the-longest-possible-email-address 
	// longest email-address is 254
	public MailAddress(CodeNode theNode, int theMaxSize) {
		super(theNode, theMaxSize);
	}

	@Override
	public String set(String aValueIn) throws Invalid {
		if(aValueIn == null){
			super.actual = null;
		}
		else{
			String aValue = aValueIn.trim();
			if(aValue.length() > this.getMaxSize()){
				throw new Invalid("'" + aValue + "' is longer than " + this.getMaxSize());
			}
			if(aValue.matches(
					"\\A[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z")){
				super.actual = aValue;
			}
			else{
				throw new Invalid("'" + aValue + "' is no valid mail-Address");
			}
		}
		return super.actual;
	}

	@Override
	public MailAddress cloneValue() {
		MailAddress re = new MailAddress(super.node.cloneNode(), super.maxSize);
		re.actual = super.actual;
		return re;
	}
	
}
