package com.conetex.contract.lang.assignment;

import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.Setable;
import com.conetex.contract.lang.Symbol;

public abstract class Creator {

	public static final Creator copy = new Creator() {
		@Override
		public <T> AbstractAssigment<T> create(Setable<T> trg, Accessible<T> src) {
			return new Copy<>(trg, src);
		}
	};

	public static final Creator refer = new Creator() {
		@Override
		public <T> AbstractAssigment<T> create(Setable<T> trg, Accessible<T> src) {
			return new Reference<>(trg, src);
		}
	};

	public abstract <T> AbstractAssigment<T> create(Setable<T> trg, Accessible<T> src);

	public static <T> AbstractAssigment<T> createFromUnqualified(Setable<?> trg, Accessible<?> src, Class<T> baseType, String name) {
		if (src == null || trg == null) {
			return null;
		}
		Accessible<T> srcCasted = src.as(baseType);
		if (srcCasted != null) {
			Setable<T> trgCasted = trg.asSetable(baseType);
			if (trgCasted != null) {
				Creator c = Creator.getCreator(name);
				if (c == null) {
					return null;
				}
				return c.<T>create(trgCasted, srcCasted);
			}
		}
		return null;
	}

	public static <T> AbstractAssigment<T> createFromQualifiedTrg(Setable<T> trg, Accessible<?> src, String name) {
		if (src == null || trg == null) {
			return null;
		}
		Class<T> trgBaseType = trg.getBaseType();
		if (trgBaseType == src.getBaseType()) {
			Accessible<T> srcCasted = src.as(trgBaseType);
			if (srcCasted != null) {
				Creator c = Creator.getCreator(name);
				if (c == null) {
					return null;
				}
				return c.<T>create(trg, srcCasted);
			}
		}
		return null;
	}

	public static <T> AbstractAssigment<T> createFromQualifiedSrc(Setable<?> trg, Accessible<T> src, String name) {
		if (src == null || trg == null) {
			return null;
		}
		Class<T> srcBaseType = src.getBaseType();
		if (srcBaseType == trg.getBaseType()) {
			Setable<T> trgCasted = trg.asSetable(srcBaseType);
			if (trgCasted != null) {
				Creator c = Creator.getCreator(name);
				if (c == null) {
					return null;
				}
				return c.<T>create(trgCasted, src);
			}
		}
		return null;
	}

	public static <T> AbstractAssigment<T> createFromQualified(Setable<T> trg, Accessible<T> src, String name) {
		if (src == null || trg == null) {
			return null;
		}
		Creator c = Creator.getCreator(name);
		if (c == null) {
			return null;
		}
		return c.<T>create(trg, src);
	}

	private static Creator getCreator(String name) {
		if (name.equals(Symbol.COPY)) {
			return Creator.copy;
		}
		if (name.equals(Symbol.REFER)) {
			return Creator.refer;
		}
		return null;
	}

}
