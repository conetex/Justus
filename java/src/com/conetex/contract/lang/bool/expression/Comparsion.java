package com.conetex.contract.lang.bool.expression;

import com.conetex.contract.lang.Accessible;

public class Comparsion {

	public static Accessible<Boolean> createComparison(Accessible<?> a, Accessible<?> b, String name) {
		Class<?> baseTypA = a.getBaseType();
		Class<?> baseTypB = b.getBaseType();
		if (Number.class.isAssignableFrom(baseTypA) && Number.class.isAssignableFrom(baseTypB)) {
			Accessible<? extends Number> theA = a.<Number>as(Number.class);
			Accessible<? extends Number> theB = b.<Number>as(Number.class);
			return ComparisonNumber.create(theA, theB, name);
		}
		/* 
		if (Number.class.isAssignableFrom(baseTypA) && Number.class.isAssignableFrom(baseTypB)) {
			Accessible<? extends Number> theA = (Accessible<? extends Number>) a;
			Accessible<? extends Number> theB = (Accessible<? extends Number>) b;
			return ComparisonNumber.create(theA, theB, name);
		} 
		if (baseTypA == String.class && baseTypB == String.class) {
			Accessible<String> theA = (Accessible<String>) a.as(String.class);
			Accessible<String> theB = (Accessible<String>) b;
			return ComparisonString.create(theA, theB, name);
		}
		*/
		if (baseTypA == String.class && baseTypB == String.class) {
			Accessible<String> theA = a.as(String.class);
			Accessible<String> theB = b.as(String.class);
			return ComparisonString.create(theA, theB, name);
		}
		return null;
	}

}
