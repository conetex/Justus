package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;

public class Copy<T> extends AbstractAssigment<T>{

	public static <T> Copy<T> create(AccessibleValue<T> trg, AccessibleValue<T> src){
		if(src == null || trg == null){
			return null;
		}
		return new Copy<T>(trg, src);
	}
	
	private Copy(AccessibleValue<T> trg, AccessibleValue<T> src){
		super(trg, src);
	}
	
	@Override
	public boolean doCopy() {
		return true;
	}

}
