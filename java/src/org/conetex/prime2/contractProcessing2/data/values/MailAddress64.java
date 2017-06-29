package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;

public class MailAddress64 extends MailAddress{
	public int getMaxSize(){ return 64; } // longest email-address is 254

	@Override
	public Value<String> createValue() {
		MailAddress64 re = new MailAddress64();
		re.value = this.getCopy();
		return re;
	}
}
