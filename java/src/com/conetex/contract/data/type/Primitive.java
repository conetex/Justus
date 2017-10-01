package com.conetex.contract.data.type;

import com.conetex.contract.build.Cast;
import com.conetex.contract.build.Symbol;
import com.conetex.contract.build.exceptionLang.UnknownType;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Attribute.EmptyLabelException;
import com.conetex.contract.data.Attribute.NullLabelException;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.value.ASCII8;
import com.conetex.contract.data.value.Base64_256;
import com.conetex.contract.data.value.Bool;
import com.conetex.contract.data.value.Int;
import com.conetex.contract.data.value.Label;
import com.conetex.contract.data.value.Lng;
import com.conetex.contract.data.value.MailAddress64;
import com.conetex.contract.run.exceptionValue.Invalid;

public class Primitive<T> extends AbstractType<T> {

	public static Primitive<?>[] types = { new Primitive<>(Bool.class, Boolean.class, new PrimitiveValueFactory<Boolean>() {
		@Override
		public Bool createValueImp() {
			return new Bool();
		}
	}), new Primitive<>(Int.class, Integer.class, new PrimitiveValueFactory<Integer>() {
		@Override
		public Int createValueImp() {
			return new Int();
		}
	}), new Primitive<>(Lng.class, Long.class, new PrimitiveValueFactory<Long>() {
		@Override
		public Lng createValueImp() {
			return new Lng();
		}
	}), new Primitive<>(ASCII8.class, String.class, new PrimitiveValueFactory<String>() {
		@Override
		public ASCII8 createValueImp() {
			return new ASCII8();
		}
	}), new Primitive<>(Label.class, String.class, new PrimitiveValueFactory<String>() {
		@Override
		public Label createValueImp() {
			return new Label();
		}
	}), new Primitive<>(Base64_256.class, String.class, new PrimitiveValueFactory<String>() {
		@Override
		public Base64_256 createValueImp() {
			return new Base64_256();
		}
	}), new Primitive<>(MailAddress64.class, String.class, new PrimitiveValueFactory<String>() {
		@Override
		public MailAddress64 createValueImp() {
			return new MailAddress64();
		}
	})

	};

	private final Class<? extends Value<T>> valueImplementClass;

	private final Class<T> rawTypeClass;

	// private final Class<Value.Interface<T>> clazz;

	final PrimitiveValueFactory<T> factory;

	private static Primitive<?> getInstanceWild(String dataType) {

		Class<?> theClass = getValueImplementClass(dataType);
		Primitive<?> re = getInstanceWild(theClass);
		if (re != null) {
			return re;
		}
		return null;

	}
	
	private static Class<?> getValueImplementClass(String dataType) {

		Class<?> theClass = null;

		String className = Bool.class.getPackage().getName() + "." + dataType;
		try {
			theClass = Class.forName(className);
		}
		catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("can not find " + className);
			// e.printStackTrace();
			return null;
		}
		catch (NoClassDefFoundError e) {
			// TODO Auto-generated catch block
			System.err.println("can not find " + className);
			// e.printStackTrace();
			return null;
		}

		return theClass;

	}

	public static <W> Primitive<W> getInstance(Class<?> theClass, Class<W> rawType) throws AbstractTypException {
		for (int i = 0; i < types.length; i++) {
			if (types[i].getValueImplementClass() == theClass) {
				return Cast.<W>toTypedPrimitive(Primitive.types[i], rawType);
			}
		}
		return null;
	}

	public static <W> Primitive<W> getInstanceAtRunTime(Class<?> theClass, Class<W> rawType) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].getValueImplementClass() == theClass) {
				try {
					return Cast.<W>toTypedPrimitive(Primitive.types[i], rawType);
				}
				catch (AbstractTypException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static Primitive<?> getInstanceWild(Class<?> theClass) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].getValueImplementClass() == theClass) {
				return Primitive.types[i];
			}
		}
		return null;
	}

	public static Attribute<?> createAttribute(String attributeName, String typeName) {
		// SimpleType
		if (typeName == null || typeName.length() == 0) {
			// TODO exception
			return null;
		}
		if (attributeName == null || attributeName.length() == 0) {
			// TODO exception
			return null;
		}

		Primitive<?> simpleType = Primitive.getInstanceWild(typeName);
		if (simpleType == null) {
			System.err.println("unknown simple Type " + typeName);
			return null;
		}
		Label str = new Label();
		try {
			str.set(attributeName);
		}
		catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Attribute<?> re = null;
		try {
			re = simpleType.createAttribute(str);
		}
		catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

		System.out.println("Primitive.createIdentifier " + attributeName + " " + typeName + " ==> " + re);
		return re;
	}

	// private PrimitiveDataType(Class<Value.Interface<T>> theClass,
	// ValueFactory<T> theFactory){
	private Primitive(Class<? extends Value<T>> theImplementClass, Class<T> theRawTypeClass, PrimitiveValueFactory<T> theFactory) {
		this.valueImplementClass = theImplementClass;
		this.rawTypeClass = theRawTypeClass;
		this.factory = theFactory;
	}

	public String getName() {
		return Symbol.SIMPLE_TYPE_NS + this.getValueImplementClass().getSimpleName();
	}
	
	public Value<T> createValue() {
		return this.factory.createValueImp();
	}

	@Override
	public Attribute<T> createAttribute(Label theName) throws Attribute.NullLabelException, Attribute.EmptyLabelException {
		/*
		 * if(theName == null || theName.get() == null){ throw new
		 * Identifier.NullLabelException(); } if(theName.get().length() < 1){ throw new
		 * Identifier.EmptyLabelException(); } return Identifier.<T>create(theName,
		 * this);
		 */
		return AbstractType.<T>createIdentifier(theName, this);
	}

	@Override
	public Class<? extends Value<T>> getValueImplementClass() {
		return this.valueImplementClass;
	}

	public Class<T> getRawTypeClass() {
		return this.rawTypeClass;
	}

	@Override
	public Attribute<?> getSubAttribute(String aName) {
		return null;
	}

	public static Class<?> getRawTypeClass(Attribute<?> id) throws UnknownType {
		AbstractType<?> t = id.getType();
		Class<? extends Value<?>> clazzChild = t.getValueImplementClass();
		Primitive<?> pri = Primitive.getInstanceWild(clazzChild);
		if (pri == null) {
			// System.err.println("ERR: can not get type of '" + n.getValue() +
			// "'");
			throw new UnknownType(clazzChild.toString());
		}
		Class<?> rawType = pri.getRawTypeClass();
		return rawType;
	}

}
