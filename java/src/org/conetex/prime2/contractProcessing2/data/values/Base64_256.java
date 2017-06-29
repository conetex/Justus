package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;

public class Base64_256 extends Base64{
	// How to calculate the memory for Base64-encoded data? See https://de.wikipedia.org/wiki/Base64 
	// 4 * ( ceil (256 / 3) ) = 344
	// 4 * ( ceil (128 / 3) ) = 172
	// 4 * ( ceil (64 / 3) ) = 88
	public int getMaxSize(){ return 344; }

	@Override
	public Value<String> createValue() {
		Base64_256 re = new Base64_256();
		re.value = this.getCopy();
		return re;
	}
}
