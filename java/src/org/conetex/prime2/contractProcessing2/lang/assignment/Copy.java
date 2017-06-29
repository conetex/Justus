package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.Reference2Value;

public class Copy<T> extends AbstractAssigment<T>{

	public static <T> Copy<T> create(Reference2Value<T> src, Reference2Value<T> trg){
		if(src == null || trg == null){
			return null;
		}
		return new Copy<T>(src, trg);
	}
	
	private Copy(Reference2Value<T> src, Reference2Value<T> trg){
		super(src, trg);
	}
	
	@Override
	public boolean doCopy() {
		return true;
	}

}
