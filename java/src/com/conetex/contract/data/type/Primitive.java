package com.conetex.contract.data.type;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.conetex.contract.build.Cast;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionLang.EmptyLabelException;
import com.conetex.contract.build.exceptionLang.NullLabelException;
import com.conetex.contract.build.exceptionLang.UnknownType;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.value.ASCII8;
import com.conetex.contract.data.value.Base64_256;
import com.conetex.contract.data.value.BigInt;
import com.conetex.contract.data.value.Bool;
import com.conetex.contract.data.value.Int;
import com.conetex.contract.data.value.Label;
import com.conetex.contract.data.value.Lng;
import com.conetex.contract.data.value.MailAddress;
import com.conetex.contract.data.value.MailAddress64;
import com.conetex.contract.run.exceptionValue.Invalid;

public class Primitive<T> extends AbstractType<T> {

	public static Map<String, Primitive<?>> instances = new HashMap<String, Primitive<?>>();

	private static String extractMaxSizeTypParamS(String className, String valueImplementClassStr) {
		String maxSizeStr = valueImplementClassStr.substring(className.length());
		for (String affix : Symbols.PARAM_AFFIXES) {
			maxSizeStr = maxSizeStr.replace(affix, "");
		}
		return maxSizeStr;
	}

	private static int extractMaxSizeTypParam(String maxSizeStr) {
		int maxSize = -1;
		try {
			maxSize = Integer.parseInt(maxSizeStr);
		}
		catch (NumberFormatException e) {
			// TODO throw Exception
			return -1;
		}

		if (maxSize > 0) {
			return maxSize;
		}
		else {
			// TODO throw Exception
			return 1;
		}
	}

	private static <T> Primitive<?> createInstanceX(String className, String valueImplementClassStr, String unifiedName, Class<? extends Value<T>> ValueClass,
			Class<T> baseTypeClass, PrimitiveValueFactory<T> theFactory) {

		Primitive<?> re = Primitive.instances.get(unifiedName);
		if (re == null) {
			re = new Primitive<>(ValueClass, baseTypeClass, theFactory);
			if (!(unifiedName.equals(valueImplementClassStr))) {
				Primitive.instances.put(unifiedName, re);
			}
		}
		Primitive.instances.put(valueImplementClassStr, re);
		return re;
	}

	private static Primitive<?> createInstance(String valImplClass) {

		if (valImplClass.startsWith(Symbols.CLASS_MAIL_ADDRESS)) {
			String maxSizeStr = extractMaxSizeTypParamS(Symbols.CLASS_MAIL_ADDRESS, valImplClass);
			int maxSize = extractMaxSizeTypParam(maxSizeStr);
			return createInstanceX(Symbols.CLASS_MAIL_ADDRESS, valImplClass, Symbols.CLASS_MAIL_ADDRESS + maxSizeStr, MailAddress.class, String.class,
					new PrimitiveValueFactory<String>() {
						@Override
						public Value<String> createValueImp() {
							return new MailAddress(maxSize);
						}
					});
		}
		else if (valueImplementClassStr.startsWith(Symbols.CLASS_SIZED_ASCII)) {
			String maxSizeStr = valueImplementClassStr.substring(Symbols.CLASS_SIZED_ASCII.length());
			maxSizeStr = maxSizeStr.replace("(", "");
		}

		return null;
	}

	public static void init() {
		Primitive.instances.put(Symbols.CLASS_BINT, new Primitive<>(BigInt.class, BigInteger.class, new PrimitiveValueFactory<BigInteger>() {
			@Override
			public Value<BigInteger> createValueImp() {
				return new BigInt();
			}
		}));

		Primitive.instances.put(Symbols.CLASS_LNG, new Primitive<>(Lng.class, Long.class, new PrimitiveValueFactory<Long>() {
			@Override
			public Lng createValueImp() {
				return new Lng();
			}
		}));

		Primitive.instances.put(Symbols.CLASS_INT, new Primitive<>(Int.class, Integer.class, new PrimitiveValueFactory<Integer>() {
			@Override
			public Int createValueImp() {
				return new Int();
			}
		}));

		Primitive.instances.put(Symbols.CLASS_BOOL, new Primitive<>(Bool.class, Boolean.class, new PrimitiveValueFactory<Boolean>() {
			@Override
			public Bool createValueImp() {
				return new Bool();
			}
		}));
	}

	private static Primitive<?>[]			types	= { new Primitive<>(ASCII8.class, String.class, new PrimitiveValueFactory<String>() {
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
											})

	};

	private final Class<? extends Value<T>>	valueImplementClass;

	private final Class<T>					rawTypeClass;

	// private final Class<Value.Interface<T>> clazz;

	final PrimitiveValueFactory<T>			factory;

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

	public static <W> Primitive<W> getInstanceAtRunTime(String valueImplementClassStr, Class<W> rawType) {
		try {
			return Primitive.getInstance(valueImplementClassStr, rawType);
		}
		catch (AbstractTypException e) {
			// TODO throw run-time-exception
			e.printStackTrace();
			return null;
		}
	}

	public static <W> Primitive<W> getInstance(String valueImplementClassStr, Class<W> rawType) throws AbstractTypException {
		Primitive<?> re = Primitive.getInstanceWild(valueImplementClassStr);
		if (re != null) {
			return Cast.<W>toTypedPrimitive(re, rawType);
		}
		return null;
	}

	private static Primitive<?> getInstanceWild(String valueImplementClassStr) {
		Primitive<?> re = Primitive.instances.get(valueImplementClassStr);
		if (re != null) {
			return re;
		}
		return createInstance(valueImplementClassStr);
		/*
		 * Class<?> valueImplementClass =
		 * getValueImplementClass(valueImplementClassStr); Primitive<?> reOld =
		 * getInstanceWild(valueImplementClass); if(reOld != null){ return
		 * reOld; } // TODO throw Exception return null;
		 */
	}

	private static Primitive<?> _getInstanceWild(Class<?> theClass) {
		for (int i = 0; i < types.length; i++) {
			if (types[i].getValueImplementClass() == theClass) {
				return Primitive.types[i];
			}
		}
		// TODO throw Exception
		return null;
	}

	public static Attribute<?> createAttribute(String attributeName, String typeName) throws EmptyLabelException, NullLabelException {
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
		re = simpleType.createAttribute(str);

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
		return Symbols.litSimpleTypeNS() + this.getValueImplementClass().getSimpleName();
	}

	public Value<T> createValue() {
		return this.factory.createValueImp();
	}

	@Override
	public Attribute<T> createAttribute(Label theName) throws EmptyLabelException, NullLabelException {
		/*
		 * if(theName == null || theName.get() == null){ throw new
		 * Identifier.NullLabelException(); } if(theName.get().length() < 1){
		 * throw new Identifier.EmptyLabelException(); } return
		 * Identifier.<T>create(theName, this);
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

}
