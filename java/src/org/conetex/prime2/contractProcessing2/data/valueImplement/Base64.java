package org.conetex.prime2.contractProcessing2.data.valueImplement;

import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public abstract class Base64 extends SizedASCII{
	@Override	
	public String set(String newValue) throws Invalid{
		String allowedChars = "A-Za-z0-9+/=";
		if( super.check(newValue, allowedChars) ){
			if(! ( newValue.matches("[A-Za-z0-9+/]{1,}[=]{0,}") )  ){  // "[\\p{ASCII}]{0,}" "\\A\\p{ASCII}*\\z" "^[\\p{ASCII}]*$"
				super.actual = newValue;
			}				
			else{
				throw new Invalid("no valid Base64! '=' is only allowed at the end!");
			}
		}
		return super.actual;
	}
}
