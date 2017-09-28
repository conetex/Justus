package com.conetex.contract.interpreter;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.interpreter.exceptionLang.CastException;
import com.conetex.contract.interpreter.exceptionType.AbstractTypException;
import com.conetex.contract.interpreter.exceptionType.TypException;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.Setable;
import com.conetex.contract.lang.access.SetableValue;
import com.conetex.contract.lang.control.function.Return;
import com.conetex.contract.runtime.exceptionValue.ValueCastException;

public class Cast {

	@SuppressWarnings("unchecked")
	public static <X> Primitive<X> toTypedPrimitive(Primitive<?> thisObj, Class<X> rawType) throws AbstractTypException {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (Primitive<X>) thisObj;
		}
		throw new TypException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}


	
	@SuppressWarnings("unchecked")
	public static <X> Accessible<X> toTypedAccessible(Accessible<?> thisObj, Class<X> rawType) throws CastException {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (Accessible<X>) thisObj;
		}
		throw new CastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

	@SuppressWarnings("unchecked")
	public static <X> Return<X> toTypedReturn(Accessible<?> thisObj, Class<X> rawType) throws CastException {
		if (thisObj instanceof Return && rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (Return<X>) thisObj;
		}
		throw new CastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

	@SuppressWarnings("unchecked")
	public static <X> Setable<X> toTypedSetable(Setable<?> thisObj, Class<X> rawType) throws CastException {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (SetableValue<X>) thisObj;
		}
		throw new CastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}



}
