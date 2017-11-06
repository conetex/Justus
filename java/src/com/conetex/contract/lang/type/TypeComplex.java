package com.conetex.contract.lang.type;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.ComplexWasInitializedExeption;
import com.conetex.contract.build.exceptionFunction.DublicateComplexException;
import com.conetex.contract.build.exceptionFunction.DuplicateIdentifierNameExeption;
import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullIdentifierException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Label;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;

public class TypeComplex extends Type<Structure>{ // AbstractType<Value<?>[]>

	private static Map<String, TypeComplex> instances = new HashMap<>();

	public static TypeComplex getInstance(String typeName) {
		return instances.get(typeName);
	}

	public static void clearInstances() {
		instances.clear();
	}

	public static Set<String> getInstanceNames() {
		return instances.keySet();
	}

	public static Collection<TypeComplex> getInstances() {
		return instances.values();
	}

	private final Map<String, Integer> index;

	private Attribute<?>[] orderedAttributes; // TODO kann das
												// nicht doch final
												// werden?

	private String name;

	public String getName() {
		return this.name;
	}

	private static TypeComplex createImpl(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		if(theIndex != null && theOrderedIdentifiers != null){
			return new TypeComplex(theName, theIndex, theOrderedIdentifiers);
		}
		return null;
	}

	public static TypeComplex create(final String theName) {
		Map<String, Integer> index = new HashMap<>();
		Attribute<?>[] idents = new Attribute<?>[0];
		return TypeComplex.createImpl(theName, index, idents);
	}

	public static TypeComplex createInit(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws AbstractInterpreterException {
		if(theOrderedIdentifiers.length == 0){
			return null;
		}
		Map<String, Integer> theIndex = new HashMap<>();
		buildIndex(theIndex, theOrderedIdentifiers);
		// return new ComplexDataType(theIndex, theOrderedAttributeTypes);
		if(TypeComplex.instances.containsKey(typeName)){
			throw new DublicateComplexException(typeName);
		}
		TypeComplex re = TypeComplex.createImpl(typeName, theIndex, theOrderedIdentifiers);
		TypeComplex.instances.put(typeName, re);
		return re;
	}

	static TypeComplex put(TypeComplex re) throws DublicateComplexException {
		String typeName = re.name;
		if(TypeComplex.instances.containsKey(typeName)){
			throw new DublicateComplexException(typeName);
		}
		TypeComplex.instances.put(typeName, re);
		return re;
	}

	static void buildIndex(Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers)
			throws DuplicateIdentifierNameExeption, NullIdentifierException {
		for(int i = 0; i < theOrderedIdentifiers.length; i++){
			if(theOrderedIdentifiers[i] == null){
				throw new NullIdentifierException(TypeComplex.class.getName() + "buildIndex");
			}
			String label = theOrderedIdentifiers[i].getLabel().get();
			if(theIndex.containsKey(label)){
				throw new DuplicateIdentifierNameExeption(label);
			}
			theIndex.put(label, Integer.valueOf(i));
		}
	}

	TypeComplex(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		this.name = theName;
		this.index = theIndex;
		this.orderedAttributes = theOrderedIdentifiers;
	}

	void initImp(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws DuplicateIdentifierNameExeption, NullIdentifierException, ComplexWasInitializedExeption, DublicateComplexException {
		if(this.index.size() > 0 || this.orderedAttributes.length > 0){
			throw new ComplexWasInitializedExeption(this.name);
		}
		if(TypeComplex.instances.containsKey(typeName)){
			throw new DublicateComplexException(typeName);
		}
		this.orderedAttributes = theOrderedIdentifiers;
		buildIndex(this.index, theOrderedIdentifiers);

	}

	public void init(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws DuplicateIdentifierNameExeption, NullIdentifierException, ComplexWasInitializedExeption, DublicateComplexException {
		this.initImp(typeName, theOrderedIdentifiers);
		TypeComplex.instances.put(typeName, this);
	}

	public int getAttributesSize() {
		return this.orderedAttributes.length;
	}

	public Set<String> getSubAttributeNames() {
		return this.index.keySet();
	}

	public int getSubAttributeIndex(String aName) {
		Integer i = this.index.get(aName);
		if(i == null){
			return -1;
		}
		return i.intValue();
	}

	public Attribute<?> getSubAttribute(int i) {
		if(i < 0 || i >= this.orderedAttributes.length){
			// TODO i < this.orderedAttributes.length darf auf keinen fall
			// vorkommen! hier
			// bitte schwere Exception werfen!
			return null;
		}
		return this.orderedAttributes[i];
	}

	@Override
	public Attribute<?> getSubAttribute(String aName) {
		// und hier muss jetzt auch nach functionsstructuren gesucht werden
		System.out.println("complexname: " + this.name);

		Attribute<?> aFun = TypeComplexOfFunction.getAttribute(this.name + "." + aName);
		// Attribute<?> aFun1 = this._functions.get(aName);
		if(aFun != null){
			return aFun;
		}

		Integer i = this.index.get(aName);
		if(i != null){
			int iv = i.intValue();
			if(iv < 0 || iv >= this.orderedAttributes.length){
				// TODO i < this.orderedAttributes.length darf auf keinen fall
				// vorkommen! hier
				// bitte schwere Exception werfen!
				return null;
			}
			return this.orderedAttributes[iv];// (Attribute<V>)
												// this.orderedAttributes[i];
		}
		else{
			String[] names = Structure.split(aName);

			if(names[0] != null){
				if(names[1] != null){

					aFun = TypeComplexOfFunction.getAttribute(this.name + "." + names[0]);
					// Attribute<?> aFun2 = this._functions.get(names[0]);
					if(aFun != null){
						Type<?> dt = aFun.getType();
						if(dt != null){
							return dt.getSubAttribute(names[1]);
						}
					}

					i = this.index.get(names[0]);
					if(i != null){
						int iv = i.intValue();
						if(iv < 0 || iv >= this.orderedAttributes.length){
							// TODO i < this.orderedAttributes.length darf auf
							// keinen fall vorkommen! hier
							// bitte schwere Exception werfen!
							return null;
						}

						Type<?> dt = this.orderedAttributes[iv].getType();
						if(dt != null){
							return dt.getSubAttribute(names[1]);
						}

					}
				}
			}

		}

		return null;
	}

	public Attribute<Structure> createComplexAttribute(String aName) throws NullLabelException, EmptyLabelException {
		// PrimitiveDataType<Structure> simpleType =
		// PrimitiveDataType.getInstance(
		// Value.Implementation.Struct.class.getSimpleName() );

		//		// TODO new
		Label str = new Label(null);
		try{
			str.set(aName);
		}
		catch(Invalid e){
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Attribute<Structure> attribute = null;
		attribute = this.createAttribute(str);

		return attribute;

	}

	public static Attribute<?> createAttribute(String attributeName, String typeName, Map<String, TypeComplex> unformedComplexTypes)
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
		TypeComplex c = TypeComplex.getInstance(typeName);
		if(c == null){
			c = unformedComplexTypes.get(typeName);
			if(c == null){
				c = TypeComplex.create(typeName);
				unformedComplexTypes.put(typeName, c);
			}
		}
		Attribute<Structure> re = c.createComplexAttribute(attributeName);
		System.out.println("createAttributesValues " + attributeName + " " + typeName + " ==> " + re);
		return re;
	}

	@Override
	public Attribute<Structure> createAttribute(Label theName) throws NullLabelException, EmptyLabelException {
		return Type.createAttribute(theName, this);
	}

	@Override
	public Class<? extends Value<Structure>> getValueImplementClass() {
		return Structure.class;
	}

	// public Value<Value<?>[]> createValue() {
	// return Structure.create(this);
	// }
	public Structure createValue(Structure theParent) {//
		return Structure.create(this, theParent);// ,
	}

	public static class _ComplexWasInitializedExeption extends AbstractInterpreterException{
		public _ComplexWasInitializedExeption(String msg) {
			super(msg);
			// TODO Auto-generated constructor stub
		}

		private static final long serialVersionUID = 1L;
	}

	public static class _DublicateComplexException extends AbstractInterpreterException{
		public _DublicateComplexException(String name) {
			super(name);
		}

		private static final long serialVersionUID = 1L;
	}

	public static String[] splitRight(String aName) {
		String[] re = new String[2];
		if(aName == null){
			return re;
		}
		int i = aName.lastIndexOf(Symbols.litTypeSeperator());
		if(i > -1 && i < aName.length()){
			re[0] = aName.substring(0, i);
			if(i + Symbols.litTypeSeperator().length() < aName.length()){
				re[1] = aName.substring(i + Symbols.litTypeSeperator().length());
			}
		}
		return re;
	}

	@Override
	public Class<Structure> getRawTypeClass() {
		return Structure.class;
	}

}
