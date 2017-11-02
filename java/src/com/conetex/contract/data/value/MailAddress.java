package com.conetex.contract.data.value;

import com.conetex.contract.data.Value;
import com.conetex.contract.run.exceptionValue.Invalid;

public class MailAddress extends SizedASCII{

	private int maxSize;
	
	// See
	// http://stackoverflow.com/questions/7717573/what-is-the-longest-possible-email-address 
	// longest email-address is 254
	public MailAddress(int theMaxSize){
		this.maxSize = theMaxSize;
	}
	
	public int getMaxSize(){
		return this.maxSize;
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
	public Value<String> cloneValue() throws Invalid {
		MailAddress re = new MailAddress(this.maxSize);
		re.actual = super.actual;
		return re;
	}
}
