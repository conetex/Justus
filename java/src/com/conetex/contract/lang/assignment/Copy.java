package com.conetex.contract.lang.assignment;

import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.Setable;

public class Copy<T> extends AbstractAssigment<T> {

	public static <T> Copy<T> _createFromClass(Setable<?> trg, Accessible<?> src, Class<T> baseType) {
		if (src == null || trg == null) {
			return null;
		}
		Accessible<T> srcCasted = src.as(baseType);
		if (srcCasted != null) {
			Setable<T> trgCasted = trg.asSetable(baseType);
			if (trgCasted != null) {
				return new Copy<>(trgCasted, srcCasted);
			}
		}
		return null;
	}

	public static <T> Copy<T> _createFromTrg(Setable<T> trg, Accessible<?> src) {
		if (src == null || trg == null) {
			return null;
		}
		Class<T> trgBaseType = trg.getBaseType();
		if (trgBaseType == src.getBaseType()) {
			Accessible<T> srcCasted = src.as(trgBaseType);
			if (srcCasted != null) {
				return new Copy<>(trg, srcCasted);
			}
		}
		return null;
	}

	public static <T> Copy<T> _createFromSrc(Setable<?> trg, Accessible<T> src) {
		if (src == null || trg == null) {
			return null;
		}
		Class<T> srcBaseType = src.getBaseType();
		if (srcBaseType == trg.getBaseType()) {
			Setable<T> trgCasted = trg.asSetable(srcBaseType);
			if (trgCasted != null) {
				return new Copy<>(trgCasted, src);
			}
		}
		return null;
	}

	public static <T> Copy<T> _create(Setable<T> trg, Accessible<T> src) {
		if (src == null || trg == null) {
			return null;
		}
		return new Copy<T>(trg, src);
	}

	Copy(Setable<T> trg, Accessible<T> src) {
		super(trg, src);
	}

	@Override
	public boolean doCopy() {
		return true;
	}

}
