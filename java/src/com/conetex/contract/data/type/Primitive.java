package com.conetex.contract.data.type;

import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Attribute.EmptyLabelException;
import com.conetex.contract.data.Attribute.NullLabelException;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.valueImplement.ASCII8;
import com.conetex.contract.data.valueImplement.Base64_256;
import com.conetex.contract.data.valueImplement.Bool;
import com.conetex.contract.data.valueImplement.Int;
import com.conetex.contract.data.valueImplement.Label;
import com.conetex.contract.data.valueImplement.Lng;
import com.conetex.contract.data.valueImplement.MailAddress64;
import com.conetex.contract.data.valueImplement.exception.Invalid;

public class Primitive<T> extends AbstractType<T> {

	public static Primitive<?>[] types = {
			// new PrimitiveDataType< Structure > ( Struct.class , new
			// ValueFactory<Structure>() { public Struct createValueImp() {
			// return new Struct() ; } } )

			new Primitive<Boolean>(Bool.class, Boolean.class, new PrimitiveValueFactory<Boolean>() {
				@Override
				public Bool createValueImp() {
					return new Bool();
				}
			}), new Primitive<Integer>(Int.class, Integer.class, new PrimitiveValueFactory<Integer>() {
				@Override
				public Int createValueImp() {
					return new Int();
				}
			}), new Primitive<Long>(Lng.class, Long.class, new PrimitiveValueFactory<Long>() {
				@Override
				public Lng createValueImp() {
					return new Lng();
				}
			})

			, new Primitive<String>(ASCII8.class, String.class, new PrimitiveValueFactory<String>() {
				@Override
				public ASCII8 createValueImp() {
					return new ASCII8();
				}
			}), new Primitive<String>(Label.class, String.class, new PrimitiveValueFactory<String>() {
				@Override
				public Label createValueImp() {
					return new Label();
				}
			})
			/*
			 * , new PrimitiveDataType< String > ( ASCII12.class , new
			 * ValueFactory<String>() { public ASCII12 createValueImp() { return
			 * new ASCII12() ; } } ) , new PrimitiveDataType< String > (
			 * ASCII16.class , new ValueFactory<String>() { public ASCII16
			 * createValueImp() { return new ASCII16() ; } } ) , new
			 * PrimitiveDataType< String > ( ASCII32.class , new
			 * ValueFactory<String>() { public ASCII32 createValueImp() { return
			 * new ASCII32() ; } } ) , new PrimitiveDataType< String > (
			 * ASCII64.class , new ValueFactory<String>() { public ASCII64
			 * createValueImp() { return new ASCII64() ; } } ) , new
			 * PrimitiveDataType< String > ( ASCII128.class, new
			 * ValueFactory<String>() { public ASCII128 createValueImp() {
			 * return new ASCII128(); } } ) , new PrimitiveDataType< String > (
			 * ASCII256.class, new ValueFactory<String>() { public ASCII256
			 * createValueImp() { return new ASCII256(); } } )
			 */
			, new Primitive<String>(Base64_256.class, String.class, new PrimitiveValueFactory<String>() {
				@Override
				public Base64_256 createValueImp() {
					return new Base64_256();
				}
			})
			/*
			 * , new PrimitiveDataType< String > ( Base64_128.class, new
			 * ValueFactory<String>() { public Base64_128 createValueImp() {
			 * return new Base64_128(); } } ) , new PrimitiveDataType< String >
			 * ( Base64_64.class , new ValueFactory<String>() { public Base64_64
			 * createValueImp() { return new Base64_64() ; } } )
			 */
			, new Primitive<String>(MailAddress64.class, String.class, new PrimitiveValueFactory<String>() {
				@Override
				public MailAddress64 createValueImp() {
					return new MailAddress64();
				}
			})
			/*
			 * , new PrimitiveDataType< String > ( MailAddress128.class, new
			 * ValueFactory<String>() { public MailAddress128 createValueImp() {
			 * return new MailAddress128(); } } ) , new PrimitiveDataType<
			 * String > ( MailAddress254.class, new ValueFactory<String>() {
			 * public MailAddress254 createValueImp() { return new
			 * MailAddress254(); } } )
			 */
	};

	private final Class<? extends Value<T>> clazz;

	private final Class<T> baseType;

	// private final Class<Value.Interface<T>> clazz;

	final PrimitiveValueFactory<T> factory;

	public static <V> Primitive<V> getInstance(String dataType) {

		Class<?> theClass = getClass(dataType);
		Primitive<V> re = getInstance(theClass);
		if (re != null) {
			return re;
		}
		return null;

	}

	public static Class<?> getClass(String dataType) {

		Class<?> theClass = null;

		String className = Bool.class.getPackage().getName() + "." + dataType;
		try {
			theClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			System.err.println("can not find " + className);
			// e.printStackTrace();
			return null;
		} catch (NoClassDefFoundError e) {
			// TODO Auto-generated catch block
			System.err.println("can not find " + className);
			// e.printStackTrace();
			return null;
		}

		return theClass;

	}

	public static <W> Primitive<W> getInstance(Class<?> theClass) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].getClazz() == theClass) {
				// TODO typ check !!!
				return (Primitive<W>) Primitive.types[i];
			}
		}
		return null;
	}

	public static <T> Attribute<T> createAttribute(String attributeName, String typeName) {
		// SimpleType
		if (typeName == null || typeName.length() == 0) {
			// TODO exception
			return null;
		}
		if (attributeName == null || attributeName.length() == 0) {
			// TODO exception
			return null;
		}

		Primitive<T> simpleType = Primitive.<T> getInstance(typeName);
		if (simpleType == null) {
			System.err.println("unknown simple Type " + typeName);
			return null;
		}
		Label str = new Label();
		try {
			str.set(attributeName);
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Attribute<T> re = null;
		try {
			re = simpleType.createAttribute(str);
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}

		System.out.println("Primitive.createIdentifier " + attributeName + " " + typeName + " ==> " + re);
		return re;
	}

	// private PrimitiveDataType(Class<Value.Interface<T>> theClass,
	// ValueFactory<T> theFactory){
	private Primitive(Class<? extends Value<T>> theClass, Class<T> theBaseType, PrimitiveValueFactory<T> theFactory) {
		this.clazz = theClass;
		this.baseType = theBaseType;
		this.factory = theFactory;
	}

	public Value<T> createValue() {
		return this.factory.createValueImp();
	}

	@Override
	public Attribute<T> createAttribute(Label theName)
			throws Attribute.NullLabelException, Attribute.EmptyLabelException {
		/*
		 * if(theName == null || theName.get() == null){ throw new
		 * Identifier.NullLabelException(); } if(theName.get().length() < 1){
		 * throw new Identifier.EmptyLabelException(); } return
		 * Identifier.<T>create(theName, this);
		 */
		return AbstractType.<T> createIdentifier(theName, this);
	}

	@Override
	public Class<? extends Value<T>> getClazz() {
		return this.clazz;
	}

	@SuppressWarnings("unused")
	private String getClazzName() {
		return this.clazz.getName();
	}

	public Class<T> getBaseType() {
		return this.baseType;
	}

	@Override
	public <U> Attribute<U> getSubAttribute(String aName) {
		return null;
	}

}
