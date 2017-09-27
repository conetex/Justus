package com.conetex.justus.study_contractProcessing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.conetex.justus.study_contractProcessing.Types.ASCII12;
import com.conetex.justus.study_contractProcessing.Types.ASCII128;
import com.conetex.justus.study_contractProcessing.Types.ASCII16;
import com.conetex.justus.study_contractProcessing.Types.ASCII256;
import com.conetex.justus.study_contractProcessing.Types.ASCII32;
import com.conetex.justus.study_contractProcessing.Types.ASCII64;
import com.conetex.justus.study_contractProcessing.Types.ASCII8;
import com.conetex.justus.study_contractProcessing.Types.Base64_128;
import com.conetex.justus.study_contractProcessing.Types.Base64_256;
import com.conetex.justus.study_contractProcessing.Types.Base64_64;
import com.conetex.justus.study_contractProcessing.Types.Bool;
import com.conetex.justus.study_contractProcessing.Types.Complex;
import com.conetex.justus.study_contractProcessing.Types.Int;
import com.conetex.justus.study_contractProcessing.Types.Lng;
import com.conetex.justus.study_contractProcessing.Types.MailAddress;
import com.conetex.justus.study_contractProcessing.Types.MailAddress128;
import com.conetex.justus.study_contractProcessing.Types.MailAddress254;
import com.conetex.justus.study_contractProcessing.Types.MailAddress64;

// TODO: Names 4 ComplexDataTyp. This is 4 strong typing
// TODO: parsing Schemas / dtd to define the types
// TODO: Array-Typs ...
// TODO: length of SimpleDat encoded in Typ-Name ...

public class State {

	public static void main(String[] args) throws ValueException {
		PrimitiveDataType<Base64_256, String> simpleType = PrimitiveDataType.getInstance(Base64_256.class);
		simpleType = new PrimitiveDataType<>(Base64_256.class, new ValueFactory<String>() {
			@Override
			public Base64_256 createValueImp() {
				return new Base64_256();
			}
		});

		ASCII8 str = new ASCII8();
		str.set("einStr");
		Attribute<String> attribute = null;
		try {
			attribute = simpleType.createAttribute(str);
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Attribute<?>[] theOrderedAttributes = { attribute };

		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ValueFactory<State> x = complexType;
		PrimitiveDataType<Complex, State>

		simpleTypeChild = new PrimitiveDataType<Complex, State>(Complex.class, x);
		// simpleTypeChild = new PrimitiveDataType< Complex , State> (
		// Complex.class ,
		// new ValueFactory<State>() { public Complex createValue() { return new
		// Complex() ; } } );
		// new PrimitiveDataType< ASCII8 , String> ( ASCII8.class , new
		// ValueFactory<String>(){ public ASCII8 createValue() { return new
		// ASCII8() ; }
		// } )
		Attribute<String> attributeChild = null;
		try {
			attributeChild = simpleType.createAttribute(str);
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Attribute<?>[] theOrderedAttributesChild = { attributeChild };
		ComplexDataType complexTypeParent = null;
		try {
			complexTypeParent = createComplexDataType(theOrderedAttributesChild);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		State state = complexTypeParent._createState();

		Value<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");

		System.out.println("ok");
	}

	public static void mainMail(String[] args) throws ValueException {
		PrimitiveDataType<MailAddress64, String> simpleType = PrimitiveDataType.getInstance(MailAddress64.class);
		simpleType = new PrimitiveDataType<MailAddress64, String>(MailAddress64.class, new ValueFactory<String>() {
			@Override
			public MailAddress64 createValueImp() {
				return new MailAddress64();
			}
		});

		ASCII8 str = new ASCII8();
		str.set("einName");
		Attribute<String> attribute = null;
		try {
			attribute = simpleType.createAttribute(str);
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Attribute<?>[] theOrderedAttributes = { attribute };

		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		State state = complexType._createState();

		Value<String> value = state.getValue("einName", MailAddress.class);
		try {
			value.set("matthias.franke@conetex.com");
			value.set("2@3.1");
			// value.set("2@3");
			// value.set("matthias.franke.conetex.com");
			value.set("2@3.de");
			// value.set("2ü@3.de");
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// System.out.println(attribute);
	}

	public static void main64(String[] args) throws ValueException {
		PrimitiveDataType<Base64_256, String> simpleType = PrimitiveDataType.getInstance(Base64_256.class);
		simpleType = new PrimitiveDataType<Base64_256, String>(Base64_256.class, new ValueFactory<String>() {
			@Override
			public Base64_256 createValueImp() {
				return new Base64_256();
			}
		});

		ASCII8 str = new ASCII8();
		str.set("einStr");
		Attribute<String> attribute = null;
		try {
			attribute = simpleType.createAttribute(str);
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Attribute<?>[] theOrderedAttributes = { attribute };

		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		State state = complexType._createState();

		Value<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");

		System.out.println("ok");
	}

	public static void mainInt(String[] args) throws ValueException {

		PrimitiveDataType<Int, Integer> simpleType = PrimitiveDataType.getInstance(Int.class);
		ASCII8 str = new ASCII8();
		str.set("ein Name");
		Attribute<Integer> attribute = null;
		try {
			attribute = simpleType.createAttribute(str);
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		Attribute<?>[] theOrderedAttributes = { attribute };

		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		State state = complexType._createState();

		Value<Integer> value = state.getValue("ein Name", Int.class);
		try {
			value.set(3);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// System.out.println(attribute);

	}

	private static PrimitiveDataType<?, ?>[] types = {
			new PrimitiveDataType<Complex, State>(Complex.class, new ValueFactory<State>() {
				@Override
				public Complex createValueImp() {
					return new Complex();
				}
			})

			, new PrimitiveDataType<Bool, Boolean>(Bool.class, new ValueFactory<Boolean>() {
				@Override
				public Bool createValueImp() {
					return new Bool();
				}
			}), new PrimitiveDataType<Int, Integer>(Int.class, new ValueFactory<Integer>() {
				@Override
				public Int createValueImp() {
					return new Int();
				}
			}), new PrimitiveDataType<Lng, Long>(Lng.class, new ValueFactory<Long>() {
				@Override
				public Lng createValueImp() {
					return new Lng();
				}
			})

			, new PrimitiveDataType<ASCII8, String>(ASCII8.class, new ValueFactory<String>() {
				@Override
				public ASCII8 createValueImp() {
					return new ASCII8();
				}
			}), new PrimitiveDataType<ASCII12, String>(ASCII12.class, new ValueFactory<String>() {
				@Override
				public ASCII12 createValueImp() {
					return new ASCII12();
				}
			}), new PrimitiveDataType<ASCII16, String>(ASCII16.class, new ValueFactory<String>() {
				@Override
				public ASCII16 createValueImp() {
					return new ASCII16();
				}
			}), new PrimitiveDataType<ASCII32, String>(ASCII32.class, new ValueFactory<String>() {
				@Override
				public ASCII32 createValueImp() {
					return new ASCII32();
				}
			}), new PrimitiveDataType<ASCII64, String>(ASCII64.class, new ValueFactory<String>() {
				@Override
				public ASCII64 createValueImp() {
					return new ASCII64();
				}
			}), new PrimitiveDataType<ASCII128, String>(ASCII128.class, new ValueFactory<String>() {
				@Override
				public ASCII128 createValueImp() {
					return new ASCII128();
				}
			}), new PrimitiveDataType<ASCII256, String>(ASCII256.class, new ValueFactory<String>() {
				@Override
				public ASCII256 createValueImp() {
					return new ASCII256();
				}
			})

			, new PrimitiveDataType<Base64_256, String>(Base64_256.class, new ValueFactory<String>() {
				@Override
				public Base64_256 createValueImp() {
					return new Base64_256();
				}
			}), new PrimitiveDataType<Base64_128, String>(Base64_128.class, new ValueFactory<String>() {
				@Override
				public Base64_128 createValueImp() {
					return new Base64_128();
				}
			}), new PrimitiveDataType<Base64_64, String>(Base64_64.class, new ValueFactory<String>() {
				@Override
				public Base64_64 createValueImp() {
					return new Base64_64();
				}
			})

			, new PrimitiveDataType<MailAddress64, String>(MailAddress64.class, new ValueFactory<String>() {
				@Override
				public MailAddress64 createValueImp() {
					return new MailAddress64();
				}
			}), new PrimitiveDataType<MailAddress128, String>(MailAddress128.class, new ValueFactory<String>() {
				@Override
				public MailAddress128 createValueImp() {
					return new MailAddress128();
				}
			}), new PrimitiveDataType<MailAddress254, String>(MailAddress254.class, new ValueFactory<String>() {
				@Override
				public MailAddress254 createValueImp() {
					return new MailAddress254();
				}
			}) };

	private final ComplexDataType type;

	private final Value<?>[] values;

	public static State _createState() {
		Attribute<?>[] theOrderedAttributes = {};
		ComplexDataType complexType = null;

		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}

		State state = complexType._createState();
		return state;
	}

	private State(final ComplexDataType theAttributeTuple, final Value<?>[] theValues) {
		this.type = theAttributeTuple;
		this.values = theValues;
	}

	public <V extends Value<?>> V getValue(String aName, Class<V> c) {
		return getValue(this.type.getAttributeIndex(aName), c);
	}

	@SuppressWarnings("unchecked")
	private <V extends Value<?>> V getValue(int i, Class<V> c) {
		Value<?> v = getValue(i);
		if (v != null) {
			return (V) v;
		}
		return null;
	}

	public Value<?> getValue(String aName) {
		return getValue(this.type.getAttributeIndex(aName));
	}

	private Value<?> getValue(int i) {
		if (i > -1 && i < this.values.length) {
			return this.values[i];
		}
		return null;
	}

	private static interface ValueFactory<T> {
		public Value<T> createValueImp();
	}

	public static class ValueException extends Exception {
		private static final long serialVersionUID = 1L;

		public ValueException(String msg) {
			super(msg);
		}
	}

	public static class ValueTransformException extends Exception {
		private static final long serialVersionUID = 1L;

		public ValueTransformException(String msg, Exception cause) {
			super(msg, cause);
		}

		public ValueTransformException(String msg) {
			super(msg);
		}
	}

	public static interface Value<T> {

		public abstract T get();

		public abstract void set(T value) throws ValueException;

		public abstract void transSet(String value) throws ValueTransformException, ValueException;

	}

	public static ComplexDataType createComplexDataType(final Attribute<?>[] theOrderedAttributeTypes)
			throws DuplicateAttributeNameExeption, NullAttributeException {
		if (theOrderedAttributeTypes.length == 0) {
			return null;
		}
		Map<String, Integer> theIndex = new HashMap<String, Integer>();
		for (int i = 0; i < theOrderedAttributeTypes.length; i++) {
			if (theOrderedAttributeTypes[i] == null) {
				throw new NullAttributeException();
			}
			String label = theOrderedAttributeTypes[i].getLabel().get();
			if (theIndex.containsKey(label)) {
				throw new DuplicateAttributeNameExeption();
			}
			theIndex.put(label, i);
		}
		return new ComplexDataType(theIndex, theOrderedAttributeTypes);
	}

	public static class ComplexDataType implements ValueFactory<State> {

		private final Map<String, Integer> index;

		private final Attribute<?>[] orderedAttributes;

		private ComplexDataType(final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedAttributeTypes) {
			this.index = theIndex;
			this.orderedAttributes = theOrderedAttributeTypes;
		}

		public State _createState() {
			Value<?>[] vals = new Value<?>[this.orderedAttributes.length];
			for (int i = 0; i < this.orderedAttributes.length; i++) {
				vals[i] = this.orderedAttributes[i].createValue();
			}
			return new State(this, vals);
		}

		public State createState(Value<?>[] theValues) {
			if (theValues.length == 0) {
				// TODO Exception
				return null;
			}
			if (theValues.length != this.orderedAttributes.length) {
				// TODO Exception
				return null;
			}
			return new State(this, theValues);
		}

		public State createState(String[] theValues) {
			Value<?>[] vals = new Value<?>[this.orderedAttributes.length];
			try {
				for (int i = 0; i < this.orderedAttributes.length; i++) {
					Value<?> re = this.orderedAttributes[i].createValue();
					re.transSet(theValues[i]);
					vals[i] = re;
				}
			} catch (ValueTransformException | ValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return new State(this, vals);
		}

		public State createState(List<String> theValues) {
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
					Value<?> re = this.orderedAttributes[i].createValue();
					re.transSet(theValues.get(i));
					vals[i] = re;
				}
			} catch (ValueTransformException | ValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return new State(this, vals);
		}

		public int getAttributeIndex(String aName) {
			Integer i = this.index.get(aName);
			if (i == null) {
				return -1;
			}
			return i;
		}

		@SuppressWarnings("unused")
		private Attribute<?> _test_getAttribute(String aName) {
			Integer i = this.index.get(aName);
			if (i == null) {
				return null;
			}
			return _test_getAttribute(i);
		}

		private Attribute<?> _test_getAttribute(int i) {
			if (i > -1 && i < this.orderedAttributes.length) {
				return this.orderedAttributes[i];
			}
			return null;
		}

		@Override
		public Value<State> createValueImp() {
			// here we go
			return new Complex();
		}

	}

	public static class Attribute<T> {

		private final ASCII8 label;

		// private final ValueFactory<T> factory;

		private final PrimitiveDataType<? extends Value<T>, T> type;

		/*
		 * private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
		 * this.label = theLabel; this.factory = theFactory; }
		 */
		private Attribute(ASCII8 theLabel, PrimitiveDataType<? extends Value<T>, T> theType) {
			this.label = theLabel;
			// this.factory = theFactory;
			this.type = theType;
		}

		public Value<T> createValue() {
			// return this.factory.createValueImp();
			return this.type.factory.createValueImp();
		}

		public ASCII8 getLabel() {
			return this.label;
		}

	}

	public static class PrimitiveDataType<V extends Value<T>, T> {

		private final Class<V> clazz;

		private final ValueFactory<T> factory;

		@SuppressWarnings("unchecked")
		public static <V extends Value<T>, T> PrimitiveDataType<V, T> getInstance(String dataType) {

			Class<?> theClass;
			try {
				theClass = Class.forName(Types.class.getName() + "$" + dataType);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return getInstance((Class<V>) theClass);

		}

		@SuppressWarnings("unchecked")
		public static <V extends Value<T>, T> PrimitiveDataType<V, T> getInstance(Class<V> theClass) {
			for (int i = 0; i < types.length; i++) {
				if (types[i].getClazz() == theClass) {
					return (PrimitiveDataType<V, T>) State.types[i];
				}
			}
			return null;
		}

		private PrimitiveDataType(Class<V> theClass, ValueFactory<T> theFactory) {
			this.clazz = theClass;
			this.factory = theFactory;
		}

		public Attribute<T> createAttribute(ASCII8 theName) throws NullLabelException, EmptyLabelException {
			if (theName == null || theName.get() == null) {
				throw new NullLabelException();
			}
			if (theName.get().length() < 1) {
				throw new EmptyLabelException();
			}
			return new Attribute<T>(theName, this);
		}

		private Class<V> getClazz() {
			return this.clazz;
		}

		@SuppressWarnings("unused")
		private String getClazzName() {
			return this.clazz.getName();
		}

	}

	public static class DuplicateAttributeNameExeption extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class NullAttributeException extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class NullLabelException extends Exception {
		private static final long serialVersionUID = 1L;

	}

	public static class EmptyLabelException extends Exception {
		private static final long serialVersionUID = 1L;

	}

}
