package com.conetex.contract.data.valueImplement;

import com.conetex.contract.data.valueImplement.exception.Invalid;

public abstract class MailAddress extends SizedASCII {

    // See
    // http://stackoverflow.com/questions/7717573/what-is-the-longest-possible-email-address
    @Override
    public abstract int getMaxSize(); // longest email-address is 254

    @Override
    public String set(String aValue) throws Invalid {
        if (aValue == null) {
            super.actual = null;
        }

        aValue = aValue.trim();
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

        return super.actual;
    }
}
