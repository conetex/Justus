package com.conetex.contract.lang.assignment;

import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.Setable;
import com.conetex.contract.lang._SetableValueOLD;

public class Reference<T> extends AbstractAssigment<T> {

	public static <S, T> Reference<T> _create(_SetableValueOLD<T> trg, Accessible<S> src) {
		if (src == null || trg == null) {
			return null;
		}
		if (trg.getBaseType() == src.getBaseType()) {
			return new Reference<T>(trg, (Accessible<T>) src);
		}
		return null;
	}

	public static <T> Reference<T> _create(Setable<T> trg, Accessible<T> src) {
		if (src == null || trg == null) {
			return null;
		}
		return new Reference<T>(trg, src);
	}

	Reference(Setable<T> trg, Accessible<T> src) {
		super(trg, src);
	}

	@Override
	public boolean doCopy() {
		return false;
	}

}
