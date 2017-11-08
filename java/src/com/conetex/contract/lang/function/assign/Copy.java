package com.conetex.contract.lang.function.assign;

import com.conetex.contract.build.Cast;
import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.Setable;

public class Copy<T> extends AbstractAssigment<T>{

	public static <T> Copy<T> _createFromClass(Setable<?> trg, Accessible<?> src, Class<T> rawType) throws CastException {
		if(src == null || trg == null){
			return null;
		}
		Accessible<T> srcCasted = Cast.toTypedAccessible(src, rawType);// src.as(rawType);
		if(srcCasted != null){
			Setable<T> trgCasted = Cast.toTypedSetable(trg, rawType);// trg.asSetable(rawType);
			if(trgCasted != null){
				return new Copy<>(trgCasted, srcCasted);
			}
		}
		return null;
	}

	public static <T> Copy<T> _createFromTrg(Setable<T> trg, Accessible<?> src) throws CastException {
		if(src == null || trg == null){
			return null;
		}
		Class<T> trgRawType = trg.getRawTypeClass();
		if(trgRawType == src.getRawTypeClass()){
			Accessible<T> srcCasted = Cast.toTypedAccessible(src, trgRawType);// src.as(trgRawType);
			if(srcCasted != null){
				return new Copy<>(trg, srcCasted);
			}
		}
		return null;
	}

	public static <T> Copy<T> _createFromSrc(Setable<?> trg, Accessible<T> src) throws CastException {
		if(src == null || trg == null){
			return null;
		}
		Class<T> srcRawType = src.getRawTypeClass();
		if(srcRawType == trg.getRawTypeClass()){
			Setable<T> trgCasted = Cast.toTypedSetable(trg, srcRawType);// trg.asSetable(srcRawType);
			if(trgCasted != null){
				return new Copy<>(trgCasted, src);
			}
		}
		return null;
	}

	public static <T> Copy<T> _create(Setable<T> trg, Accessible<T> src) {
		if(src == null || trg == null){
			return null;
		}
		return new Copy<>(trg, src);
	}

	Copy(Setable<T> trg, Accessible<T> src) {
		super(trg, src);
	}

	@Override
	public boolean doCopy() {
		return true;
	}

}
