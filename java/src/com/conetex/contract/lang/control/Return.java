package com.conetex.contract.lang.control;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Return<V> extends ReturnAbstract<V> {

	public static <SV> Return<SV> create2(Accessible<SV> theExpression) {
		// TODO drop this
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<>(theExpression);
	}

	public static <SV> Return<SV> create(Accessible<SV> theExpression) {
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<>(theExpression);
	}

	public static <T extends Number> Return<T> createNum(Accessible<T> theExpression, Class<T> theClass) {
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<>(theExpression);
	}

	public static <SV extends Number> Return<SV> createNum(Accessible<SV> theExpression) {
		if (theExpression == null) {
			System.err.println("theExpression is null");
			return null;
		}
		return new Return<>(theExpression);
	}

	private Accessible<V> expression;

	private Return(Accessible<V> theExpression) {
		this.expression = theExpression;
	}

	@Override
	public V getFrom(Structure thisObject) throws ValueCastException {
		return this.expression.getFrom(thisObject);
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO copyFrom is obsolet
		return null;
	}

	@Override
	public Class<V> getRawTypeClass() {
		return this.expression.getRawTypeClass();
	}

	@Override
	public V getFrom(Structure thisObject, Result r) throws ValueCastException {
		r.toReturn = true;
		return this.getFrom(thisObject);
	}

}
