package org.conetex.prime2.contractProcessing2.data.type;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.Label;
import org.conetex.prime2.contractProcessing2.data.values.Base64_256;
import org.conetex.prime2.contractProcessing2.data.values.Bool;
import org.conetex.prime2.contractProcessing2.data.values.Int;
import org.conetex.prime2.contractProcessing2.data.values.ASCII8;
import org.conetex.prime2.contractProcessing2.data.values.Lng;
import org.conetex.prime2.contractProcessing2.data.values.MailAddress64;

	public class Primitive<T> extends AbstractType<T>{
		
		public static Primitive<?>[] types = 
			{   
				 // new PrimitiveDataType< Structure >  ( Struct.class  , new ValueFactory<Structure>()   { public Struct   createValueImp() { return new Struct()  ; } } )
				  
				  new Primitive< Boolean> ( Bool.class     , new ValueFactory<Boolean>() { public Bool      createValueImp() { return new Bool()     ; } } )	
			    , new Primitive< Integer> ( Int.class      , new ValueFactory<Integer>() { public Int       createValueImp() { return new Int()      ; } } )
				, new Primitive< Long   > ( Lng.class      , new ValueFactory<Long>()    { public Lng       createValueImp() { return new Lng()      ; } } )
				
				, new Primitive< String > ( ASCII8.class  , new ValueFactory<String>()  { public ASCII8   createValueImp() { return new ASCII8()  ; } } )
				, new Primitive< String > ( Label.class  , new ValueFactory<String>()  { public Label   createValueImp() { return new Label()  ; } } )
	/*
				, new PrimitiveDataType< String > ( ASCII12.class , new ValueFactory<String>()  { public ASCII12  createValueImp() { return new ASCII12() ; } } )
				, new PrimitiveDataType< String > ( ASCII16.class , new ValueFactory<String>()  { public ASCII16  createValueImp() { return new ASCII16() ; } } )
				, new PrimitiveDataType< String > ( ASCII32.class , new ValueFactory<String>()  { public ASCII32  createValueImp() { return new ASCII32() ; } } )
				, new PrimitiveDataType< String > ( ASCII64.class , new ValueFactory<String>()  { public ASCII64  createValueImp() { return new ASCII64() ; } } )
				, new PrimitiveDataType< String > ( ASCII128.class, new ValueFactory<String>()  { public ASCII128 createValueImp() { return new ASCII128(); } } )
				, new PrimitiveDataType< String > ( ASCII256.class, new ValueFactory<String>()  { public ASCII256 createValueImp() { return new ASCII256(); } } )
	*/
				, new Primitive< String > ( Base64_256.class, new ValueFactory<String>()  { public Base64_256 createValueImp() { return new Base64_256(); } } )
	/*
				, new PrimitiveDataType< String > ( Base64_128.class, new ValueFactory<String>()  { public Base64_128 createValueImp() { return new Base64_128(); } } )
				, new PrimitiveDataType< String > ( Base64_64.class , new ValueFactory<String>()  { public Base64_64  createValueImp() { return new Base64_64() ; } } )
	*/			
				, new Primitive< String > ( MailAddress64.class , new ValueFactory<String>()  { public MailAddress64  createValueImp() { return new MailAddress64() ; } } )
	/*
				, new PrimitiveDataType< String > ( MailAddress128.class, new ValueFactory<String>()  { public MailAddress128 createValueImp() { return new MailAddress128(); } } )
				, new PrimitiveDataType< String > ( MailAddress254.class, new ValueFactory<String>()  { public MailAddress254 createValueImp() { return new MailAddress254(); } } )
	*/			
			};			
		
		
		private final Class<? extends Value<T>> clazz;
		//private final Class<Value.Interface<T>> clazz;
		
		final ValueFactory<T> factory;
				
		@SuppressWarnings("unchecked")
		public static <T> Primitive<T> getInstance(String dataType){
			
			Class<?> theClass;
			String className = "org.conetex.prime2.contractProcessing2.data.Values." + dataType;//TODO: klappt das
			try {
				theClass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("can not find " + className);
				//e.printStackTrace();
				return null;
			}
			return getInstance( (Class<Value<T>>)theClass );
			
		}
		
		@SuppressWarnings("unchecked")
		public static <T> Class<Value<T>> getClass(String dataType){
			
			Class<?> theClass;
			//String className = Imp.class.getName() + "$" + dataType;
			String className = "org.conetex.prime2.contractProcessing2.data.Values." + dataType;//TODO: klappt das
			try {
				theClass = Class.forName(className);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				System.err.println("can not find " + className);
				//e.printStackTrace();
				return null;
			}
			return (Class<Value<T>>)theClass;
			
		}			
		
		@SuppressWarnings("unchecked")
		public static <V> Primitive<V> getInstance(Class<? extends Value<V>> theClass){
			for (int i = 0; i < types.length; i++){
				if(types[i].getClazz() == theClass){
					return (Primitive<V>)Primitive.types[i];
				}
			}
			return null;
		} 		
		
		//private PrimitiveDataType(Class<Value.Interface<T>> theClass, ValueFactory<T> theFactory){
		private Primitive(Class<? extends Value<T>> theClass, ValueFactory<T> theFactory){
			this.clazz = theClass;
			this.factory = theFactory;
		}
		
		@Override
		public Value<T> createValue() {
			return this.factory.createValueImp();
		}
		
		@Override
		public Identifier<T> createIdentifier(Label theName) throws Identifier.NullLabelException, Identifier.EmptyLabelException {
			/*
			if(theName == null || theName.get() == null){
				throw new Identifier.NullLabelException();
			}
			if(theName.get().length() < 1){
				throw new Identifier.EmptyLabelException();
			}
			return Identifier.<T>create(theName, this);
			*/
			return AbstractType.<T>createIdentifier(theName, this);
		}

		@Override
		public Class<? extends Value<T>> getClazz() {
			return this.clazz;
		}
		
		@SuppressWarnings("unused")
		private String getClazzName() {
			return this.clazz.getName();
		}

		@Override
		public <U> Identifier<U> getSubIdentifier(String aName) {
			return null;
		}
		
	}
