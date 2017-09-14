package com.conetex.contract.lang.assignment;

import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.AccessibleAbstract;
import com.conetex.contract.lang.Setable;
import com.conetex.contract.lang.SetableValue;

public class Reference<T> extends AbstractAssigment<T> {

	public static <S, T> Reference<T> create(SetableValue<T> trg, AccessibleAbstract<S> src) {
		if (src == null || trg == null) {
			return null;
		}
		if (trg.getBaseType() == src.getBaseType()) {
			return new Reference<T>(trg, (AccessibleAbstract<T>) src);
		}
		return null;
	}

	public static <T> Reference<T> _create(Setable<T> trg, AccessibleAbstract<T> src) {
		if (src == null || trg == null) {
			return null;
		}
		return new Reference<T>(trg, src);
	}

	private Reference(Setable<T> trg, AccessibleAbstract<T> src) {
		super(trg, src);
	}

	@Override
	public boolean doCopy() {
		return false;
	}

}
