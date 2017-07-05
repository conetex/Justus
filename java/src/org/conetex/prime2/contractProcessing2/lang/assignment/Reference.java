package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;

public class Ref<T> extends AbstractAssigment<T>{

	public static <T> Ref<T> create(AccessibleValue<T> trg, AccessibleValue<T> src){
		if(src == null || trg == null){
			return null;
		}
		return new Ref<T>(trg, src);
	}
	
	private Ref(AccessibleValue<T> trg, AccessibleValue<T> src){
		super(trg, src);
	}
	
	@Override
	public boolean doCopy() {
		return false;
	}

}
