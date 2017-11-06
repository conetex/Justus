package com.conetex.contract.lang.value.implementation;

import java.math.BigInteger;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.lang.value.PersistableValue;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class BigInt extends PersistableValue<BigInteger>{

	public BigInt(CodeNode theNode) {
		super(theNode);
	}

	private BigInteger actual;

	@Override
	public BigInteger setObject(Object value) throws Invalid, ValueCastException {
		return this.set(RtCast.cast(value, BigInteger.class));
	}

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
		try{
			BigInteger v = new BigInteger(value);
			return this.set(v);
		}
		catch(NumberFormatException e){
			throw new Inconvertible("can not convert " + value + " to Integer", e);
		}
	}

	@Override
	public BigInteger getCopy() {
		return this.get();
	}

	@Override
	public Class<BigInteger> getRawTypeClass() {
		return BigInteger.class;
	}

	@Override
	public BigInt cloneValue() {
		BigInt re = new BigInt(super.node.cloneNode());
		re.actual = this.actual;
		return re;
	}

}
