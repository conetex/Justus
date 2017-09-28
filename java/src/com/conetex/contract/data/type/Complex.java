package com.conetex.contract.data.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Attribute.EmptyLabelException;
import com.conetex.contract.data.Attribute.NullLabelException;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.Label;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.Symbol;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;

public class Complex extends AbstractType<Structure> { // AbstractType<Value<?>[]>

	private static Map<String, Complex> instances = new HashMap<>();

	public static Complex getInstance(String typeName) {
		return instances.get(typeName);
	}

	public static void clearInstances() {
		instances.clear();
	}

	public static Set<String> getInstanceNames() {
		return instances.keySet();
	}

	private final Map<String, Integer> index;

	private Attribute<?>[] orderedAttributes; // TODO kann das
												// nicht doch final
												// werden?

	private String name;

	public String getName() {
		return this.name;
	}

	private static Complex createImpl(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		if (theIndex != null && theOrderedIdentifiers != null) {
			return new Complex(theName, theIndex, theOrderedIdentifiers);
		}
		return null;
	}

	public static Complex create(final String theName) {
		Map<String, Integer> index = new HashMap<>();
		Attribute<?>[] idents = new Attribute<?>[0];
		return Complex.createImpl(theName, index, idents);
	}

	public static Complex createInit(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws Attribute.DuplicateIdentifierNameExeption, Attribute.NullIdentifierException, DublicateComplexException {
		if (theOrderedIdentifiers.length == 0) {
			return null;
		}
		Map<String, Integer> theIndex = new HashMap<>();
		buildIndex(theIndex, theOrderedIdentifiers);
		// return new ComplexDataType(theIndex, theOrderedAttributeTypes);
		if (Complex.instances.containsKey(typeName)) {
			throw new DublicateComplexException(typeName);
		}
		Complex re = Complex.createImpl(typeName, theIndex, theOrderedIdentifiers);
		Complex.instances.put(typeName, re);
		return re;
	}

	private static void buildIndex(Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers)
			throws Attribute.DuplicateIdentifierNameExeption, Attribute.NullIdentifierException {
		for (int i = 0; i < theOrderedIdentifiers.length; i++) {
			if (theOrderedIdentifiers[i] == null) {
				throw new Attribute.NullIdentifierException();
			}
			String label = theOrderedIdentifiers[i].getLabel().get();
			if (theIndex.containsKey(label)) {
				throw new Attribute.DuplicateIdentifierNameExeption(label);
			}
			theIndex.put(label, Integer.valueOf(i));
		}
	}

	private Complex(final String theName, final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedIdentifiers) {
		this.name = theName;
		this.index = theIndex;
		this.orderedAttributes = theOrderedIdentifiers;
	}

	public void init(String typeName, final Attribute<?>[] theOrderedIdentifiers)
			throws Attribute.DuplicateIdentifierNameExeption, Attribute.NullIdentifierException, ComplexWasInitializedExeption, DublicateComplexException {
		if (this.index.size() > 0 || this.orderedAttributes.length > 0) {
			throw new ComplexWasInitializedExeption();
		}
		if (Complex.instances.containsKey(typeName)) {
			throw new DublicateComplexException(typeName);
		}
		this.orderedAttributes = theOrderedIdentifiers;
		buildIndex(this.index, theOrderedIdentifiers);
		Complex.instances.put(typeName, this);
	}

	public Structure _construct(Value<?>[] theValues, Structure theParent) {
		if (theValues.length == 0) {
			// TODO Exception
			return null;
		}
		if (theValues.length != this.orderedAttributes.length) {
			// TODO Exception
			return null;
		}
		// return new Structure(this, theValues);
		return Structure._create(this, theValues, theParent);
	}

	public int getAttributesSize() {
		return this.orderedAttributes.length;
	}

	public Structure _construct(String[] theValues, Structure theParent) {
		Value<?>[] vals = new Value<?>[this.orderedAttributes.length];
		try {
			for (int i = 0; i < this.orderedAttributes.length; i++) {
				Value<?> re = this.orderedAttributes[i].createValue(theParent);
				re.setConverted(theValues[i]);
				vals[i] = re;
			}
		}
		catch (Inconvertible | Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// return new Structure(this, vals);
		return Structure._create(this, vals, theParent);
	}

	public Structure _construct(List<String> theValues, Structure theParent) {
		if (theValues.size() == 0) {
			// TODO Exception
			return null;
		}
		if (theValues.size() != this.orderedAttributes.length) {
			// TODO Exception
			return null;
		}
		Value<?>[] vals = new Value<?>[this.orderedAttributes.length];
		try {
			for (int i = 0; i < this.orderedAttributes.length; i++) {
				Value<?> re = this.orderedAttributes[i].createValue(theParent);
				re.setConverted(theValues.get(i));
				vals[i] = re;
			}
		}
		catch (Inconvertible | Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		// return new Structure(this, vals);
		return Structure._create(this, vals, theParent);
	}

	public int getSubAttributeIndex(String aName) {
		Integer i = this.index.get(aName);
		if (i == null) {
			return -1;
		}
		return i.intValue();
	}

	@Override
	public Attribute<?> getSubAttribute(String aName) {
		Integer i = this.index.get(aName);
		if (i != null) {
			int iv = i.intValue();
			if (iv < 0 || iv >= this.orderedAttributes.length) {
				// TODO i < this.orderedAttributes.length darf auf keinen fall
				// vorkommen! hier
				// bitte schwere Exception werfen!
				return null;
			}
			return this.orderedAttributes[iv];// (Attribute<V>)
												// this.orderedAttributes[i];
		}
		else {

			String[] names = Structure.split(aName);
			if (names[0] != null) {
				if (names[1] != null) {

					i = this.index.get(names[0]);
					if (i != null) {
						int iv = i.intValue();
						if (iv < 0 || iv >= this.orderedAttributes.length) {
							// TODO i < this.orderedAttributes.length darf auf
							// keinen fall vorkommen! hier
							// bitte schwere Exception werfen!
							return null;
						}

						AbstractType<?> dt = this.orderedAttributes[iv].getType();
						if (dt != null) {
							return dt.getSubAttribute(names[1]);
						}

					}
				}
			}

		}

		return null;
	}

	public Attribute<Structure> createComplexAttribute(String aName) {
		// PrimitiveDataType<Structure> simpleType =
		// PrimitiveDataType.getInstance(
		// Value.Implementation.Struct.class.getSimpleName() );

		Label str = new Label();
		try {
			str.set(aName);
		}
		catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Attribute<Structure> attribute = null;
		try {
			attribute = this.createAttribute(str);
		}
		catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

		return attribute;

	}

	public static Attribute<?> createAttribute(String attributeName, String typeName, Map<String, Complex> unformedComplexTypes) {
		// ComplexType
		if (typeName == null || typeName.length() == 0) {
			// TODO exception
			return null;
		}
		if (attributeName == null || attributeName.length() == 0) {
			// TODO exception
			return null;
		}
		Complex c = Complex.getInstance(typeName);
		if (c == null) {
			c = unformedComplexTypes.get(typeName);
			if (c == null) {
				c = Complex.create(typeName);
				unformedComplexTypes.put(typeName, c);
			}
		}
		Attribute<Structure> re = c.createComplexAttribute(attributeName);
		System.out.println("createAttributesValues " + attributeName + " " + typeName + " ==> " + re);
		return re;
	}

	public Attribute<Value<?>[]> _createAttribute2(Label theName) throws NullLabelException, EmptyLabelException {
		return null;// AbstractType.<Value<?>[]>createAttribute(theName, this);
	}

	@Override
	public Attribute<Structure> createAttribute(Label theName) throws NullLabelException, EmptyLabelException {
		return AbstractType.createAttribute(theName, this);
	}

	public Class<? extends Value<Value<?>[]>> _getClazz2() {
		return null;// Structure.class;
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

	public static class ComplexWasInitializedExeption extends Exception {
		private static final long serialVersionUID = 1L;
	}

	public static class DublicateComplexException extends Exception {
		public DublicateComplexException(String name) {
			super(name);
		}

		private static final long serialVersionUID = 1L;
	}

	public static String[] splitRight(String aName) {
		String[] re = new String[2];
		if (aName == null) {
			return re;
		}
		int i = aName.lastIndexOf(Symbol.TYPE_SEPERATOR);
		if (i > -1 && i < aName.length()) {
			re[0] = aName.substring(0, i);
			if (i + Symbol.TYPE_SEPERATOR.length() < aName.length()) {
				re[1] = aName.substring(i + Symbol.TYPE_SEPERATOR.length());
			}
		}
		return re;
	}

}
