package com.conetex.contract.lang.function.assign;

import com.conetex.contract.build.Cast;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.Setable;

public abstract class Creator{

	private static final Creator copy = new Creator(){
		@Override
		public <T> AbstractAssigment<T> create(Setable<T> trg, Accessible<T> src) {
			return new Copy<>(trg, src);
		}
	};

	private static final Creator refer = new Creator(){
		@Override
		public <T> AbstractAssigment<T> create(Setable<T> trg, Accessible<T> src) {
			return new Reference<>(trg, src);
		}
	};

	protected abstract <T> AbstractAssigment<T> create(Setable<T> trg, Accessible<T> src);

	public static <T> AbstractAssigment<T> createFromUnqualified(Setable<?> trg, Accessible<?> src, Class<T> rawType, String name) throws CastException {
		if(src == null || trg == null){
			return null;
		}
		Accessible<T> srcCasted = Cast.toTypedAccessible(src, rawType);// src.as(rawType);
		if(srcCasted != null){
			Setable<T> trgCasted = Cast.toTypedSetable(trg, rawType);// trg.asSetable(rawType);
			if(trgCasted != null){
				Creator c = Creator.getCreator(name);
				if(c == null){
					return null;
				}
				return c.create(trgCasted, srcCasted);
			}
		}
		return null;
	}

	public static <T> AbstractAssigment<T> createFromQualifiedTrg(Setable<T> trg, Accessible<?> src, String name) throws CastException {
		if(src == null || trg == null){
			return null;
		}
		Class<T> trgRawType = trg.getRawTypeClass();
		if(trgRawType == src.getRawTypeClass()){
			Accessible<T> srcCasted = Cast.toTypedAccessible(src, trgRawType);// src.as(trgRawType);
			if(srcCasted != null){
				Creator c = Creator.getCreator(name);
				if(c == null){
					return null;
				}
				return c.create(trg, srcCasted);
			}
		}
		return null;
	}

	public static <T> AbstractAssigment<T> createFromQualifiedSrc(Setable<?> trg, Accessible<T> src, String name) throws CastException {
		if(src == null || trg == null){
			return null;
		}
		Class<T> srcRawType = src.getRawTypeClass();
		if(srcRawType == trg.getRawTypeClass()){
			Setable<T> trgCasted = Cast.toTypedSetable(trg, srcRawType);// trg.asSetable(srcRawType);
			if(trgCasted != null){
				Creator c = Creator.getCreator(name);
				if(c == null){
					return null;
				}
				return c.create(trgCasted, src);
			}
		}
		return null;
	}

	public static <T> AbstractAssigment<T> createFromQualified(Setable<T> trg, Accessible<T> src, String name) {
		if(src == null || trg == null){
			return null;
		}
		Creator c = Creator.getCreator(name);
		if(c == null){
			return null;
		}
		return c.create(trg, src);
	}

	private static Creator getCreator(String name) {
		if(name.equals(Symbols.comCopy())){
			return Creator.copy;
		}
		if(name.equals(Symbols.comRefer())){
			return Creator.refer;
		}
		return null;
	}

}
