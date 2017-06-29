package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;

public class Label extends SizedASCII{
	public static final String NAME_SEPERATOR = ".";
	public int getMaxSize(){ return 8; }
	@Override
	public Value<String> createValue() {
		Label re = new Label();
		super.value = this.getCopy();
		return re;
	}
}
