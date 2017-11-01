package com.conetex.contract.lang.control;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.Cast;
import com.conetex.contract.build.exceptionLang.CastException;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.control.ReturnAbstract.Result;
import com.conetex.contract.lang.math.ElementaryArithmetic;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.run.exceptionValue.Invalid;

public class Function<V> extends Accessible<V> {

	private static Map<String, Function<? extends Number>> instancesNum = new HashMap<>();

	private static Map<String, Function<Boolean>> instancesBoolean = new HashMap<>();

	private static Map<String, Function<String>> instancesString = new HashMap<>();

	private static Map<String, Function<? extends Structure>> instancesStructure = new HashMap<>();

	private static Map<String, Function<?>> instancesVoid = new HashMap<>();

	public static Function<Boolean> getInstanceBool(String name) {
		Function<Boolean> f = instancesBoolean.get(name);
		if (f == null) {
			return null;
		}
		return f;
	}

	public static Function<? extends Number> getInstanceNum(String name) {
		Function<? extends Number> f = instancesNum.get(name);
		if (f == null) {
			return null;
		}
		return f;
	}

	public static Function<? extends Structure> getInstanceStructure(String name) {
		Function<? extends Structure> f = instancesStructure.get(name);
		if (f == null) {
			return null;
		}
		return f;
	}

	public static Function<? extends Object> getInstanceVoid(String name) {
		Function<? extends Object> fn = instancesVoid.get(name);
		if (fn != null) {
			return fn;
		}
		return null;
	}

	public static Function<?> getInstance(String name) {
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
			List<ReturnAbstract<Byte>> returns = getReturns(theSteps, Byte.class);
			Function<? extends Number> main = new Function<>(theSteps, returns, theName, Byte.class);
			Function.instancesNum.put(theName, main);
			return main;
		}
		if (theRawTypeClass == Integer.class) {
			List<ReturnAbstract<Integer>> returns = getReturns(theSteps, Integer.class);
			Function<? extends Number> main = new Function<>(theSteps, returns, theName, Integer.class);
			Function.instancesNum.put(theName, main);
			return main;
		}
		if (theRawTypeClass == Long.class) {
			List<ReturnAbstract<Long>> returns = getReturns(theSteps, Long.class);
			Function<? extends Number> main = new Function<>(theSteps, returns, theName, Long.class);
			Function.instancesNum.put(theName, main);
			return main;
		}
		if (theRawTypeClass == BigInteger.class) {
			List<ReturnAbstract<BigInteger>> returns = getReturns(theSteps, BigInteger.class);
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
		List<ReturnAbstract<Boolean>> returns = getReturns(theSteps, Boolean.class);
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
		List<ReturnAbstract<Structure>> returns = getReturns(theSteps, Structure.class);
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
		List<ReturnAbstract<Object>> returns = getReturns(theSteps, Object.class);
		Function<Object> re = new Function<>(theSteps, returns, theName, Object.class);
		Function.instancesVoid.put(theName, re);
		return re;
	}

	public static final <V> V doSteps(Accessible<?>[] steps, List<ReturnAbstract<V>> returns, Result ret, Structure thisObject)
			throws AbstractRuntimeException {
		loopOverSteps: for (int i = 0; i < steps.length; i++) {
			if (steps[i] instanceof ReturnAbstract) {
				for (ReturnAbstract<V> r : returns) {
					if (r == steps[i]) {
						V re = r.getFrom(thisObject.get(), ret);// TODO get() weg
						if (ret.toReturn) {
							System.out.println("MOCK return: " + re);
							return re;
						}
						else {
							continue loopOverSteps;
						}
					}
				}
				System.err.println("not able to return... " + steps[i]);
			}
			else {
				Object stepResult = steps[i].getFrom(thisObject.get());
				System.out.println("MOCK stepResult: " + stepResult);
			}
		}
		return null;
	}

	public static Class<?> getReturnTyp(Accessible<?>[] theSteps) {
		Class<?> pre = null;
		for (Accessible<?> a : theSteps) {
			if (a instanceof ReturnAbstract) {
				Class<?> c = a.getRawTypeClass();
				if (pre != null) {
					if (pre != c) {
						if (Number.class.isAssignableFrom(c)) {
							pre = ElementaryArithmetic.getBiggest(pre, c);
						}
						else {
							System.err.println("typen passen nich...");
						}
					} // else ok
				}
				pre = c;
			}
		}
		if (pre == null) {
			return Object.class;
		}
		return pre;
	}

	public static <X> List<ReturnAbstract<X>> getReturns(Accessible<?>[] theSteps, Class<X> rawType) throws CastException {
		List<ReturnAbstract<X>> returns = new LinkedList<>();
		for (Accessible<?> a : theSteps) {
			if (a instanceof ReturnAbstract) {
				Class<?> c = a.getRawTypeClass();
				if (rawType.isAssignableFrom(c)) {
					ReturnAbstract<X> re = Cast.toTypedReturn(a, rawType);
					returns.add(re);
				}
				else {
					System.err.println("typen passen nich...");
				}
			}
		}
		return returns;
	}

	private String name;

	public String getName() {
		return this.name;
	}

	private Class<V> rawTypeClass;

	private List<ReturnAbstract<V>> returns;

	@Override
	public String toString() {
		return "function " + this.name;
	}

	private Accessible<?>[] steps;

	private Function(Accessible<?>[] theSteps, List<ReturnAbstract<V>> theReturns, String theName, Class<V> theRawTypeClass) {
		this.steps = theSteps;
		this.returns = theReturns;
		this.name = theName;
		this.rawTypeClass = theRawTypeClass;
	}

	public V getFromRoot(Structure thisObject) throws AbstractRuntimeException {
		System.out.println("Function getFrom " + this.name);

		Structure thisObjectB = thisObject.getStructure(this.name);
		if (thisObject.getParent() == null) {
			thisObjectB = thisObject;
		}

		if (thisObjectB == null) {
			System.err.println("Function Structure getFrom: no access to data for function " + this.name);
			return null;
		}
		return Function.doSteps(this.steps, this.returns, new Result(), thisObjectB);
	}

	@Override
	public V getFrom(Structure thisObject) throws AbstractRuntimeException {
		return Function.doSteps(this.steps, this.returns, new Result(), thisObject);
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
