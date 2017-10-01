package com.conetex.contract.data.value;

public class MailAddress64 extends MailAddress {

	@Override
	public int getMaxSize() {
		// longest email-address is 254
		return 64;
	}

}
