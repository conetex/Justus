package org.conetex.prime2.contractProcessing2.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.conetex.prime2.contractProcessing2.data.Value.Implementation.ASCII8;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Base64_256;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Bool;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Int;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Label;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Lng;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.MailAddress64;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Struct;
import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueFactory;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;

public class Type {

	private static PrimitiveDataType<?>[] types = 
		{   
			  new PrimitiveDataType< Structure >  ( Struct.class  , new ValueFactory<Structure>()   { public Struct   createValueImp() { return new Struct()  ; } } )
			  
			, new PrimitiveDataType< Boolean> ( Bool.class     , new ValueFactory<Boolean>() { public Bool      createValueImp() { return new Bool()     ; } } )	
		    , new PrimitiveDataType< Integer> ( Int.class      , new ValueFactory<Integer>() { public Int       createValueImp() { return new Int()      ; } } )
			, new PrimitiveDataType< Long   > ( Lng.class      , new ValueFactory<Long>()    { public Lng       createValueImp() { return new Lng()      ; } } )
			
			, new PrimitiveDataType< String > ( Label.class  , new ValueFactory<String>()  { public Label   createValueImp() { return new Label()  ; } } )
			, new PrimitiveDataType< String > ( ASCII8.class  , new ValueFactory<String>()  { public ASCII8   createValueImp() { return new ASCII8()  ; } } )
/*
			, new PrimitiveDataType< String > ( ASCII12.class , new ValueFactory<String>()  { public ASCII12  createValueImp() { return new ASCII12() ; } } )
			, new PrimitiveDataType< String > ( ASCII16.class , new ValueFactory<String>()  { public ASCII16  createValueImp() { return new ASCII16() ; } } )
			, new PrimitiveDataType< String > ( ASCII32.class , new ValueFactory<String>()  { public ASCII32  createValueImp() { return new ASCII32() ; } } )
			, new PrimitiveDataType< String > ( ASCII64.class , new ValueFactory<String>()  { public ASCII64  createValueImp() { return new ASCII64() ; } } )
			, new PrimitiveDataType< String > ( ASCII128.class, new ValueFactory<String>()  { public ASCII128 createValueImp() { return new ASCII128(); } } )
			, new PrimitiveDataType< String > ( ASCII256.class, new ValueFactory<String>()  { public ASCII256 createValueImp() { return new ASCII256(); } } )
*/
			, new PrimitiveDataType< String > ( Base64_256.class, new ValueFactory<String>()  { public Base64_256 createValueImp() { return new Base64_256(); } } )
/*
			, new PrimitiveDataType< String > ( Base64_128.class, new ValueFactory<String>()  { public Base64_128 createValueImp() { return new Base64_128(); } } )
			, new PrimitiveDataType< String > ( Base64_64.class , new ValueFactory<String>()  { public Base64_64  createValueImp() { return new Base64_64() ; } } )
*/			
			, new PrimitiveDataType< String > ( MailAddress64.class , new ValueFactory<String>()  { public MailAddress64  createValueImp() { return new MailAddress64() ; } } )
/*
			, new PrimitiveDataType< String > ( MailAddress128.class, new ValueFactory<String>()  { public MailAddress128 createValueImp() { return new MailAddress128(); } } )
			, new PrimitiveDataType< String > ( MailAddress254.class, new ValueFactory<String>()  { public MailAddress254 createValueImp() { return new MailAddress254(); } } )
*/			
		};	
	
		public static class PrimitiveDataType<T> {
					
			private final Class<? extends Value.Interface<T>> clazz;
			
			final ValueFactory<T> factory;
					
			@SuppressWarnings("unchecked")
			public static <T> PrimitiveDataType<T> getInstance(String dataType){
				
				Class<?> theClass;
				String className = Value.Implementation.class.getName() + "$" + dataType;
				try {
					theClass = Class.forName(className);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.err.println("can not find " + className);
					//e.printStackTrace();
					return null;
				}
				return getInstance( (Class<Value.Interface<T>>)theClass );
				
			}
			
			@SuppressWarnings("unchecked")
			public static <T> Class<Value.Interface<T>> getClass(String dataType){
				
				Class<?> theClass;
				String className = Value.Implementation.class.getName() + "$" + dataType;
				try {
					theClass = Class.forName(className);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					System.err.println("can not find " + className);
					//e.printStackTrace();
					return null;
				}
				return (Class<Value.Interface<T>>)theClass;
				
			}			
			
			
			@SuppressWarnings("unchecked")
			public static <T> PrimitiveDataType<T> getInstance(Class<? extends Value.Interface<T>> theClass){
				for (int i = 0; i < types.length; i++){
					if(types[i].getClazz() == theClass){
						return (PrimitiveDataType<T>)Type.types[i];
					}
				}
				return null;
			} 		
			
			private PrimitiveDataType(Class<? extends Value.Interface<T>> theClass, ValueFactory<T> theFactory){
				this.clazz = theClass;
				this.factory = theFactory;
			}
			
			public Identifier<T> createAttribute(ASCII8 theName) throws Identifier.NullLabelException, Identifier.EmptyLabelException {
				if(theName == null || theName.get() == null){
					throw new Identifier.NullLabelException();
				}
				if(theName.get().length() < 1){
					throw new Identifier.EmptyLabelException();
				}
				//return new Identifier<T>(theName, this);
				return Identifier.<T>create(theName, this);
			}
	
			private Class<? extends Value.Interface<T>> getClazz() {
				return this.clazz;
			}
			
			@SuppressWarnings("unused")
			private String getClazzName() {
				return this.clazz.getName();
			}
			
		}
		
		public static class ComplexDataType implements ValueFactory<Structure>{
			
			private final Map<String, Integer> index;
			
			private final Identifier<?>[] orderedAttributes;
			 
			public static ComplexDataType create(final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedAttributeTypes){
				if(theIndex != null && theOrderedAttributeTypes != null){
					return new ComplexDataType(theIndex, theOrderedAttributeTypes);
				}
				return null;
			}			
			
			public static ComplexDataType createComplexDataType(final Identifier<?>[] theOrderedAttributeTypes) throws Identifier.DuplicateAttributeNameExeption, Identifier.NullAttributeException{
				if(theOrderedAttributeTypes.length == 0){
					return null;
				}
				Map<String, Integer> theIndex = new HashMap<String, Integer>();
				for(int i = 0; i < theOrderedAttributeTypes.length; i++){
					if(theOrderedAttributeTypes[i] == null){
						throw new Identifier.NullAttributeException();
					}				
					String label = theOrderedAttributeTypes[i].getLabel().get();
					if(theIndex.containsKey(label)){
						throw new Identifier.DuplicateAttributeNameExeption();
					}
					theIndex.put(label, i);
				}
				//return new ComplexDataType(theIndex, theOrderedAttributeTypes);
				return ComplexDataType.create(theIndex, theOrderedAttributeTypes);
			}			
			
			private ComplexDataType(final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedAttributeTypes){
				this.index = theIndex;
				this.orderedAttributes = theOrderedAttributeTypes;			
			}
			
			
			
			public Structure _createState() {
				Value.Interface<?>[] vals = new Value.Interface<?>[ this.orderedAttributes.length ];
				for(int i = 0; i < this.orderedAttributes.length; i++){
					vals[i] = this.orderedAttributes[i].createValue();
				}
				//return new Structure(this, vals);
				return Structure.create(this, vals);
			}
			
			public Structure construct(Value.Interface<?>[] theValues) {
				if(theValues.length == 0){
					// TODO Exception
					return null;
				}			
				if(theValues.length != this.orderedAttributes.length){
					// TODO Exception
					return null;
				}		
				//return new Structure(this, theValues);
				return Structure.create(this, theValues);
			}
			
			public Structure construct(String[] theValues) {
				Value.Interface<?>[] vals = new Value.Interface<?>[ this.orderedAttributes.length ];
				try {
					for(int i = 0; i < this.orderedAttributes.length; i++){
						Value.Interface<?> re = this.orderedAttributes[i].createValue();
						re.transSet( theValues[i] );
						vals[i] = re;
					}
				} catch (ValueTransformException | ValueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}			
				//return new Structure(this, vals);
				return Structure.create(this, vals);
			}
			
			public Structure construct(List<String> theValues) {
				if(theValues.size() == 0){
					// TODO Exception
					return null;
				}			
				if(theValues.size() != this.orderedAttributes.length){
					// TODO Exception
					return null;
				}
				Value.Interface<?>[] vals = new Value.Interface<?>[ this.orderedAttributes.length ];
				try {
					for(int i = 0; i < this.orderedAttributes.length; i++){
						Value.Interface<?> re = this.orderedAttributes[i].createValue();
						re.transSet( theValues.get(i) );
						vals[i] = re;
					}
				} catch (ValueTransformException | ValueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}			
				//return new Structure(this, vals);
				return Structure.create(this, vals);
			}		
			
					
			public int getAttributeIndex(String aName){
				Integer i = this.index.get(aName);
				if(i == null){
					return -1;
				}			
				return i;
			}
			
			@SuppressWarnings("unused")
			private Identifier<?> _test_getAttribute(String aName){
				Integer i = this.index.get(aName);
				if(i == null){
					return null;
				}			
				return _test_getAttribute(i);
			}	
			
			private Identifier<?> _test_getAttribute(int i){
				if(i > -1 && i < this.orderedAttributes.length){
					return this.orderedAttributes[i];
				}			
				return null;
			}

			@Override
			public Value.Interface<Structure> createValueImp() {
				// here we go
				return new Struct(  );
			}	
			
		}		
		
	}