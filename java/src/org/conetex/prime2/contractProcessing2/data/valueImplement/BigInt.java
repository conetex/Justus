package org.conetex.prime2.contractProcessing2.data.valueImplement;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class BigInt implements Value<BigInteger>{

	private BigInteger actual;
	
	@Override
	public BigInteger set(BigInteger aValue){
		this.actual = aValue;
		return this.actual;
	}
	
	@Override
	public final BigInteger get(){
		return this.actual;
	}
	
	@Override
	public void setConverted(String value) throws Inconvertible {
		try {
			BigInteger v = new BigInteger(value);
			this.set(v);
		} catch (NumberFormatException e) {
			throw new Inconvertible("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public BigInteger copy() {
		return this.get();
	}

	@Override
	public Class<BigInteger> getBaseType() {
		return BigInteger.class;
	}

	
}
