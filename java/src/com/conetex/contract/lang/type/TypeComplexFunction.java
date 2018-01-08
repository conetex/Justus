package com.conetex.contract.lang.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.conetex.contract.build.BuildTypes.TypeComplexTemp;
import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.ComplexWasInitializedExeption;
import com.conetex.contract.build.exceptionFunction.DublicateComplexException;
import com.conetex.contract.build.exceptionFunction.DuplicateIdentifierNameExeption;
import com.conetex.contract.build.exceptionFunction.NullIdentifierException;
import com.conetex.contract.build.exceptionFunction.PrototypeWasInitialized;
import com.conetex.contract.lang.function.Accessible;
import com.conetex.contract.lang.function.control.Function;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.PrototypeInInvalidState;
import com.conetex.contract.run.exceptionValue.ValueCastException;

public class TypeComplexFunction extends TypeComplex {

	private Structure										prototype;

	private Structure										runtimeCopyInUse;

	private Structure										runtimeCopyNotInUse;

	private static final Map<String, Attribute<?>>			functions	= new HashMap<>();

	private static final Map<String, TypeComplexFunction>	instances	= new HashMap<>();

	public static void clearInstances() {
		TypeComplexFunction.functions.clear();
		TypeComplexFunction.instances.clear();
	}
	
	public static Collection<TypeComplexFunction> getInstances() {
		return TypeComplexFunction.instances.values();
	}

	public static TypeComplexFunction getInstance(String typeName) {
		return instances.get(typeName);
	}

	public void setPrototype(Structure thePrototype) throws PrototypeWasInitialized {
		if (this.prototype != null) {
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
		if (this.prototype == null) {
			throw new PrototypeInInvalidState("prototype is null");
		}
		if (this.runtimeCopyInUse != null) {
			throw new PrototypeInInvalidState("runtimeCopyInUse is not null");
		}
		if (this.runtimeCopyNotInUse == null) {
			throw new PrototypeInInvalidState("runtimeCopyNotInUse is null");
		}

		this.runtimeCopyInUse = this.runtimeCopyNotInUse;
		this.runtimeCopyNotInUse = null;

		TypeComplex complex = this.prototype.getComplex();
		for (String key : complex.getSubAttributeNames()) {
			Value<?> vOrg = this.prototype.getValue(key);
			Value<?> vNew = this.runtimeCopyInUse.getValue(key);
			vNew.setObject(vOrg.get());
		}

		this.runtimeCopyInUse.setParent(obj);
		return this.runtimeCopyInUse;
	}

	public void unutilizeStructure(Structure s) throws PrototypeInInvalidState {
		if (this.runtimeCopyInUse == null) {
			throw new PrototypeInInvalidState("runtimeCopyInUse is null");
		}
		if (this.runtimeCopyNotInUse != null) {
			throw new PrototypeInInvalidState("runtimeCopyNotInUse is not null");
		}
		this.runtimeCopyNotInUse = this.runtimeCopyInUse;
		this.runtimeCopyInUse = null;
	}

	private TypeComplexFunction(String theName, Map<String, Integer> theIndex, Attribute<?>[] theOrderedIdentifiers) {
		super(theName, theIndex, theOrderedIdentifiers);
	}

	@Override
	public TypeComplex init(String typeName, TypeComplex parent, final Attribute<?>[] theOrderedIdentifiers)
			throws DuplicateIdentifierNameExeption, NullIdentifierException, ComplexWasInitializedExeption, DublicateComplexException {
		// System.err.println("this method should not be called");
		super.initImp(typeName, theOrderedIdentifiers);
		TypeComplex.put(this);
		TypeComplexFunction.instances.put(typeName, this);
		return this;
	}

	public static Attribute<?> getAttribute(String name) {
		return TypeComplexFunction.functions.get(name);
	}

	private static TypeComplexFunction createImpl(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		if (theIndex != null && theOrderedIdentifiers != null) {
			return new TypeComplexFunction(theName, theIndex, theOrderedIdentifiers);
		}
		return null;
	}

	public static TypeComplex createInit(String typeName, final Attribute<?>[] theOrderedIdentifiers) throws AbstractInterpreterException {
		if (theOrderedIdentifiers.length == 0) {
			return null;
		}
		Map<String, Integer> theIndex = new HashMap<>();
		TypeComplex.buildIndex(theIndex, theOrderedIdentifiers);
		// return new ComplexDataType(theIndex, theOrderedAttributeTypes);
		if (TypeComplexFunction.instances.containsKey(typeName)) {
			throw new DublicateComplexException(typeName);
		}
		TypeComplexFunction re = TypeComplexFunction.createImpl(typeName, theIndex, theOrderedIdentifiers);
		TypeComplex.put(re);
		TypeComplexFunction.instances.put(typeName, re);
		return re;
	}

	public static void createAttributeFun(String attributeName, String typeName, Map<String, TypeComplexTemp> unformedComplexTypes)
			throws AbstractInterpreterException {
		Attribute<Structure> re = TypeComplex.createAttribute(attributeName, typeName, unformedComplexTypes);
		TypeComplexFunction.functions.put(typeName, re);
		System.out.println("createAttributesValues " + attributeName + " " + typeName + " ==> " + re);
	}

	public static void fillMissingPrototypeValues() throws PrototypeWasInitialized {
		for (TypeComplexFunction cf : TypeComplexFunction.instances.values()) {
			if (cf.prototype == null) {
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
		return TypeComplexFunction.staticGetCommand();
	}

	public static String staticGetCommand() {
		return Symbols.comFunction();
	}

	@Override
	public CodeNode createCodeNode(TypeComplex parent) {

		List<CodeNode> children = new LinkedList<>();

		for (Attribute<?> a : super.orderedAttributes) {
			children.add(a.persist(this));
		}

		Function<?> f = Function.getInstance(super.name);

		if (f != null) {
			for (Accessible<?> a : f.getSteps()) {
				CodeNode x = a.createCodeNode(this);
				if (x == null) {
					System.err.println("SHIT");
				}
				children.add(x);
			}
		}
		// else{
		// System.err.println("SHIT");
		// }

		CodeNode cn = new CodeNode(parent, this.getCommand(), new String[] { super.name, super.name }, children);

		return cn;
	}

}
