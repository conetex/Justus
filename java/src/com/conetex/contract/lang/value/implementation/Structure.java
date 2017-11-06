package com.conetex.contract.lang.value.implementation;

import java.util.LinkedList;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.Type;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexOfFunction;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.run.RtCast;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class Structure implements Value<Structure>{// { Value<Value<?>[]>

	private final TypeComplex type;

	private Structure parent;

	private Value<?>[] values;

	public static Structure create(final TypeComplex theAttributeTuple, final Structure theParent) {
		if(theAttributeTuple != null){
			return new Structure(theAttributeTuple, theParent);
		}
		return null;
	}

	/*
	 * private Structure(final Complex theAttributeTuple, final Value<?>[]
	 * theValues, final Structure theParent){ this.type = theAttributeTuple;
	 * this.values = theValues; this.parent = theParent; }
	 */

	private Structure(final TypeComplex theAttributeTuple, final Structure theParent) {
		this.type = theAttributeTuple;
		this.values = new Value<?>[theAttributeTuple.getAttributesSize()];
		this.parent = theParent;
	}

	public void fillMissingValues() {
		for(int i = 0; i < this.values.length; i++){
			if(this.values[i] == null){
				Attribute<?> a = this.type.getSubAttribute(i);
				if(a == null){
					System.err.println("no getSubAttribute " + i);
				}
				else{
					this.values[i] = a.createNewValue(this);
				}
			}
		}
	}

	public static String[] split(String aName) {
		String[] re = new String[2];
		if(aName == null){
			return re;
		}
		int i = aName.indexOf(Label.NAME_SEPERATOR);
		if(i > -1 && i < aName.length()){
			re[0] = aName.substring(0, i);
			if(i + Label.NAME_SEPERATOR.length() < aName.length()){
				re[1] = aName.substring(i + Label.NAME_SEPERATOR.length());
			}
		}
		return re;
	}

	public Structure getStructure(String aName) throws ValueCastException {
		// public <V extends Value<?>> V getValue (String aName, Class<V> c){
		// TODO do xpath syntax. access parent objects ???
		if(this.type.getName().equals(aName)){
			return this;
		}
		int idIndex = this.type.getSubAttributeIndex(aName);
		if(idIndex > -1){
			Value<Structure> v = getValue(idIndex, Structure.class);
			if(v == null){
				return null;
			}
			return v.get();
		}
		else{
			String[] names = Structure.split(aName);
			if(names[0] != null){
				if(names[1] != null){
					idIndex = this.type.getSubAttributeIndex(names[0]);
					// TODO wenn hier die typen nicht passen und keine structure
					// da liegt, sondern
					// was anderes...
					// sollte das vern�nftig gemeldet werden!!!
					Value<Structure> subStructure = getValue(idIndex, Structure.class);
					if(subStructure != null){
						return subStructure.get().getStructure(names[1]);
					}
				}
			}
			else{
				if(this.parent != null){
					return this.parent.getStructure(aName);
				}
			}

		}
		return null;
	}

	public Value<?> getValue(String aName) {
		int i = this.type.getSubAttributeIndex(aName);
		if(i > -1){
			return getValue(i);
		}
		return null;
	}

	public <R> Value<R> getValue(String aName, Class<R> clazz) throws ValueCastException {
		// TODO hier erst getStructure...

		// public <V extends Value<?>> V getValue (String aName, Class<V> c){
		// TODO do xpath syntax. access parent objects ???
		if(this.type.getName().equals(aName)){
			if(clazz == Structure.class){
				return RtCast.<R>toTypedValue(this, clazz);// (Value<R>) this;
			}
			else{
				System.err.println("Cast not possible: " + clazz + " != " + this.getClass());
				return null;
			}
		}
		int idIndex = this.type.getSubAttributeIndex(aName);
		if(idIndex > -1){
			return getValue(idIndex, clazz);
		}
		else{
			String[] names = Structure.split(aName);
			if(names[0] != null){
				if(names[1] != null){
					idIndex = this.type.getSubAttributeIndex(names[0]);
					// TODO wenn hier die typen nicht passen und keine structure
					// da liegt, sondern
					// was anderes...
					// sollte das vern�nftig gemeldet werden!!!
					Value<Structure> subStructure = getValue(idIndex, Structure.class);
					if(subStructure != null){
						return subStructure.get().getValue(names[1], clazz);
					}
					/* Todo so machen wirs nich! scon bei call reservieren! */
					else{
						TypeComplexOfFunction z = TypeComplexOfFunction.getInstance(this.type.getName() + "." + names[0]);
						// ComplexFunction z = this.type.getComplexFunction(names[0]);
						if(z != null){
							return z.<R>getValue(names[1], clazz);
						}
					}

				}
			}
			else{
				if(this.parent != null){
					return this.parent.getValue(aName, clazz);
				}
			}

		}
		return null;
	}

	private <R> Value<R> getValue(int i, Class<R> c) throws ValueCastException {
		Value<?> v = getValue(i);
		if(v != null){
			if(v.getRawTypeClass() == c){
				return RtCast.<R>toTypedValue(v, c);
			}
			else{
				System.err.println("Cast not possible: " + c + " != " + v.getRawTypeClass());
			}
		}
		return null;
	}

	private Value<?> getValue(int i) {
		if(i > -1 && i < this.values.length){
			return this.values[i];
		}
		return null;
	}

	public boolean set(String id, Value<?> value) throws Invalid {
		int i = this.type.getSubAttributeIndex(id);
		if(i > -1 && i < this.values.length){
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
	public Structure getCopy() throws Invalid {
		if(this.values == null){
			return null;
		}
		Value<?>[] theValues = new Value<?>[this.values.length];
		for(int i = 0; i < theValues.length; i++){
			theValues[i] = this.values[i].cloneValue();
		}

		Structure StructureNew = new Structure(this.type, this.parent);
		StructureNew.values = theValues;
		return StructureNew;
	}

	@Override
	public Structure cloneValue() throws Invalid {
		return this.getCopy();
	}

	@Override
	public Structure set(Structure other) throws Invalid {
		// TODO typcheck ...
		if(this.values == null || this.values.length == other.values.length){
			this.values = other.values;
		}
		return this;
	}

	@Override
	public Structure setObject(Object value) throws Invalid, ValueCastException {
		Structure v = RtCast.cast(value, Structure.class);
		return this.set(v);
	}

	@Override
	public Structure setConverted(String value) throws Inconvertible, Invalid {
		throw new Inconvertible("can not create Structure from String!");
	}

	@Override
	public Class<Structure> getRawTypeClass() {
		return Structure.class;
	}

	public TypeComplex getComplex() {
		return this.type;
	}

	public Structure getParent() {
		return this.parent;
	}

	public void setParent(Structure theParent) {
		this.parent = theParent;
	}

	@Override
	public void persist(Attribute<?> a) throws UnknownCommandParameter, UnknownCommand {
		int i = 0;
		for(Value<?> v : this.values){
			v.persist(this.type.getSubAttribute(i));
			i++;
		}
	}

}