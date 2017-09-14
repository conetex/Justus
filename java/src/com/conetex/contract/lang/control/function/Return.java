package com.conetex.contract.lang.control.function;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.lang.Accessible;

public class Return<V> extends Accessible<V> { // V extends Value<?>

	public static <SV> Return<SV> create2(Accessible<SV> theExpression) {
		// TODO drop this
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<SV>(theExpression);
	}

	public static <SV> Return<SV> create(Accessible<SV> theExpression) {
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<SV>(theExpression);
	}

	public static <T extends Number> Return<T> createNum(Accessible<T> theExpression, Class<T> theClass) {
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<T>(theExpression);
	}

	public static <SV extends Number> Return<SV> createNum(Accessible<SV> theExpression) {
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<SV>(theExpression);
	}

	private Accessible<V> expression;

	private Return(Accessible<V> theExpression) {
		this.expression = theExpression;
	}

	@Override
	public V getFrom(Structure thisObject) {
		return this.expression.getFrom(thisObject);
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO copyFrom is obsolet
		return null;
	}

	@Override
	public Class<V> getBaseType() {
		// TODO V ist oft noch java typ. das sollte sich jetzt nach Value
		// ändern. Hier
		// ist es schon so: V extends Value<?>
		return null;
	}

}
