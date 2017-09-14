package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.Setable;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;

public class Copy<T> extends AbstractAssigment<T>{

	public static <S, T> Copy<T> create(SetableValue<T> trg, Accessible<S> src) {
		if(src == null || trg == null){
			return null;
		}
		if(trg.getBaseType() == src.getBaseType()){
			return new Copy<T>(trg, (Accessible<T>) src);
		}
		return null;
	}	
	
	public static <T> Copy<T> _create(Setable<T> trg, Accessible<T> src){
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
