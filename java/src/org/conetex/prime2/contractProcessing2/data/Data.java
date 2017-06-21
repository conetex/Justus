package org.conetex.prime2.contractProcessing2.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.conetex.prime2.contractProcessing2.data.Data.Structure;
import org.conetex.prime2.contractProcessing2.data.Data.Value;
import org.conetex.prime2.contractProcessing2.data.Data.Type.*;
import org.conetex.prime2.contractProcessing2.data.Data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Data.Value.ValueFactory;
import org.conetex.prime2.contractProcessing2.data.Data.Value.ValueTransformException;
import org.conetex.prime2.contractProcessing2.data.Data.Value.Implementation.*;

// TODO: Names 4 ComplexDataTyp. This is 4 strong typing
// TODO: parsing Schemas / dtd to define the types
// TODO: Array-Typs ...
// TODO: length of SimpleDat encoded in Typ-Name ...


public class Data {

	public static void main(String[] args) throws ValueException{
		PrimitiveDataType<String> simpleType = PrimitiveDataType.getInstance(Base64_256.class);
		simpleType = new PrimitiveDataType< String   > ( Base64_256.class , new ValueFactory<String>(){	public Base64_256 createValueImp() { return new Base64_256();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einStr");
		Identifier<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ValueFactory<Structure> x = complexType;
		PrimitiveDataType<Structure> 
		
		simpleTypeChild = new PrimitiveDataType< Structure > ( Struct.class     , x );
		//simpleTypeChild = new PrimitiveDataType< Complex , State> ( Complex.class     , new ValueFactory<State>() { public Complex  createValue() { return new Complex()     ; } } );
		//                new PrimitiveDataType< ASCII8 , String> ( ASCII8.class      , new ValueFactory<String>(){ public ASCII8   createValue() { return new ASCII8()  ; } } )
		Identifier<String> attributeChild = null;
		try {
			attributeChild = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		Identifier<?>[] theOrderedAttributesChild = {attributeChild};
		ComplexDataType complexTypeParent = null;
		try {
			complexTypeParent = createComplexDataType(theOrderedAttributesChild);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		Structure state = complexTypeParent._createState();
		
		Value.Interface<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");
		
		System.out.println("ok");
	}	
	
	public static void mainMail(String[] args) throws ValueException {
		PrimitiveDataType<String> simpleType = PrimitiveDataType.getInstance(MailAddress64.class);
		simpleType = new PrimitiveDataType<  String   > ( MailAddress64.class , new Value.ValueFactory<String>(){	public MailAddress64 createValueImp() { return new MailAddress64();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einName");
		Identifier<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value.Interface<String> value = state.getValue("einName", MailAddress.class);
		try {
			value.set("matthias.franke@conetex.com");
			value.set("2@3.1");
			//value.set("2@3");
			//value.set("matthias.franke.conetex.com");
			value.set("2@3.de");
			//value.set("2ü@3.de");
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//System.out.println(attribute);
	}	
	
	public static void main64(String[] args) throws ValueException{
		PrimitiveDataType<String> simpleType = PrimitiveDataType.getInstance(Base64_256.class);
		simpleType = new PrimitiveDataType<   String   > ( Base64_256.class , new ValueFactory<String>(){	public Base64_256 createValueImp() { return new Base64_256();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einStr");
		Identifier<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value.Interface<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");
		
		System.out.println("ok");
	}	
	
	public static void mainInt(String[] args) throws ValueException{
		
		PrimitiveDataType<Integer> simpleType = PrimitiveDataType.getInstance(Int.class);
		ASCII8 str = new ASCII8();
		str.set("ein Name");
		Identifier<Integer> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (Identifier.NullLabelException | Identifier.EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Identifier<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (Identifier.DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Structure state = complexType._createState();
		
		Value.Interface<Integer> value = state.getValue("ein Name", Int.class);
		try {
			value.set(3);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(attribute);
		
	}

	private static PrimitiveDataType<?>[] types = 
		{   
			  new PrimitiveDataType< Structure >  ( Struct.class  , new ValueFactory<Structure>()   { public Struct   createValueImp() { return new Struct()  ; } } )
			  
			, new PrimitiveDataType< Boolean> ( Bool.class     , new ValueFactory<Boolean>() { public Bool      createValueImp() { return new Bool()     ; } } )	
		    , new PrimitiveDataType< Integer> ( Int.class      , new ValueFactory<Integer>() { public Int       createValueImp() { return new Int()      ; } } )
			, new PrimitiveDataType< Long   > ( Lng.class      , new ValueFactory<Long>()    { public Lng       createValueImp() { return new Lng()      ; } } )
			
			, new PrimitiveDataType< String > ( ASCII8.class  , new ValueFactory<String>()  { public ASCII8   createValueImp() { return new ASCII8()  ; } } )
			, new PrimitiveDataType< String > ( ASCII12.class , new ValueFactory<String>()  { public ASCII12  createValueImp() { return new ASCII12() ; } } )
			, new PrimitiveDataType< String > ( ASCII16.class , new ValueFactory<String>()  { public ASCII16  createValueImp() { return new ASCII16() ; } } )
			, new PrimitiveDataType< String > ( ASCII32.class , new ValueFactory<String>()  { public ASCII32  createValueImp() { return new ASCII32() ; } } )
			, new PrimitiveDataType< String > ( ASCII64.class , new ValueFactory<String>()  { public ASCII64  createValueImp() { return new ASCII64() ; } } )
			, new PrimitiveDataType< String > ( ASCII128.class, new ValueFactory<String>()  { public ASCII128 createValueImp() { return new ASCII128(); } } )
			, new PrimitiveDataType< String > ( ASCII256.class, new ValueFactory<String>()  { public ASCII256 createValueImp() { return new ASCII256(); } } )

			, new PrimitiveDataType< String > ( Base64_256.class, new ValueFactory<String>()  { public Base64_256 createValueImp() { return new Base64_256(); } } )
			, new PrimitiveDataType< String > ( Base64_128.class, new ValueFactory<String>()  { public Base64_128 createValueImp() { return new Base64_128(); } } )
			, new PrimitiveDataType< String > ( Base64_64.class , new ValueFactory<String>()  { public Base64_64  createValueImp() { return new Base64_64() ; } } )
			
			, new PrimitiveDataType< String > ( MailAddress64.class , new ValueFactory<String>()  { public MailAddress64  createValueImp() { return new MailAddress64() ; } } )
			, new PrimitiveDataType< String > ( MailAddress128.class, new ValueFactory<String>()  { public MailAddress128 createValueImp() { return new MailAddress128(); } } )
			, new PrimitiveDataType< String > ( MailAddress254.class, new ValueFactory<String>()  { public MailAddress254 createValueImp() { return new MailAddress254(); } } )
			
		};	
	
	public static class Structure {
		
		private final ComplexDataType type;
		
		private final Value.Interface<?>[] values;
		
		public static Structure _createState(){
			Identifier<?>[] theOrderedAttributes = {};
			ComplexDataType complexType = null;
			
			try {
				complexType = createComplexDataType(theOrderedAttributes);
			} catch (Identifier.DuplicateAttributeNameExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (Identifier.NullAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
			Structure state = complexType._createState();
			return state;
		}
	
		private Structure(final ComplexDataType theAttributeTuple, final Value.Interface<?>[] theValues){
			this.type = theAttributeTuple;
			this.values = theValues;		
		}	
		
		public <V extends Value.Interface<?>> V getValue (String aName, Class<V> c){
			return getValue( this.type.getAttributeIndex(aName), c );
		}
		
		@SuppressWarnings("unchecked")
		private <V extends Value.Interface<?>> V getValue (int i, Class<V> c){
			Value.Interface<?> v = getValue(i);
			if( v != null ){
				return (V) v;
			}
			return null;
		}
		
		public Value.Interface<?> getValue (String aName){
			return getValue( this.type.getAttributeIndex(aName) );
		}
		
		private Value.Interface<?> getValue (int i){
			if(i > -1 && i < this.values.length){
				return this.values[i];
			}
			return null;
		}
		
	}

	

	
	public static class Value{
		

		public static interface ValueFactory<T>{
			public Value.Interface<T> createValueImp();
		}
		
		public static class ValueException extends Exception {
			private static final long serialVersionUID = 1L;		
			public ValueException(String msg){
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
		
		public static interface Interface<T>{
			
			public abstract T get();
			
			public abstract void set(T value) throws ValueException;
	
			public abstract void transSet(String value) throws ValueTransformException, ValueException;
		}
		
		public static class Implementation{
		
			public static class Struct implements Value.Interface<Structure>{
	
				private Structure value;
				
				public void set(Structure aValue){
					this.value = aValue;			
				}
				
				public final Structure get(){
					return this.value;
				}
	
				@Override
				public void transSet(String value) throws ValueTransformException {
					throw new ValueTransformException("can not convert String to State");
				}
				
			}
			
			public static class Int implements Value.Interface<Integer>{
	
				private Integer value;
				
				@Override
				public void set(Integer aValue){
					this.value = aValue;			
				}
				
				@Override
				public final Integer get(){
					return this.value;
				}
				
				@Override
				public void transSet(String value) throws ValueTransformException {
					try {
						Integer v = Integer.parseInt(value);
						this.set(v);
					} catch (NumberFormatException e) {
						throw new ValueTransformException("can not convert " + value + " to Integer", e);
					}
				}		
				
			}
	
			public static class Lng implements Value.Interface<Long>{
	
				private Long value;
				
				@Override
				public void set(Long aValue){
					this.value = aValue;			
				}
				
				@Override
				public final Long get(){
					return this.value;
				}
	
				@Override
				public void transSet(String value) throws ValueTransformException, NumberFormatException {
					try {
						Long v = Long.parseLong(value);
						this.set(v);	
					} catch (NumberFormatException e) {
						throw new ValueTransformException("can not convert " + value + " to Long", e);
					}			
				}
				
			}
	
			public static class Bool implements Value.Interface<Boolean>{
				
				private Boolean value;
				
				@Override
				public void set(Boolean aValue){
					this.value = aValue;
				}
				
				@Override
				public final Boolean get(){
					return this.value;
				}
	
				@Override
				public void transSet(String value) throws ValueTransformException {
					if(value.equalsIgnoreCase("true")){
						this.set(Boolean.TRUE);
					}
					else if(value.equalsIgnoreCase("false")){
						this.set(Boolean.FALSE);
					}
					else if(value.equals("1")){
						this.set(Boolean.TRUE);
					}
					else if(value.equals("0")){
						this.set(Boolean.FALSE);
					}
					else {
						throw new ValueTransformException("can not convert '" + value + "' to Boolean!");
					}
				}		
				
			}
			
			public static abstract class SizedASCII implements Value.Interface<String>{
				
				private String value;
						
				protected boolean check(String aValue, String allowedChars) throws ValueException{
					if(aValue == null){
						return true;
					}
					
					if(aValue.length() > this.getMaxSize()){
						throw new ValueException("Input is longer than " + this.getMaxSize() + ": '" + aValue + "'");
					}
	
					// HINT: Take care adding allowed control chars. You have to adjust throwing Exceptions.
					//       The throwing Exceptions searches for control chars, to provide detailed Information to the user. 
					// ascii:
					//String chars = "\\p{ASCII}";
					
					// ascii without control characters but with \t (Tabulator):
					//String chars = "\\t !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~"; // "\t !"#\$%&'\(\)\*\+,\-\./0-9:;<=>\?@A-Z\[\\\]\^_`a-z\{\|\}~"
	
					// ascii with control characters:
					//String chars = "\\s !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~";
					
					// ascii without control characters:
					//String allowedChars = " !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~"; // "\t !"#\$%&'\(\)\*\+,\-\./0-9:;<=>\?@A-Z\[\\\]\^_`a-z\{\|\}~"
					
					if( aValue.matches("[" + allowedChars + "]{1,}") ){  // "[\\p{ASCII}]{0,}" "\\A\\p{ASCII}*\\z" "^[\\p{ASCII}]*$"
						return true;
					}
					
					Pattern noASCIIpattern = Pattern.compile("[^" + allowedChars + "]{1}");
					Matcher m = noASCIIpattern.matcher(aValue);
					String noASCII = "";
					if (m.find()) {
						noASCII = m.group(0);
						
						Pattern ctrlCharPattern = Pattern.compile("[\\s]{1}"); // check for control chars (\s)
						Matcher ctrlCharMatcher = ctrlCharPattern.matcher(noASCII);					
						if (ctrlCharMatcher.find()) {
							// noASCII is a control char
							ctrlCharPattern = Pattern.compile("([[^\\s][ ]]{0,})[\\s]{1}"); // search for control chars (\s), except space (blank)
							ctrlCharMatcher = ctrlCharPattern.matcher(aValue);					
							if (ctrlCharMatcher.find()) {
								// Locate the control char
								int groupCount = ctrlCharMatcher.groupCount();
								if(groupCount > 0){
									String strBevorCtrlChar = ctrlCharMatcher.group(1);
									if(strBevorCtrlChar == null || strBevorCtrlChar.length() == 0){								
										throw new ValueException("found control char at begin of Input! Don't use control chars!");						
									}
									throw new ValueException("Please do not use control chars! found control char after '" + strBevorCtrlChar + "'");
								}
							}
							throw new ValueException("found control char in Input! Don't use control chars!");
						}
						
						throw new ValueException("Please do not use '" + noASCII + "' in '" + aValue + "'!");
					}
					
					// when code above is correct we should never go here ... 
					throw new ValueException("regex '[" + allowedChars + "]{1,}' is not matched by '" + aValue + "'! Please report! This Issue should be debugged!");
				
				}
				
				@Override
				public void set(String aValue) throws ValueException{
					// ascii without control characters:
					String allowedChars = " !\"#\\$%&'\\(\\)\\*\\+,\\-\\./0-9:;<=>\\?@A-Z\\[\\\\\\]\\^_`a-z\\{\\|\\}~"; // "\t !"#\$%&'\(\)\*\+,\-\./0-9:;<=>\?@A-Z\[\\\]\^_`a-z\{\|\}~"
					if( this.check(aValue, allowedChars) ){
						this.value = aValue;				
					}
				}		
				
				@Override
				public void transSet(String value) throws ValueException  {
					this.set(value);
				}		
				
				@Override
				public final String get(){
					return this.value;
				}	
				
				public abstract int getMaxSize();
				
			}
			
			public static class ASCII8 extends SizedASCII{
				public int getMaxSize(){ return 8; }
			}	
			
			public static class ASCII12 extends SizedASCII{
				public int getMaxSize(){ return 12; }			
			}
			
			public static class ASCII16 extends SizedASCII{
				public int getMaxSize(){ return 16; }			
			}	
			
			public static class ASCII32 extends SizedASCII{
				public int getMaxSize(){ return 32;	}			
			}	
			
			public static class ASCII64 extends SizedASCII{
				public int getMaxSize(){ return 64;	}			
			}	
			
			public static class ASCII128 extends SizedASCII{
				public int getMaxSize(){ return 128; }			
			}	
			
			public static class ASCII256 extends SizedASCII{
				public int getMaxSize(){ return 256; }			
			}	
					
			public static abstract class Base64 extends SizedASCII{
				@Override	
				public void set(String aValue) throws ValueException{
					String allowedChars = "A-Za-z0-9+/=";
					if( super.check(aValue, allowedChars) ){
						if(! ( aValue.matches("[A-Za-z0-9+/]{1,}[=]{0,}") )  ){  // "[\\p{ASCII}]{0,}" "\\A\\p{ASCII}*\\z" "^[\\p{ASCII}]*$"
							super.value = aValue;
						}				
						else{
							throw new ValueException("no valid Base64! '=' is only allowed at the end!");
						}
					}
				}
			}	
	
			public static class Base64_256 extends Base64{
				// How to calculate the memory for Base64-encoded data? See https://de.wikipedia.org/wiki/Base64 
				// 4 * ( ceil (256 / 3) ) = 344
				public int getMaxSize(){ return 344; }
			}
			
			public static class Base64_128 extends Base64{
				// 4 * ( ceil (128 / 3) ) = 172
				public int getMaxSize(){ return 172; }
			}
			
			public static class Base64_64 extends Base64{
				// 4 * ( ceil (64 / 3) ) = 88
				public int getMaxSize(){ return 88; }
			}	
			
			
			
			public static abstract class MailAddress extends SizedASCII{
				
				// See http://stackoverflow.com/questions/7717573/what-is-the-longest-possible-email-address
				public abstract int getMaxSize(); // longest email-address is 254
				
				@Override
				public void set(String aValue) throws ValueException{
					if(aValue == null){
						super.value = null;
					}
					
					aValue = aValue.trim();
					if(aValue.length() > this.getMaxSize()){
						throw new ValueException("'" + aValue + "' is longer than " + this.getMaxSize());
					}
					
					if( aValue.matches("\\A[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\z") ){
						super.value = aValue;
					}
					else{
						throw new ValueException("'" + aValue + "' is no valid mail-Address");
					}
				}		
			}
			
			public static class MailAddress64 extends MailAddress{
				public int getMaxSize(){ return 64; }
			}
			
			public static class MailAddress128 extends MailAddress{
				public int getMaxSize(){ return 128; }
			}	
			
			public static class MailAddress254 extends MailAddress{
				public int getMaxSize(){ return 254; } // longest email-address is 254
			}
		
		}
			
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
		return new ComplexDataType(theIndex, theOrderedAttributeTypes);
	}	
		
	public static class Identifier<T> {
		
		private final ASCII8 label;
		
		//private final ValueFactory<T> factory;
		
		private final PrimitiveDataType<T> type;
		
		/*
		private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
			this.label = theLabel;
			this.factory = theFactory;
		}
		*/
		private Identifier(ASCII8 theLabel, PrimitiveDataType<T> theType){
			this.label = theLabel;
			//this.factory = theFactory;
			this.type = theType;
		}				
		
		public Value.Interface<T> createValue() {
			//return this.factory.createValueImp();
			return this.type.factory.createValueImp();
		}
		
		public ASCII8 getLabel() {
			return this.label;
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
	
	public static class Type {
		
		public static class PrimitiveDataType<T> {
					
			private final Class<? extends Value.Interface<T>> clazz;
			
			private final ValueFactory<T> factory;
					
			@SuppressWarnings("unchecked")
			public static <T> PrimitiveDataType<T> getInstance(String dataType){
				
				Class<?> theClass;
				try {
					theClass = Class.forName(Data.Value.Implementation.class.getName() + "$" + dataType);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				return getInstance( (Class<Value.Interface<T>>)theClass );
				
			}
			
			@SuppressWarnings("unchecked")
			public static <T> PrimitiveDataType<T> getInstance(Class<? extends Value.Interface<T>> theClass){
				for (int i = 0; i < types.length; i++){
					if(types[i].getClazz() == theClass){
						return (PrimitiveDataType<T>)Data.types[i];
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
				return new Identifier<T>(theName, this);
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
			 
			private ComplexDataType(final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedAttributeTypes){
				this.index = theIndex;
				this.orderedAttributes = theOrderedAttributeTypes;			
			}
			
			public Structure _createState() {
				Value.Interface<?>[] vals = new Value.Interface<?>[ this.orderedAttributes.length ];
				for(int i = 0; i < this.orderedAttributes.length; i++){
					vals[i] = this.orderedAttributes[i].createValue();
				}
				return new Structure(this, vals);
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
				return new Structure(this, theValues);
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
				return new Structure(this, vals);
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
				return new Structure(this, vals);
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
	

}
