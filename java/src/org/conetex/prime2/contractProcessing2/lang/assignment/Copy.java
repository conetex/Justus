package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.Reference2Value;

public class Copy<T> extends AbstractAssigment<T>{

	public static <T> Copy<T> create(Reference2Value<T> trg, Reference2Value<T> src){
		if(src == null || trg == null){
			return null;
		}
		return new Copy<T>(trg, src);
	}
	
	private Copy(Reference2Value<T> trg, Reference2Value<T> src){
		super(trg, src);
	}
	
	@Override
	public boolean doCopy() {
		return true;
	}

}
