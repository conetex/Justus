package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;

public abstract class Base64 extends SizedASCII{
	@Override	
	public void set(String aValue) throws ValueException{
		String allowedChars = "A-Za-z0-9+/=";
		if( super.check(aValue, allowedChars) ){
			if(! ( aValue.matches("[A-Za-z0-9+/]{1,}[=]{0,}") )  ){  // "[\\p{ASCII}]{0,}" "\\A\\p{ASCII}*\\z" "^[\\p{ASCII}]*$"
				super.value = aValue;
			}				
			else{
				throw new ValueException("no valid Base64! '=' is only allowed at the end!");
			}
		}
	}
}
