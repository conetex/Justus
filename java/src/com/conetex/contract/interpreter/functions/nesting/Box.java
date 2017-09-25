package com.conetex.contract.interpreter.functions.nesting;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.exception.OperationInterpreterException;
import com.conetex.contract.interpreter.functions.exception.OperationMeansNotCalled;
import com.conetex.contract.lang.Accessible;

public abstract class Box<T, S> extends Egg<T> {

	private Map<String, Egg<? extends S>> childBuilder = new HashMap<>();

	public Box(String name) {
		super(name);
	}

	public final void contains(String theOperationName, Egg<? extends S> b) {
		if (this.childBuilder.containsKey(theOperationName)) {
			System.err.println("duplicate inner operation '" + theOperationName + "' in " + this.getName());
		}
		this.childBuilder.put(theOperationName, b);
	}

	public final void contains(Egg<? extends S> b) throws OperationMeansNotCalled {
		Set<String> keySet = b.keySet();
		if (keySet.size() == 0) {
			throw new OperationMeansNotCalled(b.getName());
		}
		for (String s : b.keySet()) {
			this.contains(s, b);
		}
	}

	public final Accessible<? extends S> createChild(CodeNode n, Complex parentTyp)
			throws OperationInterpreterException {
		String name = n.getTag();
		Egg<? extends S> s = this.childBuilder.get(name);
		if (s == null) {
			System.err.println("inner Operation '" + name + "' not found in " + this.getName());
			return null;
		}
		System.out.println("createChild " + name + " " + n.getName());
		return s.createThis(n, parentTyp);
	}

	public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException;

}
