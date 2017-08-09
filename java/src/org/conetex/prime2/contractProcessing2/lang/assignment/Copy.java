package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Setable;

public class Copy<T> extends AbstractAssigment<T>{

	public static <T> Copy<T> create(Setable<T> trg, Accessible<T> src){
		if(src == null || trg == null){
			return null;
		}
		return new Copy<T>(trg, src);
	}
	
	private Copy(Setable<T> trg, Accessible<T> src){
		super(trg, src);
	}
	
	@Override
	public boolean doCopy() {
		return true;
	}

}
