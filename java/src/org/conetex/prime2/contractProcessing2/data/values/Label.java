package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;

public class Label extends SizedASCII {

	public static final String NAME_SEPERATOR = ".";

	public int getMaxSize() {
		return 8;
	}
	
}