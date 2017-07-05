package org.conetex.prime2.contractProcessing2.data.valueImplement;

import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public abstract class MailAddress extends SizedASCII{
	
	// See http://stackoverflow.com/questions/7717573/what-is-the-longest-possible-email-address
	public abstract int getMaxSize(); // longest email-address is 254
	
	@Override
	public void set(String aValue) throws Invalid{
		if(aValue == null){
			super.actual = null;
		}
		
		aValue = aValue.trim();
		if(aValue.length() > this.getMaxSize()){
			throw new Invalid("'" + aValue + "' is longer than " + this.getMaxSize());
		}
		
		if( aValue.matches("\\A[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z") ){
			super.actual = aValue;
		}
		else{
			throw new Invalid("'" + aValue + "' is no valid mail-Address");
		}
	}		
}
