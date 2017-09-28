package com.conetex.contract.lang.control.function;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.BuildFunctions;
import com.conetex.contract.build.exceptionLang.CastException;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.runtime.exceptionValue.Invalid;
import com.conetex.contract.runtime.exceptionValue.ValueCastException;

public class Function<V> extends Accessible<V> {

	private static Map<String, Function<? extends Number>>		instancesNum		= new HashMap<>();

	private static Map<String, Function<Boolean>>				instancesBoolean	= new HashMap<>();

	private static Map<String, Function<String>>				instancesString		= new HashMap<>();

	private static Map<String, Function<? extends Structure>>	instancesStructure	= new HashMap<>();

	private static Map<String, Function<?>>						instancesVoid		= new HashMap<>();

	public static Accessible<Boolean> getInstanceBool(String name) {
		Function<Boolean> f = instancesBoolean.get(name);
		if (f == null) {
			return null;
		}
		return f;
	}

	public static Accessible<? extends Number> getInstanceNum(String name) {
		Function<? extends Number> f = instancesNum.get(name);
		if (f == null) {
			return null;
		}
		return f;
	}

	public static Accessible<? extends Structure> getInstanceStructure(String name) {
		Function<? extends Structure> f = instancesStructure.get(name);
		if (f == null) {
			return null;
		}
		return f;
	}

	public static Accessible<? extends Object> getInstanceVoid(String name) {
		Function<? extends Object> fn = instancesVoid.get(name);
		if (fn != null) {
			return fn;
		}
		return null;
	}

	public static Accessible<?> getInstance(String name) {
		Function<? extends Number> fn = instancesNum.get(name);
		if (fn != null) {
			return fn;
		}
		Function<? extends Boolean> fb = instancesBoolean.get(name);
		if (fb != null) {
			return fb;
		}
		Function<? extends String> fs = instancesString.get(name);
		if (fs != null) {
			return fs;
		}
		Function<? extends Structure> fstruct = instancesStructure.get(name);
		if (fstruct != null) {
			return fstruct;
		}
		Function<? extends Object> fvoid = instancesVoid.get(name);
		if (fvoid != null) {
			return fvoid;
		}
		return null;
	}

	public static Function<? extends Number> createNum(Accessible<?>[] theSteps, String theName, Class<?> theRawTypeClass) throws CastException {
		if (theSteps == null) {
			System.err.println("theSteps is null");
			return null;
		}
		if (theName == null || theName.length() < 1) {
			System.err.println("theName is null");
			return null;
		}
		if (Function.instancesNum.containsKey(theName)) {
			System.err.println("duplicate function " + theName);
			return null;
		}

		if (theRawTypeClass == Byte.class) {
			List<Return<Byte>> returns = BuildFunctions.getReturns(theSteps, Byte.class);
			Function<? extends Number> main = new Function<>(theSteps, returns, theName, Byte.class);
			Function.instancesNum.put(theName, main);
			return main;
		}
		if (theRawTypeClass == Integer.class) {
			List<Return<Integer>> returns = BuildFunctions.getReturns(theSteps, Integer.class);
			Function<? extends Number> main = new Function<>(theSteps, returns, theName, Integer.class);
			Function.instancesNum.put(theName, main);
			return main;
		}
		if (theRawTypeClass == Long.class) {
			List<Return<Long>> returns = BuildFunctions.getReturns(theSteps, Long.class);
			Function<? extends Number> main = new Function<>(theSteps, returns, theName, Long.class);
			Function.instancesNum.put(theName, main);
			return main;
		}
		if (theRawTypeClass == BigInteger.class) {
			List<Return<BigInteger>> returns = BuildFunctions.getReturns(theSteps, BigInteger.class);
			Function<? extends Number> main = new Function<>(theSteps, returns, theName, BigInteger.class);
			Function.instancesNum.put(theName, main);
			return main;
		}

		System.err.println("is not num: " + theRawTypeClass);
		return null;
	}

	public static Function<Boolean> createBool(Accessible<?>[] theSteps, String theName) throws CastException {
		if (theSteps == null) {
			System.err.println("theSteps is null");
			return null;
		}
		if (theName == null || theName.length() < 1) {
			System.err.println("theName is null");
			return null;
		}
		if (Function.instancesBoolean.containsKey(theName)) {
			System.err.println("duplicate function " + theName);
			return null;
		}
		List<Return<Boolean>> returns = BuildFunctions.getReturns(theSteps, Boolean.class);
		Function<Boolean> re = new Function<>(theSteps, returns, theName, Boolean.class);
		Function.instancesBoolean.put(theName, re);
		return re;
	}

	public static Function<Structure> createStructure(Accessible<?>[] theSteps, String theName) throws CastException {
		if (theSteps == null) {
			System.err.println("theSteps is null");
			return null;
		}
		if (theName == null || theName.length() < 1) {
			System.err.println("theName is null");
			return null;
		}
		if (Function.instancesStructure.containsKey(theName)) {
			System.err.println("duplicate function " + theName);
			return null;
		}
		List<Return<Structure>> returns = BuildFunctions.getReturns(theSteps, Structure.class);
		Function<Structure> re = new Function<>(theSteps, returns, theName, Structure.class);
		Function.instancesStructure.put(theName, re);
		return re;
	}

	public static Function<Object> createVoid(Accessible<?>[] theSteps, String theName) throws CastException {
		if (theSteps == null) {
			System.err.println("theSteps is null");
			return null;
		}
		if (theName == null || theName.length() < 1) {
			System.err.println("theName is null");
			return null;
		}
		if (Function.instancesVoid.containsKey(theName)) {
			System.err.println("duplicate function " + theName);
			return null;
		}
		List<Return<Object>> returns = BuildFunctions.getReturns(theSteps, Object.class);
		Function<Object> re = new Function<>(theSteps, returns, theName, Object.class);
		Function.instancesVoid.put(theName, re);
		return re;
	}

	public static final <V> V doSteps(Accessible<?>[] steps, List<Return<V>> returns, Structure thisObject) throws ValueCastException {
		for (int i = 0; i < steps.length; i++) {
			if (steps[i].getClass() == Return.class) {
				for (Return<V> r : returns) {
					if (r == steps[i]) {
						V re = r.getFrom(thisObject.get());
						System.out.println("MOCK return: " + re);
						return re;
					}
				}
				System.err.println("not able to return... " + steps[i]);
			}
			Object stepResult = steps[i].getFrom(thisObject.get());
			System.out.println("MOCK stepResult: " + stepResult);
		}
		return null;
	}

	/*
	 * public static <SV extends Value<?>> Function<SV> create(Structure
	 * theData, Accessible<?>[] theSteps){ if(theData == null || theSteps ==
	 * null){ return null; } return new Function<SV>(theData, theSteps); }
	 */
	// private Value<?>[] values;
	// private String[] valueNames;
	// private Structure data;

	private String			name;

	private Class<V>		rawTypeClass;

	private List<Return<V>>	returns;

	@Override
	public String toString() {
		return "function " + this.name;
	}

	private Accessible<?>[] steps;

	private Function(Accessible<?>[] theSteps, List<Return<V>> theReturns, String theName, Class<V> theRawTypeClass) {
		// this.data = theData;
		this.steps = theSteps;
		this.returns = theReturns;
		this.name = theName;
		this.rawTypeClass = theRawTypeClass;
	}

	@Override
	public V getFrom(Structure thisObject) throws ValueCastException {
		System.out.println("Function getFrom " + this.name);
		Structure thisObjectB = thisObject.getStructure(this.name);
		if (thisObjectB == null) {
			System.err.println("Function Structure getFrom: no access to data for function " + this.name);
			return null;
		}
		return Function.doSteps(this.steps, this.returns, thisObjectB);
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<V> getRawTypeClass() {
		return this.rawTypeClass;
	}

}
