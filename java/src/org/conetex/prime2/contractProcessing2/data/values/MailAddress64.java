package org.conetex.prime2.contractProcessing2.data.values;

public class MailAddress64 extends MailAddress {

	public int getMaxSize() {
		// longest email-address is 254
		return 64;
	} 

}