package com.conetex.contract.build;

import com.conetex.contract.build.exceptionLang.CastException;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.build.exceptionType.TypException;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.Setable;
import com.conetex.contract.lang.access.SetableValue;
import com.conetex.contract.lang.control.ReturnAbstract;

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
	public static <X> ReturnAbstract<X> toTypedReturn(Accessible<?> thisObj, Class<X> rawType) throws CastException {
		if (thisObj instanceof ReturnAbstract && rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (ReturnAbstract<X>) thisObj;
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
