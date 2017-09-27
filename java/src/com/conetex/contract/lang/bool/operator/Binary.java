package com.conetex.contract.lang.bool.operator;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.lang.access.Accessible;

public abstract class Binary extends Accessible<Boolean> {// implements
															// Accessible<Boolean>

	public static Binary create(Accessible<? extends Boolean> theA, Accessible<? extends Boolean> theB,
			String operation) {
		if (theA == null || theB == null) {
			return null;
		}
		if (operation.equals(Symbol.AND)) {
			return new Binary(theA, theB) {
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if (a && b) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
		} else if (operation.equals(Symbol.OR)) {
			return new Binary(theA, theB) {
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if (a || b) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
		} else if (operation.equals(Symbol.XOR)) {
			return new Binary(theA, theB) {
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if (a ^ b) {
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

	private Binary(Accessible<? extends Boolean> theA, Accessible<? extends Boolean> theB) {
		this.a = theA;
		this.b = theB;
	}

	protected abstract Boolean calc(Boolean a, Boolean b);

	@Override
	public Boolean getFrom(Structure thisObject) {
		Boolean a = this.a.getFrom(thisObject);
		Boolean b = this.b.getFrom(thisObject);
		if (a == null || b == null) {
			return null;
		}
		return this.calc(a, b);
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	/*
	 * @Override public boolean compute(Structure thisObject) {
	 * getFrom(thisObject); // TODO compute ist nur f�rs debuggen ... ansonsten
	 * // ist das ja sinnlos hier! return true; }
	 */

	@Override
	public Class<Boolean> getRawTypeClass() {
		return Boolean.class;
	}

}
