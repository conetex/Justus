package com.conetex.contract.data.valueImplement;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.Primitive;
import com.conetex.contract.data.valueImplement.exception.Inconvertible;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public class Structure implements Value<Structure> {// { Value<Value<?>[]>

	private final Complex type;

	private Structure parent;

	private Value<?>[] values;

	public static Structure _create(final Complex theAttributeTuple, final Value<?>[] theValues,
			final Structure theParent) {
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

	@SuppressWarnings("unchecked")
	private <V extends Value<?>> V _getValue(String aName, Class<V> c) {
		// TODO do xpath syntax. access parent objects ???
		if (this.type.getName().equals(aName)) {
			return (V) this;
		}
		int idIndex = this.type.getSubAttributeIndex(aName);
		if (idIndex > -1) {
			return _getValue(idIndex, c);
		} else {
			/*
			 * int i = aName.indexOf(Label.NAME_SEPERATOR); if(i > -1 && i <
			 * aName.length()){ String nameOfSubStructure = aName.substring(0,
			 * i); if(i + Label.NAME_SEPERATOR.length() < aName.length()){
			 * attributeIdx = this.type.getAttributeIndex( nameOfSubStructure );
			 * Value.Interface<Structure> subStructure = getValue(attributeIdx,
			 * Struct.class); if(subStructure != null){ Structure s =
			 * subStructure.get(); if(s != null){ aName =
			 * aName.substring(i+Label.NAME_SEPERATOR.length()); return
			 * s.getValue(aName, c); } } } }
			 */
			String[] names = Structure.split(aName);
			if (names[0] != null) {
				if (names[1] != null) {
					idIndex = this.type.getSubAttributeIndex(names[0]);
					// TODO wenn hier die typen nicht passen und keine structure
					// da liegt, sondern
					// was anderes...
					// sollte das vernünftig gemeldet werden!!!
					Structure subStructure = _getValue(idIndex, Structure.class);
					if (subStructure != null) {
						return subStructure._getValue(names[1], c);
					}
				}
			}

		}
		return null;
	}

	public Value<?> _getValue(String aName) {
		return this._getValue(aName, Value.class);// TODO seltsam sieht das aus
													// ...
	}

	@SuppressWarnings("unchecked")
	private <V extends Value<?>> V _getValue(int i, Class<V> c) {
		Value<?> v = getValue(i);
		if (v != null) {
			// TODO check this cast
			return (V) v;
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <R extends Value<?>> R _getValueNewNew(String aName, Class<R> clazz) {
		// public <V extends Value<?>> V getValue (String aName, Class<V> c){
		// TODO do xpath syntax. access parent objects ???
		if (this.type.getName().equals(aName)) {
			return (R) this;
		}
		int idIndex = this.type.getSubAttributeIndex(aName);
		if (idIndex > -1) {
			// TODO check this cast
			return (R) getValue(idIndex);
		} else {
			String[] names = Structure.split(aName);
			if (names[0] != null) {
				if (names[1] != null) {
					idIndex = this.type.getSubAttributeIndex(names[0]);
					// TODO wenn hier die typen nicht passen und keine structure
					// da liegt, sondern
					// was anderes...
					// sollte das vernünftig gemeldet werden!!!
					Structure subStructure = _getValue(idIndex, Structure.class);
					if (subStructure != null) {
						return subStructure._getValueNewNew(names[1], clazz);
					}
				}
			} else {
				if (this.parent != null) {
					return this.parent._getValueNewNew(aName, clazz);
				}
			}
		}
		return null;
	}	



	public Value<Structure> getStructure(String aName) {
		// public <V extends Value<?>> V getValue (String aName, Class<V> c){
		// TODO do xpath syntax. access parent objects ???
		if (this.type.getName().equals(aName)) {
			return this;
		}
		int idIndex = this.type.getSubAttributeIndex(aName);
		if (idIndex > -1) {
			return getValue(idIndex, Structure.class);
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
			} else {
				if (this.parent != null) {
					return this.parent.getStructure(aName);
				}
			}

		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public <R> Value<R> getValue(String aName, Class<R> clazz) {
		// TODO hier erst getStructure...
		
		
		// public <V extends Value<?>> V getValue (String aName, Class<V> c){
		// TODO do xpath syntax. access parent objects ???
		if (this.type.getName().equals(aName)) {
			if (clazz == Structure.class) {
				return (Value<R>) this;
			} else {
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
			} else {
				if (this.parent != null) {
					return this.parent.getValue(aName, clazz);
				}
			}

		}
		return null;
	}

	@SuppressWarnings("unchecked")
	private <R> Value<R> getValue(int i, Class<R> c) {
		Value<?> v = getValue(i);
		if (v != null) {
			if (v.getBaseType() == c) {
				return (Value<R>) v;
			} else {
				System.err.println("Cast not possible: " + c + " != " + v.getBaseType());
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
		Primitive<T> type = Primitive.<T> getInstance(src.getClass());
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
	public Class<Structure> getBaseType() {
		return Structure.class;
	}

}
