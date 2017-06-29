package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.Reference2Value;

public class Ref<T> extends AbstractAssigment<T>{

	public static <T> Ref<T> create(Reference2Value<T> src, Reference2Value<T> trg){
		if(src == null || trg == null){
			return null;
		}
		return new Ref<T>(src, trg);
	}
	
	private Ref(Reference2Value<T> src, Reference2Value<T> trg){
		super(src, trg);
	}
	
	@Override
	public boolean doCopy() {
		return false;
	}

}
