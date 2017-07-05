package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;

public class Reference<T> extends AbstractAssigment<T>{

	public static <T> Reference<T> create(AccessibleValue<T> trg, AccessibleValue<T> src){
		if(src == null || trg == null){
			return null;
		}
		return new Reference<T>(trg, src);
	}
	
	private Reference(AccessibleValue<T> trg, AccessibleValue<T> src){
		super(trg, src);
	}
	
	@Override
	public boolean doCopy() {
		return false;
	}

}
