package com.conetex.contract.data.type;

import java.util.HashMap;
import java.util.Map;

import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.build.exceptionLang.ComplexWasInitializedExeption;
import com.conetex.contract.build.exceptionLang.DublicateComplexException;
import com.conetex.contract.build.exceptionLang.DuplicateIdentifierNameExeption;
import com.conetex.contract.build.exceptionLang.NullIdentifierException;
import com.conetex.contract.build.exceptionLang.PrototypeWasInitialized;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.PrototypeInInvalidState;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class FunctionAttributes extends Complex{

	private Structure prototype;

	private Structure runtimeCopyInUse;

	private Structure runtimeCopyNotInUse;

	private static Map<String, Attribute<?>> functions = new HashMap<>();

	private static Map<String, FunctionAttributes> instances = new HashMap<>();

	public static FunctionAttributes getInstance(String typeName) {
		return instances.get(typeName);
	}

	public void setPrototype(Structure thePrototype) throws PrototypeWasInitialized {
		if(this.prototype != null){
			throw new PrototypeWasInitialized(this.getName());
		}
		this.prototype = thePrototype;
		Structure parent = this.prototype.getParent();
		Complex complex = this.prototype.getComplex();
		Structure runtimeCopy = complex.createValue(parent);
		runtimeCopy.fillMissingValues();
		this.runtimeCopyNotInUse = runtimeCopy;
	}

	public Structure utilizeStructure(Structure obj) throws Invalid, ValueCastException, PrototypeInInvalidState {
		if(this.prototype == null){
			throw new PrototypeInInvalidState("prototype is null");
		}
		if(this.runtimeCopyInUse != null){
			throw new PrototypeInInvalidState("runtimeCopyInUse is not null");
		}
		if(this.runtimeCopyNotInUse == null){
			throw new PrototypeInInvalidState("runtimeCopyNotInUse is null");
		}

		this.runtimeCopyInUse = this.runtimeCopyNotInUse;
		this.runtimeCopyNotInUse = null;

		Complex complex = this.prototype.getComplex();
		for(String key : complex.getSubAttributeNames()){
			Value<?> vOrg = this.prototype.getValue(key);
			Value<?> vNew = this.runtimeCopyInUse.getValue(key);
			vNew.setObject(vOrg.get());
		}

		this.runtimeCopyInUse.setParent(obj);
		return this.runtimeCopyInUse;
	}

	public void unutilizeStructure(Structure s) throws PrototypeInInvalidState {
		if(this.runtimeCopyInUse == null){
			throw new PrototypeInInvalidState("runtimeCopyInUse is null");
		}
		if(this.runtimeCopyNotInUse != null){
			throw new PrototypeInInvalidState("runtimeCopyNotInUse is not null");
		}
		this.runtimeCopyNotInUse = this.runtimeCopyInUse;
		this.runtimeCopyInUse = null;
	}

	FunctionAttributes(String theName, Map<String, Integer> theIndex, Attribute<?>[] theOrderedIdentifiers) {
		super(theName, theIndex, theOrderedIdentifiers);
	}

	public void init(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws DuplicateIdentifierNameExeption, NullIdentifierException, ComplexWasInitializedExeption, DublicateComplexException {
		super.initImp(typeName, theOrderedIdentifiers);
		Complex.put(this);
		FunctionAttributes.instances.put(typeName, this);
	}

	public static Attribute<?> getAttribute(String name) {
		return FunctionAttributes.functions.get(name);
	}

	private static FunctionAttributes createImpl(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		if(theIndex != null && theOrderedIdentifiers != null){
			return new FunctionAttributes(theName, theIndex, theOrderedIdentifiers);
		}
		return null;
	}

	public static Complex create(final String theName) {
		Map<String, Integer> index = new HashMap<>();
		Attribute<?>[] idents = new Attribute<?>[0];
		return FunctionAttributes.createImpl(theName, index, idents);
	}

	public static Complex createInit(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws AbstractInterpreterException {
		if(theOrderedIdentifiers.length == 0){
			return null;
		}
		Map<String, Integer> theIndex = new HashMap<>();
		Complex.buildIndex(theIndex, theOrderedIdentifiers);
		// return new ComplexDataType(theIndex, theOrderedAttributeTypes);
		if(FunctionAttributes.instances.containsKey(typeName)){
			throw new DublicateComplexException(typeName);
		}
		FunctionAttributes re = FunctionAttributes.createImpl(typeName, theIndex, theOrderedIdentifiers);
		Complex.put(re);
		FunctionAttributes.instances.put(typeName, re);
		return re;
	}

	public static Attribute<?> createAttribute(String attributeName, String typeName, Map<String, Complex> unformedComplexTypes)
			throws AbstractInterpreterException {
		// ComplexType
		if(typeName == null || typeName.length() == 0){
			// TODO exception
			return null;
		}
		if(attributeName == null || attributeName.length() == 0){
			// TODO exception
			return null;
		}
		Complex c = Complex.getInstance(typeName);
		if(c == null){
			c = unformedComplexTypes.get(typeName);
			if(c == null){
				c = FunctionAttributes.create(typeName);
				unformedComplexTypes.put(typeName, c);
			}
		}
		Attribute<Structure> re = c.createComplexAttribute(attributeName);
		FunctionAttributes.functions.put(typeName, re);
		System.out.println("createAttributesValues " + attributeName + " " + typeName + " ==> " + re);
		return re;
	}

	public static void fillMissingPrototypeValues() throws PrototypeWasInitialized {
		for(FunctionAttributes cf : FunctionAttributes.instances.values()){
			if(cf.prototype == null){
				Structure s = cf.createValue(null);
				s.fillMissingValues();
				cf.setPrototype(s);
			}
		}
	}

	public <R> Value<R> getValue(String n, Class<R> clazz) throws ValueCastException {
		return this.prototype.getValue(n, clazz);
	}

}
