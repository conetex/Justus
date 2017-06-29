package org.conetex.prime2.contractProcessing2.data.values;

import org.conetex.prime2.contractProcessing2.data.Value;

	public class ASCII8 extends SizedASCII{
		public int getMaxSize(){ return 8; }
		@Override
		public Value<String> createValue() {
			ASCII8 re = new ASCII8();
			re.value = this.getCopy();
			return re;
		}				
	}
