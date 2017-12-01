package com.conetex.contract.lang.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.ComplexWasInitializedExeption;
import com.conetex.contract.build.exceptionFunction.DublicateComplexException;
import com.conetex.contract.build.exceptionFunction.DuplicateIdentifierNameExeption;
import com.conetex.contract.build.exceptionFunction.NullIdentifierException;
import com.conetex.contract.build.exceptionFunction.PrototypeWasInitialized;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.PrototypeInInvalidState;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class TypeComplexOfFunction extends TypeComplex{

	private Structure prototype;

	private Structure runtimeCopyInUse;

	private Structure runtimeCopyNotInUse;

	private static final Map<String, Attribute<?>> functions = new HashMap<>();

	private static final Map<String, TypeComplexOfFunction> instances = new HashMap<>();

	public static Collection<TypeComplexOfFunction> getInstances() {
		return TypeComplexOfFunction.instances.values();
	}
	
	public static TypeComplexOfFunction getInstance(String typeName) {
		return instances.get(typeName);
	}

	public void setPrototype(Structure thePrototype) throws PrototypeWasInitialized {
		if(this.prototype != null){
			throw new PrototypeWasInitialized(this.getName());
		}
		this.prototype = thePrototype;
		Structure parent = this.prototype.getParent();
		TypeComplex complex = this.prototype.getComplex();
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

		TypeComplex complex = this.prototype.getComplex();
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

	private TypeComplexOfFunction(String theName, Map<String, Integer> theIndex, Attribute<?>[] theOrderedIdentifiers) {
		super(theName, theIndex, theOrderedIdentifiers);
	}

	public void init(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws DuplicateIdentifierNameExeption, NullIdentifierException, ComplexWasInitializedExeption, DublicateComplexException {
		super.initImp(typeName, theOrderedIdentifiers);
		TypeComplex.put(this);
		TypeComplexOfFunction.instances.put(typeName, this);
	}

	public static Attribute<?> getAttribute(String name) {
		return TypeComplexOfFunction.functions.get(name);
	}

	private static TypeComplexOfFunction createImpl(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		if(theIndex != null && theOrderedIdentifiers != null){
			return new TypeComplexOfFunction(theName, theIndex, theOrderedIdentifiers);
		}
		return null;
	}

	private static TypeComplex create(final String theName) {
		Map<String, Integer> index = new HashMap<>();
		Attribute<?>[] idents = new Attribute<?>[0];
		return TypeComplexOfFunction.createImpl(theName, index, idents);
	}

	public static TypeComplex createInit(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws AbstractInterpreterException {
		if(theOrderedIdentifiers.length == 0){
			return null;
		}
		Map<String, Integer> theIndex = new HashMap<>();
		TypeComplex.buildIndex(theIndex, theOrderedIdentifiers);
		// return new ComplexDataType(theIndex, theOrderedAttributeTypes);
		if(TypeComplexOfFunction.instances.containsKey(typeName)){
			throw new DublicateComplexException(typeName);
		}
		TypeComplexOfFunction re = TypeComplexOfFunction.createImpl(typeName, theIndex, theOrderedIdentifiers);
		TypeComplex.put(re);
		TypeComplexOfFunction.instances.put(typeName, re);
		return re;
	}

	public static void createAttributeFun(String attributeName, String typeName, Map<String, TypeComplex> unformedComplexTypes)
			throws AbstractInterpreterException {
		// ComplexType
		if(typeName == null || typeName.length() == 0){
			// TODO exception
			return;
		}
		if(attributeName == null || attributeName.length() == 0){
			// TODO exception
			return;
		}
		TypeComplex c = TypeComplex.getInstance(typeName);
		if(c == null){
			c = unformedComplexTypes.get(typeName);
			if(c == null){
				c = TypeComplexOfFunction.create(typeName);
				unformedComplexTypes.put(typeName, c);
			}
		}
		Attribute<Structure> re = c.createComplexAttribute(attributeName);
		TypeComplexOfFunction.functions.put(typeName, re);
		System.out.println("createAttributesValues " + attributeName + " " + typeName + " ==> " + re);
    }

	public static void fillMissingPrototypeValues() throws PrototypeWasInitialized {
		for(TypeComplexOfFunction cf : TypeComplexOfFunction.instances.values()){
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

	@Override
	public String getCommand() {
		return TypeComplexOfFunction.staticGetCommand();
	}
	
	public static String staticGetCommand() {
		return Symbols.comFunction();
	}
	
	public CodeNode createCodeNode(TypeComplex parent) {
		String name = super.name;
		
		List<CodeNode> children = new LinkedList<>();
		
		for(Attribute<?> a : super.orderedAttributes){
			children.add( a.persist(this) );
		}

		CodeNode cn = new CodeNode(parent, this.getCommand(), new String[] {name, name}, children);
		
		return cn;
	}
	
}
