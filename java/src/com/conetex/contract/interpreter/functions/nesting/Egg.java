package com.conetex.contract.interpreter.functions.nesting;

import java.util.HashSet;
import java.util.Set;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.functions.exception.OperationInterpreterException;
import com.conetex.contract.lang.Accessible;

public abstract class Egg<T> {

	private String name;

	private Set<String> meaning = new HashSet<>();

	protected Egg(String theName) {
		this.name = theName;
	}

	public final String getName() {
		return this.name;
	}

	final Accessible<? extends T> createThis(CodeNode n, Complex parentTyp) throws OperationInterpreterException {
		if (!this.meaning.contains(n.getTag())) {
			System.err.println("Operation " + n.getTag() + " not found!");
			return null;
		}
		return this.create(n, parentTyp);
	}

	public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp) throws OperationInterpreterException;

	final Set<String> keySet() {
		return this.meaning;
	}

	public final void means(String theOperationName) {
		if (this.meaning.contains(theOperationName)) {
			System.err.println("duplicate operation '" + theOperationName + "' in " + this.getName());
		}
		this.meaning.add(theOperationName);
	}

	public final void means(String[] theOperationNames) {
		for (String theOperationName : theOperationNames) {
			this.means(theOperationName);
		}
	}

}

/*
 * public abstract class Egg<T> extends Abstract<T> {
 * 
 * private Map<String, Egg<T>> _builder = new HashMap<>();
 * 
 * public Egg(String name) { super(name); }
 * 
 * public final void _means(String theOperationName, Egg<T> b) { if
 * (this._builder.containsKey(theOperationName)) {
 * System.err.println("duplicate operation '" + theOperationName + "' in " +
 * this.getName()); } this._builder.put(theOperationName, b); }
 * 
 * public final void _means(String theOperationName) {
 * this._means(theOperationName, this); }
 * 
 * final Accessible<? extends T> _createThis(CodeNode n, Complex parentTyp)
 * throws OperationInterpreterException { String name = n.getTag(); Egg<T> s =
 * this._builder.get(name); if (s == null) { System.err.println("Operation " +
 * name + " not found!"); return null; } return s.create(n, parentTyp); }
 * 
 * final Set<String> _keySet() { return this._builder.keySet(); }
 * 
 * public abstract Accessible<? extends T> create(CodeNode n, Complex parentTyp)
 * throws OperationInterpreterException;
 * 
 * }
 */