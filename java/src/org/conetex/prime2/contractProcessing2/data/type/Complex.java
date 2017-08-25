package org.conetex.prime2.contractProcessing2.data.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Symbol;

	public class Complex extends AbstractType<Value<?>[]> { // implements ValueFactory<Value<?>[]>
		
		private static Map<String, Complex> instances = new HashMap<String, Complex>();
		
		public static Complex getInstance(String typeName){
			return instances.get(typeName);
		}
		
		public static void clearInstances(){
			instances.clear();
		}
		
		public static Set<String> getInstanceNames() {
			return instances.keySet();
		}		
		
		private final Map<String, Integer> index;
		
		private Identifier<?>[] orderedIdentifier;// TODO kann das nicht doch final werden?
		 
		private String name;
		
		public String getName() {
			return this.name;
		}

		private static Complex createImpl(final String theName, final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedIdentifiers) {
			if(theIndex != null && theOrderedIdentifiers != null){
				return new Complex(theName, theIndex, theOrderedIdentifiers);
			}
			return null;
		}			
		
		public static Complex create(final String theName) {
			Map<String, Integer> index = new HashMap<String, Integer>();
			Identifier<?>[] idents = new Identifier<?>[0];
			return Complex.createImpl(theName, index, idents);
		}
		
		public static Complex createInit(String typeName, final Identifier<?>[] theOrderedIdentifiers) throws Identifier.DuplicateIdentifierNameExeption, Identifier.NullIdentifierException, DublicateComplexException{
			if(theOrderedIdentifiers.length == 0){
				return null;
			}
			Map<String, Integer> theIndex = new HashMap<String, Integer>();
			buildIndex(theIndex, theOrderedIdentifiers);
			//return new ComplexDataType(theIndex, theOrderedAttributeTypes);
			if(Complex.instances.containsKey(typeName)){
				throw new DublicateComplexException(typeName);
			}		
			Complex re = Complex.createImpl(typeName, theIndex, theOrderedIdentifiers);
			Complex.instances.put(typeName, re);
			return re;
		}			
				
		private static void buildIndex(Map<String, Integer> theIndex, final Identifier<?>[] theOrderedIdentifiers) throws Identifier.DuplicateIdentifierNameExeption, Identifier.NullIdentifierException{
			for(int i = 0; i < theOrderedIdentifiers.length; i++){
				if(theOrderedIdentifiers[i] == null){
					throw new Identifier.NullIdentifierException();
				}				
				String label = theOrderedIdentifiers[i].getLabel().get();
				if(theIndex.containsKey(label)){
					throw new Identifier.DuplicateIdentifierNameExeption(label);
				}
				theIndex.put(label, i);
			}
		}
		
		
		
		
		private Complex(final String theName, final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedIdentifiers){
			this.name = theName;
			this.index = theIndex;
			this.orderedIdentifier = theOrderedIdentifiers;			
		}
		
		public void init(String typeName, final Identifier<?>[] theOrderedIdentifiers) throws Identifier.DuplicateIdentifierNameExeption, Identifier.NullIdentifierException, ComplexWasInitializedExeption, DublicateComplexException{
			if(this.index.size() > 0 || this.orderedIdentifier.length > 0){
				throw new ComplexWasInitializedExeption();
			}
			if(Complex.instances.containsKey(typeName)){
				throw new DublicateComplexException(typeName);
			}
			this.orderedIdentifier = theOrderedIdentifiers;
			buildIndex(this.index, theOrderedIdentifiers);
			Complex.instances.put(typeName, this);
		}
		

		
		

		
		public Structure _construct(Value<?>[] theValues, Structure theParent) {
			if(theValues.length == 0){
				// TODO Exception
				return null;
			}			
			if(theValues.length != this.orderedIdentifier.length){
				// TODO Exception
				return null;
			}		
			//return new Structure(this, theValues);
			return Structure._create(this, theValues, theParent);
		}
		
		public int getIdentifiersSize(){
			return this.orderedIdentifier.length;
		}
		
		public Structure _construct(String[] theValues, Structure theParent) {
			Value<?>[] vals = new Value<?>[ this.orderedIdentifier.length ];
			try {
				for(int i = 0; i < this.orderedIdentifier.length; i++){
					Value<?> re = this.orderedIdentifier[i].createValue(theParent);
					re.setConverted( theValues[i] );
					vals[i] = re;
				}
			} catch (Inconvertible | Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}			
			//return new Structure(this, vals);
			return Structure._create(this, vals, theParent);
		}
		
		public Structure _construct(List<String> theValues, Structure theParent) {
			if(theValues.size() == 0){
				// TODO Exception
				return null;
			}			
			if(theValues.size() != this.orderedIdentifier.length){
				// TODO Exception
				return null;
			}
			Value<?>[] vals = new Value<?>[ this.orderedIdentifier.length ];
			try {
				for(int i = 0; i < this.orderedIdentifier.length; i++){
					Value<?> re = this.orderedIdentifier[i].createValue(theParent);
					re.setConverted( theValues.get(i) );
					vals[i] = re;
				}
			} catch (Inconvertible | Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}			
			//return new Structure(this, vals);
			return Structure._create(this, vals, theParent);
		}		
		
		public int getSubIdentifierIndex(String aName){
			Integer i = this.index.get(aName);
			if(i == null){
				return -1;
			}			
			return i;
		}
		
		@SuppressWarnings("unchecked")
		public <V> Identifier<V> getSubIdentifier(String aName){
			Integer i = this.index.get(aName);
			if(i != null){
				if(i < 0 || i >= this.orderedIdentifier.length){
					// TODO i < this.orderedAttributes.length darf auf keinen fall vorkommen! hier bitte schwere Exception werfen!
					return null;
				}
				// TODO typ-check !!!
				return (Identifier<V>)this.orderedIdentifier[i];
			}
			else{
				
				String[] names = Structure.split(aName);
			    if(names[0] != null){
					if(names[1] != null){
			    		
						i = this.index.get( names[0] );
						if(i != null){
							if(i < 0 || i >= this.orderedIdentifier.length){
								// TODO i < this.orderedAttributes.length darf auf keinen fall vorkommen! hier bitte schwere Exception werfen!
								return null;
							}
							
				    		AbstractType<?> dt = this.orderedIdentifier[i].getType();
				    		if(dt != null){
				    			return dt.getSubIdentifier( names[1] );
				    		}
							
						}				    		
					}
			    }				
				
			}
			
			return null;
		}	
		
		
		
		
		

		
		
	
		public Identifier<Value<?>[]> createComplexAttribute(String name){
			//PrimitiveDataType<Structure> simpleType = PrimitiveDataType.getInstance( Value.Implementation.Struct.class.getSimpleName() );
			
			Label str = new Label(); 
			try {
				str.set(name);
			} catch (Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			Identifier<Value<?>[]> attribute = null;
			try {
				attribute = this.createIdentifier( str );
			} catch (NullLabelException | EmptyLabelException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return null;
			}	
			
			return attribute;
			
		}	
		
		public static Identifier<?> createIdentifier(String attributeName, String typeName, Map<String, Complex> unformedComplexTypes){
			// ComplexType
			if(typeName == null || typeName.length() == 0){
				// TODO exception
				return null;
			}
			if(attributeName == null || attributeName.length() == 0){
				// TODO exception
				return null;
			}
			Complex c = Complex.getInstance(typeName);
			if(c == null){
				c = unformedComplexTypes.get(typeName);
				if(c == null){
					c = Complex.create(typeName);
					unformedComplexTypes.put(typeName, c);
				}				
			}
			Identifier<Value<?>[]> re = c.createComplexAttribute(attributeName);
	System.out.println("createAttributesValues " + attributeName + " " + typeName + " ==> " + re);
			return re;
		}		

		@Override
		public Identifier<Value<?>[]> createIdentifier(Label theName)
				throws NullLabelException, EmptyLabelException {
			return AbstractType.<Value<?>[]>createIdentifier(theName, this);
		}

		@Override
		public Class<? extends Value<Value<?>[]>> getClazz() {
			return Structure.class;
		}

		
		//public Value<Value<?>[]> createValue() {
		//	return Structure.create(this);
		//}
		public Structure createValue(Structure theParent) {//
			return Structure.create(this, theParent);//, 
		}

		public static class ComplexWasInitializedExeption extends Exception{
			private static final long serialVersionUID = 1L;
		}
		
		public static class DublicateComplexException extends Exception{
			public DublicateComplexException(String name) {
				super(name);
			}
			private static final long serialVersionUID = 1L;			
		}

		
		
		public static String[] splitRight(String aName){
			String[] re = new String[2];
			if(aName == null){
				return re;
			}
		    int i = aName.lastIndexOf(Symbol.TYPE_SEPERATOR);
			if(i > -1 && i < aName.length()){
				re[0] = aName.substring(0, i);
		    	if(i + Symbol.TYPE_SEPERATOR.length() < aName.length()){
		    		re[1] = aName.substring(i+Symbol.TYPE_SEPERATOR.length());
		    	}
		    }
			return re;
		}		
		

	}		
