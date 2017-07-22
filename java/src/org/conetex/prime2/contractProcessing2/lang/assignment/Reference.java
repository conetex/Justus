package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;
import org.conetex.prime2.contractProcessing2.lang.Setable;

public class Reference<T> extends AbstractAssigment<T>{

	public static <T> Reference<T> create(Setable<T> trg, Accessible<T> src){
		if(src == null || trg == null){
			return null;
		}
		return new Reference<T>(trg, src);
	}
	
	private Reference(Setable<T> trg, Accessible<T> src){
		super(trg, src);
	}
	
	@Override
	public boolean doCopy() {
		return false;
	}

}
