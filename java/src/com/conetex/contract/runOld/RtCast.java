package com.conetex.contract.runOld;

import com.conetex.contract.lang.value.Value;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class RtCast{

	@SuppressWarnings("unchecked")
	public static <X> Value<X> toTypedValue(Value<?> thisObj, Class<X> rawType) throws ValueCastException {
		if(rawType.isAssignableFrom(thisObj.getRawTypeClass())){
			return (Value<X>) thisObj;
		}
		throw new ValueCastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

	@SuppressWarnings("unchecked")
	public static <X> X cast(Object thisObj, Class<X> rawType) throws ValueCastException {
		if(thisObj == null){
			return null;
		}
		if(thisObj.getClass() == rawType){
			return (X) thisObj;
		}
		throw new ValueCastException(thisObj.getClass().getName() + " can not be casted to " + rawType.getName());
	}

}
