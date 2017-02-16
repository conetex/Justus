package org.conetex.prime2.contractProcessing;

import java.util.HashMap;
import java.util.Map;

import org.conetex.prime2.contractProcessing.Types.*;

public class State {

	public static void main(String[] args) throws ValueException{
		PrimitiveDataType<Base64_256, String> simpleType = PrimitiveDataType.getInstance(Base64_256.class);
		simpleType = new PrimitiveDataType< Base64_256,  String   > ( Base64_256.class , new ValueFactory<String>(){	public Base64_256 createValueImp() { return new Base64_256();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einStr");
		Attribute<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Attribute<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ValueFactory<State> x = complexType;
		PrimitiveDataType<Complex, State> 
		
		simpleTypeChild = new PrimitiveDataType< Complex , State> ( Complex.class     , x );
		//simpleTypeChild = new PrimitiveDataType< Complex , State> ( Complex.class     , new ValueFactory<State>() { public Complex  createValue() { return new Complex()     ; } } );
		//                new PrimitiveDataType< ASCII8 , String> ( ASCII8.class      , new ValueFactory<String>(){ public ASCII8   createValue() { return new ASCII8()  ; } } )
		Attribute<String> attributeChild = null;
		try {
			attributeChild = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		Attribute<?>[] theOrderedAttributesChild = {attributeChild};
		ComplexDataType complexTypeParent = null;
		try {
			complexTypeParent = createComplexDataType(theOrderedAttributesChild);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		State state = complexTypeParent.createState();
		
		Value<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");
		
		System.out.println("ok");
	}	
	
	public static void mainMail(String[] args) throws ValueException {
		PrimitiveDataType<MailAddress64, String> simpleType = PrimitiveDataType.getInstance(MailAddress64.class);
		simpleType = new PrimitiveDataType< MailAddress64,  String   > ( MailAddress64.class , new ValueFactory<String>(){	public MailAddress64 createValueImp() { return new MailAddress64();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einName");
		Attribute<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Attribute<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		State state = complexType.createState();
		
		Value<String> value = state.getValue("einName", MailAddress.class);
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
		PrimitiveDataType<Base64_256, String> simpleType = PrimitiveDataType.getInstance(Base64_256.class);
		simpleType = new PrimitiveDataType< Base64_256,  String   > ( Base64_256.class , new ValueFactory<String>(){	public Base64_256 createValueImp() { return new Base64_256();  } } );
		
		ASCII8 str = new ASCII8();
		str.set("einStr");
		Attribute<String> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Attribute<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		State state = complexType.createState();
		
		Value<String> value = state.getValue("einStr", Base64_256.class);
		value.set("mat thias.!  #$%. frm");
		
		System.out.println("ok");
	}	
	
	public static void mainInt(String[] args) throws ValueException{
		
		PrimitiveDataType<Int, Integer> simpleType = PrimitiveDataType.getInstance(Int.class);
		ASCII8 str = new ASCII8();
		str.set("ein Name");
		Attribute<Integer> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		Attribute<?>[] theOrderedAttributes = {attribute};
		
		ComplexDataType complexType = null;
		try {
			complexType = createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		State state = complexType.createState();
		
		Value<Integer> value = state.getValue("ein Name", Int.class);
		try {
			value.set(3);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//System.out.println(attribute);
		
	}

	private static PrimitiveDataType<?,?>[] types = 
		{
			  new PrimitiveDataType< Bool     , Boolean> ( Bool.class     , new ValueFactory<Boolean>() { public Bool      createValueImp() { return new Bool()     ; } } )	
		    , new PrimitiveDataType< Int      , Integer> ( Int.class      , new ValueFactory<Integer>() { public Int       createValueImp() { return new Int()      ; } } )
			, new PrimitiveDataType< Lng      , Long   > ( Lng.class      , new ValueFactory<Long>()    { public Lng       createValueImp() { return new Lng()      ; } } )
			
			, new PrimitiveDataType< ASCII8  , String > ( ASCII8.class  , new ValueFactory<String>()  { public ASCII8   createValueImp() { return new ASCII8()  ; } } )
			, new PrimitiveDataType< ASCII12 , String > ( ASCII12.class , new ValueFactory<String>()  { public ASCII12  createValueImp() { return new ASCII12() ; } } )
			, new PrimitiveDataType< ASCII16 , String > ( ASCII16.class , new ValueFactory<String>()  { public ASCII16  createValueImp() { return new ASCII16() ; } } )
			, new PrimitiveDataType< ASCII32 , String > ( ASCII32.class , new ValueFactory<String>()  { public ASCII32  createValueImp() { return new ASCII32() ; } } )
			, new PrimitiveDataType< ASCII64 , String > ( ASCII64.class , new ValueFactory<String>()  { public ASCII64  createValueImp() { return new ASCII64() ; } } )
			, new PrimitiveDataType< ASCII128, String > ( ASCII128.class, new ValueFactory<String>()  { public ASCII128 createValueImp() { return new ASCII128(); } } )
			, new PrimitiveDataType< ASCII256, String > ( ASCII256.class, new ValueFactory<String>()  { public ASCII256 createValueImp() { return new ASCII256(); } } )

			, new PrimitiveDataType< Base64_256, String > ( Base64_256.class, new ValueFactory<String>()  { public Base64_256 createValueImp() { return new Base64_256(); } } )
			, new PrimitiveDataType< Base64_128, String > ( Base64_128.class, new ValueFactory<String>()  { public Base64_128 createValueImp() { return new Base64_128(); } } )
			, new PrimitiveDataType< Base64_64 , String > ( Base64_64.class , new ValueFactory<String>()  { public Base64_64  createValueImp() { return new Base64_64() ; } } )
			
			, new PrimitiveDataType< MailAddress64 , String > ( MailAddress64.class , new ValueFactory<String>()  { public MailAddress64  createValueImp() { return new MailAddress64() ; } } )
			, new PrimitiveDataType< MailAddress128, String > ( MailAddress128.class, new ValueFactory<String>()  { public MailAddress128 createValueImp() { return new MailAddress128(); } } )
			, new PrimitiveDataType< MailAddress254, String > ( MailAddress254.class, new ValueFactory<String>()  { public MailAddress254 createValueImp() { return new MailAddress254(); } } )
		};	
	
	private final ComplexDataType type;
	
	private final Value<?>[] values;
	
	public static State createState(){
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
		
		State state = complexType.createState();
		return state;
	}

	private State(final ComplexDataType theAttributeTuple, final Value<?>[] theValues){
		this.type = theAttributeTuple;
		this.values = theValues;		
	}	
	
	public <V extends Value<?>> V getValue (String aName, Class<V> c){
		return getValue( this.type.getAttributeIndex(aName), c );
	}
	
	@SuppressWarnings("unchecked")
	private <V extends Value<?>> V getValue (int i, Class<V> c){
		Value<?> v = getValue(i);
		if( v != null ){
			return (V) v;
		}
		return null;
	}
	
	public Value<?> getValue (String aName){
		return getValue( this.type.getAttributeIndex(aName) );
	}
	
	private Value<?> getValue (int i){
		if(i > -1 && i < this.values.length){
			return this.values[i];
		}
		return null;
	}		
		
	private static interface ValueFactory<T>{
		public Value<T> createValueImp();
	}
	
	public static class ValueException extends Exception {
		private static final long serialVersionUID = 1L;		
		public ValueException(String msg){
			super(msg);
		}
	}	
	
	public static interface Value<T>{
		
		public abstract T get();
		
		public abstract void set(T value) throws ValueException;
		
	}
		
	private static ComplexDataType createComplexDataType(final Attribute<?>[] theOrderedAttributeTypes) throws DuplicateAttributeNameExeption, NullAttributeException{
		Map<String, Integer> theIndex = new HashMap<String, Integer>();
		for(int i = 0; i < theOrderedAttributeTypes.length; i++){
			if(theOrderedAttributeTypes[i] == null){
				throw new NullAttributeException();
			}				
			String label = theOrderedAttributeTypes[i].getLabel().get();
			if(theIndex.containsKey(label)){
				throw new DuplicateAttributeNameExeption();
			}
			theIndex.put(label, i);
		}
		return new ComplexDataType(theIndex, theOrderedAttributeTypes);
	}	
	
	public static class ComplexDataType implements ValueFactory<State>{
		
		private final Map<String, Integer> index;
		
		private final Attribute<?>[] orderedAttributes;
		
		private ComplexDataType(final Map<String, Integer> theIndex, final Attribute<?>[] theOrderedAttributeTypes){
			this.index = theIndex;
			this.orderedAttributes = theOrderedAttributeTypes;			
		}
		
		public State createState() {
			Value<?>[] theAttributes = new Value<?>[ this.orderedAttributes.length ];
			for(int i = 0; i < this.orderedAttributes.length; i++){
				theAttributes[i] = this.orderedAttributes[i].createValue();
			}
			return new State(this, theAttributes);
		}
		
		public int getAttributeIndex(String aName){
			Integer i = this.index.get(aName);
			if(i == null){
				return -1;
			}			
			return i;
		}
		
		@SuppressWarnings("unused")
		private Attribute<?> _test_getAttribute(String aName){
			Integer i = this.index.get(aName);
			if(i == null){
				return null;
			}			
			return _test_getAttribute(i);
		}	
		
		private Attribute<?> _test_getAttribute(int i){
			if(i > -1 && i < this.orderedAttributes.length){
				return this.orderedAttributes[i];
			}			
			return null;
		}

		@Override
		public Value<State> createValueImp() {
			// here we go
			return new Complex( createState() );
		}	
		
	}		
		
	private static class Attribute<T> {
		
		private final ASCII8 label;
		
		private final ValueFactory<T> factory;
		
		private Attribute(ASCII8 theLabel, ValueFactory<T> theFactory){
			this.label = theLabel;
			this.factory = theFactory;
		}
		
		public Value<T> createValue() {
			return this.factory.createValueImp();
		}
		
		public ASCII8 getLabel() {
			return this.label;
		}
		
	}
	
	private static class PrimitiveDataType<V extends Value<T>, T> {
		
		private final Class<V> clazz;
		
		private final ValueFactory<T> factory;
				
		@SuppressWarnings("unchecked")
		public static <V extends Value<T>, T> PrimitiveDataType<V, T> getInstance(Class<V> theClass){
			for (int i = 0; i < types.length; i++){
				if(types[i].getClazz() == theClass){
					return (PrimitiveDataType<V, T>)State.types[i];
				}
			}
			return null;
		} 		
		
		private PrimitiveDataType(Class<V> theClass, ValueFactory<T> theFactory){
			this.clazz = theClass;
			this.factory = theFactory;
		}
		
		public Attribute<T> createAttribute(ASCII8 theName) throws NullLabelException, EmptyLabelException {
			if(theName == null || theName.get() == null){
				throw new NullLabelException();
			}
			if(theName.get().length() < 1){
				throw new EmptyLabelException();
			}
			return new Attribute<T>(theName, this.factory);
		}

		private Class<V> getClazz() {
			return this.clazz;
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
