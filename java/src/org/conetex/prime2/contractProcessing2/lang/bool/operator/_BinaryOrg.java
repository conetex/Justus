package org.conetex.prime2.contractProcessing2.lang.bool.operator;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang._x_Pair;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public abstract class _BinaryOrg extends _x_Pair<Boolean> implements Accessible<Boolean> {

	public static <RE> Accessible<RE> create(Accessible<Boolean> theA, Accessible<Boolean> theB, String operation, Class<RE> re) {
		if(re == Boolean.class){
			return (Accessible<RE>) create(theA, theB, operation);
		}
		return null;
	}
	
	public static _BinaryOrg create(Accessible<Boolean> theA, Accessible<Boolean> theB, String operation) {
		if (theA == null || theB == null) {
			return null;
		}
		if (operation.equals(Symbol.AND)) {
			return new _BinaryOrg(theA, theB) {
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if (a && b) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
		} else if (operation.equals(Symbol.OR)) {
			return new _BinaryOrg(theA, theB) {
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if (a || b) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
		} else if (operation.equals(Symbol.XOR)) {
			return new _BinaryOrg(theA, theB) {
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

	private _BinaryOrg(Accessible<Boolean> theA, Accessible<Boolean> theB) {
		super(theA, theB);
	}

	protected abstract Boolean calc(Boolean a, Boolean b);

	@Override
	public Boolean getFrom(Structure thisObject) {
		Boolean a = super.getA().getFrom(thisObject);
		Boolean b = super.getB().getFrom(thisObject);
		if (a == null || b == null) {
			return null;
		}
		return this.calc(a, b);
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public Accessible<Boolean> as(Class<?> baseType) {
		if(baseType == this.getBaseType()){
			return this;
		}
		return null;
	}
	
	@Override
	public <T> Accessible<T> as2(Class<T> baseType) {
		if(baseType == this.getBaseType()){
			return (Accessible<T>) this;
		}
		return null;
	}
	
	/*
	@Override
	public boolean compute(Structure thisObject) {
		getFrom(thisObject); // TODO compute ist nur f�rs debuggen ... ansonsten
								// ist das ja sinnlos hier!
		return true;
	}
	*/

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}

}
