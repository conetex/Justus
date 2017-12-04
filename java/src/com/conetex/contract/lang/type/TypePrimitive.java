package com.conetex.contract.lang.type;

import java.math.BigInteger;
import java.util.Map;
import java.util.TreeMap;

import com.conetex.contract.build.Cast;
import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.build.exceptionType.AbstractTypException;
import com.conetex.contract.lang.value.PrimitiveValueFactory;
import com.conetex.contract.lang.value.Value;
import com.conetex.contract.lang.value.implementation.Base64;
import com.conetex.contract.lang.value.implementation.BigInt;
import com.conetex.contract.lang.value.implementation.Bool;
import com.conetex.contract.lang.value.implementation.Int;
import com.conetex.contract.lang.value.implementation.Label;
import com.conetex.contract.lang.value.implementation.Lng;
import com.conetex.contract.lang.value.implementation.MailAddress;
import com.conetex.contract.lang.value.implementation.SizedASCII;
import com.conetex.contract.run.exceptionValue.Invalid;

public class TypePrimitive<T> extends Type<T>{

	private static final Map<String, TypePrimitive<?>> instances = new TreeMap<>();

	private static String createGetMaxSizeStr(String className, String valueImplementClassStr) {
		String maxSizeStr = valueImplementClassStr.substring(className.length());
		for(String affix : Symbols.PARAM_AFFIXES){
			maxSizeStr = maxSizeStr.replace(affix, "");
		}
		return maxSizeStr;
	}

	private static int createGetMaxSize(String maxSizeStr) {
		int maxSize = -1;
		try{
			maxSize = Integer.parseInt(maxSizeStr);
		}
		catch(NumberFormatException e){
			// TODO throw Exception
			return -1;
		}

		if(maxSize > 0){
			return maxSize;
		}
		else{
			// TODO throw Exception
			return 1;
		}
	}

	private static void createPut(String valImplClass, String unifiedName, TypePrimitive<?> re) {
		if(!(unifiedName.equals(valImplClass))){
			TypePrimitive.instances.put(unifiedName, re);
		}
		TypePrimitive.instances.put(valImplClass, re);
	}

	private static TypePrimitive<?> create(String valImplClass) {

		TypePrimitive<?> re = null;
		if(valImplClass.startsWith(Symbols.CLASS_SIZED_ASCII)){
			String maxSizeStr = TypePrimitive.createGetMaxSizeStr(Symbols.CLASS_SIZED_ASCII, valImplClass);
			String unifiedName = Symbols.CLASS_SIZED_ASCII + maxSizeStr;
			if((re = TypePrimitive.instances.get(unifiedName)) == null){
				final int maxSize = TypePrimitive.createGetMaxSize(maxSizeStr);
				re = new TypePrimitive<>(valImplClass, SizedASCII.class, String.class, new PrimitiveValueFactory<String>(){
					@Override
					public Value<String> createValueImp(CodeNode theNode) {
						// TODO new
						return new SizedASCII(theNode, maxSize);
					}
				});
			}
			TypePrimitive.createPut(valImplClass, unifiedName, re);
		}
		else if(valImplClass.startsWith(Symbols.CLASS_MAIL_ADDRESS)){
			String maxSizeStr = TypePrimitive.createGetMaxSizeStr(Symbols.CLASS_MAIL_ADDRESS, valImplClass);
			String unifiedName = Symbols.CLASS_MAIL_ADDRESS + maxSizeStr;
			if((re = TypePrimitive.instances.get(unifiedName)) == null){
				final int maxSize = TypePrimitive.createGetMaxSize(maxSizeStr);
				re = new TypePrimitive<>(valImplClass, MailAddress.class, String.class, new PrimitiveValueFactory<String>(){
					@Override
					public Value<String> createValueImp(CodeNode theNode) {
						// TODO new
						return new MailAddress(theNode, maxSize);
					}
				});
			}
			TypePrimitive.createPut(valImplClass, unifiedName, re);
		}
		else if(valImplClass.startsWith(Symbols.CLASS_BASE64)){
			String maxSizeStr = TypePrimitive.createGetMaxSizeStr(Symbols.CLASS_BASE64, valImplClass);
			String unifiedName = Symbols.CLASS_BASE64 + maxSizeStr;
			if((re = TypePrimitive.instances.get(unifiedName)) == null){
				final int maxSize = TypePrimitive.createGetMaxSize(maxSizeStr);
				re = new TypePrimitive<>(valImplClass, Base64.class, String.class, new PrimitiveValueFactory<String>(){
					@Override
					public Value<String> createValueImp(CodeNode theNode) {
						// TODO new
						return new Base64(theNode, maxSize);
					}
				});
			}
			TypePrimitive.createPut(valImplClass, unifiedName, re);
		}

		if(re == null){
			// TODO exception
			System.err.println("Problem ...");
		}
		return re;
	}

	public static void init() {
		// TODO new
		TypePrimitive.instances.put(Symbols.CLASS_BINT, new TypePrimitive<>(Symbols.CLASS_BINT, BigInt.class, BigInteger.class, BigInt::new));

		// TODO new
		TypePrimitive.instances.put(Symbols.CLASS_LNG, new TypePrimitive<>(Symbols.CLASS_LNG, Lng.class, Long.class, Lng::new));

		TypePrimitive.instances.put(Symbols.CLASS_INT, new TypePrimitive<>(Symbols.CLASS_INT, Int.class, Integer.class, new PrimitiveValueFactory<Integer>(){
			@Override
			public Int createValueImp(CodeNode theNode) {
				// TODO new
				return new Int(theNode);
			}
		}));

		TypePrimitive.instances.put(Symbols.CLASS_BOOL, new TypePrimitive<>(Symbols.CLASS_BOOL, Bool.class, Boolean.class, new PrimitiveValueFactory<Boolean>(){
			@Override
			public Bool createValueImp(CodeNode theNode) {
				// TODO new
				return new Bool(theNode);
			}
		}));
	}

	private final Class<? extends Value<T>> valueImplementClass;

	private final Class<T> rawTypeClass;

	private final String name;

	// private final Class<Value.Interface<T>> clazz;

	private final PrimitiveValueFactory<T> factory;

	public static <W> TypePrimitive<W> getInstanceAtRunTime(String valueImplementClassStr, Class<W> rawType) {
		try{
			return TypePrimitive.getInstance(valueImplementClassStr, rawType);
		}
		catch(AbstractTypException e){
			// TODO throw run-time-exception
			e.printStackTrace();
			return null;
		}
	}

	public static <W> TypePrimitive<W> getInstance(String valueImplementClassStr, Class<W> rawType) throws AbstractTypException {
		TypePrimitive<?> re = TypePrimitive.getInstanceWild(valueImplementClassStr);
		if(re != null){
			return Cast.toTypedPrimitive(re, rawType);
		}
		return null;
	}

	private static TypePrimitive<?> getInstanceWild(String valueImplementClassStr) {
		TypePrimitive<?> re = TypePrimitive.instances.get(valueImplementClassStr);
		if(re != null){
			return re;
		}
		return create(valueImplementClassStr);
		/*
		 * Class<?> valueImplementClass =
		 * getValueImplementClass(valueImplementClassStr); Primitive<?> reOld =
		 * getInstanceWild(valueImplementClass); if(reOld != null){ return
		 * reOld; } // TODO throw Exception return null;
		 */
	}

	public static Attribute<?> createAttribute(String attributeName, String typeName) throws EmptyLabelException, NullLabelException {
		// SimpleType
		if(typeName == null || typeName.length() == 0){
			// TODO exception
			return null;
		}
		if(attributeName == null || attributeName.length() == 0){
			// TODO exception
			return null;
		}

		TypePrimitive<?> simpleType = TypePrimitive.getInstanceWild(typeName);
		if(simpleType == null){
			System.err.println("unknown simple Type " + typeName);
			return null;
		}
		// TODO new
		Label str = new Label(null);
		try{
			str.set( Symbols.getSimpleName(attributeName) );// TODO MERGE // str.set(attributeName);
		}
		catch(Invalid e){
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
	private TypePrimitive(String theName, Class<? extends Value<T>> theImplementClass, Class<T> theRawTypeClass, PrimitiveValueFactory<T> theFactory) {
		this.valueImplementClass = theImplementClass;
		this.rawTypeClass = theRawTypeClass;
		this.factory = theFactory;
		this.name = theName;
	}

	public String getName() {
		return Symbols.litSimpleTypeNS() + this.name;//this.getValueImplementClass().getSimpleName();
	}

	public Value<T> createValue(CodeNode theNode) {
		return this.factory.createValueImp(theNode);
	}

	@Override
	public Attribute<T> createAttribute(Label theName) throws EmptyLabelException, NullLabelException {
		/*
		 * if(theName == null || theName.get() == null){ throw new
		 * Identifier.NullLabelException(); } if(theName.get().length() < 1){
		 * throw new Identifier.EmptyLabelException(); } return
		 * Identifier.<T>create(theName, this);
		 */
		return Type.createIdentifier(theName, this);
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
