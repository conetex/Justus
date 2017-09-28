package com.conetex.contract.run;

import com.conetex.contract.data.Value;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class RtCast {

	@SuppressWarnings("unchecked")
	public static <X> Value<X> toTypedValue(Value<?> thisObj, Class<X> rawType) throws ValueCastException {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (Value<X>) thisObj;
		}
		throw new ValueCastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

}
