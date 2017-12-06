package com.conetex.contract.build;

import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.build.exceptionType.TypException;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.access.AccessibleValue;
import com.conetex.contract.lang.function.control.ReturnAbstract;
import com.conetex.contract.lang.type.TypePrimitive;

public class Cast{

	@SuppressWarnings("unchecked")
	public static <X> TypePrimitive<X> toTypedPrimitive(TypePrimitive<?> thisObj, Class<X> rawType) throws AbstractTypException {
		if(rawType.isAssignableFrom(thisObj.getRawTypeClass())){
			return (TypePrimitive<X>) thisObj;
		}
		throw new TypException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

	@SuppressWarnings("unchecked")
	public static <X> Accessible<X> toTypedAccessible(Accessible<?> thisObj, Class<X> rawType) throws CastException {
		if(rawType.isAssignableFrom(thisObj.getRawTypeClass())){
			return (Accessible<X>) thisObj;
		}
		throw new CastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

	@SuppressWarnings("unchecked")
	public static <X> ReturnAbstract<X> toTypedReturn(Accessible<?> thisObj, Class<X> rawType) throws CastException {
		if(thisObj instanceof ReturnAbstract && rawType.isAssignableFrom(thisObj.getRawTypeClass())){
			return (ReturnAbstract<X>) thisObj;
		}
		throw new CastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

	@SuppressWarnings("unchecked")
	public static <X> AccessibleValue<X> toTypedSetable(AccessibleValue<?> thisObj, Class<X> rawType) throws CastException {
		if(rawType.isAssignableFrom(thisObj.getRawTypeClass())){
			return (AccessibleValue<X>) thisObj;
		}
		throw new CastException(thisObj.getRawTypeClass().getName() + " can not be casted to " + rawType.getName());
	}

}
