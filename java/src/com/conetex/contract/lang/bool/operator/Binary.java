package com.conetex.contract.lang.bool.operator;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public abstract class Binary extends Accessible<Boolean> {// implements
															// Accessible<Boolean>

	public static Binary create(Accessible<? extends Boolean> theA, Accessible<? extends Boolean> theB, String operation) {
		if (theA == null || theB == null) {
			return null;
		}
		if (operation.equals(Symbol.AND)) {
			return new Binary(theA, theB) {
				@Override
				protected Boolean calc(Boolean aA, Boolean aB) {
					if (aA.booleanValue() && aB.booleanValue()) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
		}
		else if (operation.equals(Symbol.OR)) {
			return new Binary(theA, theB) {
				@Override
				protected Boolean calc(Boolean aA, Boolean aB) {
					if (aA.booleanValue() || aB.booleanValue()) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
		}
		else if (operation.equals(Symbol.XOR)) {
			return new Binary(theA, theB) {
				@Override
				protected Boolean calc(Boolean aA, Boolean aB) {
					if (aA.booleanValue() ^ aB.booleanValue()) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
		}
		return null;
	}

	private Accessible<? extends Boolean> a;

	private Accessible<? extends Boolean> b;

	protected Binary(Accessible<? extends Boolean> theA, Accessible<? extends Boolean> theB) {
		this.a = theA;
		this.b = theB;
	}

	protected abstract Boolean calc(Boolean aA, Boolean aB);

	@Override
	public Boolean getFrom(Structure thisObject) throws ValueCastException {
		Boolean aA = this.a.getFrom(thisObject);
		Boolean aB = this.b.getFrom(thisObject);
		if (aA == null || aB == null) {
			return null;
		}
		return this.calc(aA, aB);
	}

	@Override
	public Boolean copyFrom(Structure thisObject) throws ValueCastException {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

}
