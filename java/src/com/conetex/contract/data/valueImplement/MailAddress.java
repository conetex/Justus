package com.conetex.contract.data.valueImplement;

import com.conetex.contract.runtime.exceptionValue.Invalid;

public abstract class MailAddress extends SizedASCII {

	// See
	// http://stackoverflow.com/questions/7717573/what-is-the-longest-possible-email-address
	@Override
	public abstract int getMaxSize(); // longest email-address is 254

	@Override
	public String set(String aValueIn) throws Invalid {
		if (aValueIn == null) {
			super.actual = null;
		}
		else {
			String aValue = aValueIn.trim();
			if (aValue.length() > this.getMaxSize()) {
				throw new Invalid("'" + aValue + "' is longer than " + this.getMaxSize());
			}
			if (aValue.matches(
					"\\A[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z")) {
				super.actual = aValue;
			}
			else {
				throw new Invalid("'" + aValue + "' is no valid mail-Address");
			}
		}
		return super.actual;
	}
}
