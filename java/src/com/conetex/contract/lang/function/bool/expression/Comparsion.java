package com.conetex.contract.lang.function.bool.expression;

import com.conetex.contract.build.Cast;
import com.conetex.contract.build.exceptionFunction.CastException;
import com.conetex.contract.lang.function.Accessible;

public class Comparsion {

	public static Accessible<Boolean> createComparison(Accessible<?> a, Accessible<?> b, String name) throws CastException {
		Class<?> baseTypA = a.getRawTypeClass();
		Class<?> baseTypB = b.getRawTypeClass();
		if (Number.class.isAssignableFrom(baseTypA) && Number.class.isAssignableFrom(baseTypB)) {
			Accessible<? extends Number> theA = Cast.toTypedAccessible(a, Number.class);
			Accessible<? extends Number> theB = Cast.toTypedAccessible(b, Number.class);
			return ComparisonNumber.create(theA, theB, name);
		}
		/*
		 * if (Number.class.isAssignableFrom(baseTypA) &&
		 * Number.class.isAssignableFrom(baseTypB)) { Accessible<? extends
		 * Number> theA = (Accessible<? extends Number>) a; Accessible<? extends
		 * Number> theB = (Accessible<? extends Number>) b; return
		 * ComparisonNumber.create(theA, theB, name); } if (baseTypA ==
		 * String.class && baseTypB == String.class) { Accessible<String> theA =
		 * (Accessible<String>) a.as(String.class); Accessible<String> theB =
		 * (Accessible<String>) b; return ComparisonString.create(theA, theB,
		 * name); }
		 */
		if (baseTypA == String.class && baseTypB == String.class) {
			Accessible<String> theA = Cast.toTypedAccessible(a, String.class); // a.as(String.class);
			Accessible<String> theB = Cast.toTypedAccessible(b, String.class); // b.as(String.class);
			return ComparisonString.create(theA, theB, name);
		}
		return null;
	}

}
