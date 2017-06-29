package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.Reference2Value;

public class Ref<T> extends AbstractAssigment<T>{

	public static <T> Ref<T> create(Reference2Value<T> trg, Reference2Value<T> src){
		if(src == null || trg == null){
			return null;
		}
		return new Ref<T>(trg, src);
	}
	
	private Ref(Reference2Value<T> trg, Reference2Value<T> src){
		super(trg, src);
	}
	
	@Override
	public boolean doCopy() {
		return false;
	}

}
