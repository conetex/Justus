package com.conetex.contract.data.valueImplement;

import java.math.BigInteger;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.exception.Inconvertible;

public class BigInt implements Value<BigInteger> {

	private BigInteger actual;

	@Override
	public BigInteger set(BigInteger aValue) {
		this.actual = aValue;
		return this.actual;
	}

	@Override
	public final BigInteger get() {
		return this.actual;
	}

	@Override
	public BigInteger setConverted(String value) throws Inconvertible {
		try {
			BigInteger v = new BigInteger(value);
			return this.set(v);
		}
		catch (NumberFormatException e) {
			throw new Inconvertible("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public BigInteger copy() {
		return this.get();
	}

	@Override
	public Class<BigInteger> getRawTypeClass() {
		return BigInteger.class;
	}

}
