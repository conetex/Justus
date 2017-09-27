package com.conetex.contract.lang;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.access.Setable;
import com.conetex.contract.lang.access.SetableValue;

public class Cast {

	@SuppressWarnings("unchecked")
	public static <X> Accessible<X> toTypedAccessible(Accessible<?> thisObj, Class<X> rawType) {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (Accessible<X>) thisObj;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <X> Setable<X> toTypedSetable(Setable<?> thisObj, Class<X> rawType) {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (SetableValue<X>) thisObj;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <X> Primitive<X> toTypedPrimitive(Primitive<?> thisObj, Class<X> rawType) {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (Primitive<X>) thisObj;
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <X> Value<X> toTypedValue(Value<?> thisObj, Class<X> rawType) {
		if (rawType.isAssignableFrom(thisObj.getRawTypeClass())) {
			return (Value<X>) thisObj;
		}
		return null;
	}

}
