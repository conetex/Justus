package com.conetex.contract.data.valueImplement;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Structure implements Value<Structure> {// { Value<Value<?>[]>

	private final Complex type;

	private Structure parent;

	private Value<?>[] values;

	public static Structure _create(final Complex theAttributeTuple, final Value<?>[] theValues, final Structure theParent) {
		if (theAttributeTuple != null && theValues != null) {
			return null;// new Structure(theAttributeTuple, theValues,
						// theParent);
		}
		return null;
	}

	public static Structure create(final Complex theAttributeTuple, final Structure theParent) {
		if (theAttributeTuple != null) {
			return new Structure(theAttributeTuple, theParent);
		}
		return null;
	}

	/*
	 * private Structure(final Complex theAttributeTuple, final Value<?>[]
	 * theValues, final Structure theParent){ this.type = theAttributeTuple;
	 * this.values = theValues; this.parent = theParent; }
	 */

	private Structure(final Complex theAttributeTuple, final Structure theParent) {
		this.type = theAttributeTuple;
		this.values = new Value<?>[theAttributeTuple.getAttributesSize()];
		this.parent = theParent;
	}

	public static String[] split(String aName) {
		String[] re = new String[2];
		if (aName == null) {
			return re;
		}
		int i = aName.indexOf(Label.NAME_SEPERATOR);
		if (i > -1 && i < aName.length()) {
			re[0] = aName.substring(0, i);
			if (i + Label.NAME_SEPERATOR.length() < aName.length()) {
				re[1] = aName.substring(i + Label.NAME_SEPERATOR.length());
			}
		}
		return re;
	}

	public Structure getStructure(String aName) throws ValueCastException {
		// public <V extends Value<?>> V getValue (String aName, Class<V> c){
		// TODO do xpath syntax. access parent objects ???
		if (this.type.getName().equals(aName)) {
			return this;
		}
		int idIndex = this.type.getSubAttributeIndex(aName);
		if (idIndex > -1) {
			Value<Structure> v = getValue(idIndex, Structure.class);
			if (v == null) {
				return null;
			}
			return v.get();
		}
		else {
			String[] names = Structure.split(aName);
			if (names[0] != null) {
				if (names[1] != null) {
					idIndex = this.type.getSubAttributeIndex(names[0]);
					// TODO wenn hier die typen nicht passen und keine structure
					// da liegt, sondern
					// was anderes...
					// sollte das vernünftig gemeldet werden!!!
					Value<Structure> subStructure = getValue(idIndex, Structure.class);
					if (subStructure != null) {
						return subStructure.get().getStructure(names[1]);
					}
				}
			}
			else {
				if (this.parent != null) {
					return this.parent.getStructure(aName);
				}
			}

		}
		return null;
	}

	public <R> Value<R> getValue(String aName, Class<R> clazz) throws ValueCastException {
		// TODO hier erst getStructure...

		// public <V extends Value<?>> V getValue (String aName, Class<V> c){
		// TODO do xpath syntax. access parent objects ???
		if (this.type.getName().equals(aName)) {
			if (clazz == Structure.class) {
				return RtCast.<R>toTypedValue(this, clazz);// (Value<R>) this;
			}
			else {
				System.err.println("Cast not possible: " + clazz + " != " + this.getClass());
				return null;
			}
		}
		int idIndex = this.type.getSubAttributeIndex(aName);
		if (idIndex > -1) {
			return getValue(idIndex, clazz);
		}
		else {
			String[] names = Structure.split(aName);
			if (names[0] != null) {
				if (names[1] != null) {
					idIndex = this.type.getSubAttributeIndex(names[0]);
					// TODO wenn hier die typen nicht passen und keine structure
					// da liegt, sondern
					// was anderes...
					// sollte das vernünftig gemeldet werden!!!
					Value<Structure> subStructure = getValue(idIndex, Structure.class);
					if (subStructure != null) {
						return subStructure.get().getValue(names[1], clazz);
					}
				}
			}
			else {
				if (this.parent != null) {
					return this.parent.getValue(aName, clazz);
				}
			}

		}
		return null;
	}

	private <R> Value<R> getValue(int i, Class<R> c) throws ValueCastException {
		Value<?> v = getValue(i);
		if (v != null) {
			if (v.getRawTypeClass() == c) {
				return RtCast.<R>toTypedValue(v, c);
			}
			else {
				System.err.println("Cast not possible: " + c + " != " + v.getRawTypeClass());
			}
		}
		return null;
	}

	private Value<?> getValue(int i) {
		if (i > -1 && i < this.values.length) {
			return this.values[i];
		}
		return null;
	}

	private static <T> Value<T> clone(Value<T> src) throws Invalid {
		Primitive<T> type = Primitive.<T>getInstanceAtRunTime(src.getClass(), src.getRawTypeClass());
		if (type == null) {
			// TODO ERROR
			return null;
		}
		Value<T> re = type.createValue();
		T val = src.copy();
		re.set(val); // throws the Exception
		return re;
	}

	public boolean set(String id, Value<?> value) throws Invalid {
		int i = this.type.getSubAttributeIndex(id);
		if (i > -1 && i < this.values.length) {
			this.values[i] = value;
			return true;
		}
		return false;
	}

	@Override
	public Structure get() {
		return this;
	}

	@Override
	public Structure copy() throws Invalid {
		if (this.values == null) {
			return null;
		}
		Value<?>[] theValues = new Value<?>[this.values.length];
		for (int i = 0; i < theValues.length; i++) {
			theValues[i] = clone(this.values[i]);
		}

		Structure StructureNew = new Structure(this.type, this.parent);
		StructureNew.values = theValues;
		return StructureNew;
	}

	@Override
	public Structure set(Structure other) throws Invalid {
		// TODO typcheck ...
		if (this.values == null || this.values.length == other.values.length) {
			this.values = other.values;
		}
		return this;
	}

	public Value<?>[] _setConverted2(String value) throws Inconvertible, Invalid {
		throw new Inconvertible("can not create Structure from String!");
	}

	@Override
	public Structure setConverted(String value) throws Inconvertible, Invalid {
		throw new Inconvertible("can not create Structure from String!");
	}

	@Override
	public Class<Structure> getRawTypeClass() {
		return Structure.class;
	}

}
