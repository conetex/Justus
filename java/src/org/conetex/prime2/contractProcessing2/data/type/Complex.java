package org.conetex.prime2.contractProcessing2.data.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.values.Label;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

	public class Complex extends AbstractType<Value<?>[]> { // implements ValueFactory<Value<?>[]>
		
		private final Map<String, Integer> index;
		
		private final Identifier<?>[] orderedIdentifier;
		 
		private static Complex create(final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedIdentifiers){
			if(theIndex != null && theOrderedIdentifiers != null){
				return new Complex(theIndex, theOrderedIdentifiers);
			}
			return null;
		}			
		
		public static Complex createComplexDataType(final Identifier<?>[] theOrderedIdentifiers) throws Identifier.DuplicateIdentifierNameExeption, Identifier.NullIdentifierException{
			if(theOrderedIdentifiers.length == 0){
				return null;
			}
			Map<String, Integer> theIndex = new HashMap<String, Integer>();
			for(int i = 0; i < theOrderedIdentifiers.length; i++){
				if(theOrderedIdentifiers[i] == null){
					throw new Identifier.NullIdentifierException();
				}				
				String label = theOrderedIdentifiers[i].getLabel().get();
				if(theIndex.containsKey(label)){
					throw new Identifier.DuplicateIdentifierNameExeption();
				}
				theIndex.put(label, i);
			}
			//return new ComplexDataType(theIndex, theOrderedAttributeTypes);
			return Complex.create(theIndex, theOrderedIdentifiers);
		}			
		
		private Complex(final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedIdentifiers){
			this.index = theIndex;
			this.orderedIdentifier = theOrderedIdentifiers;			
		}
		
		
		
		public Structure _createState() {
			Value<?>[] vals = new Value<?>[ this.orderedIdentifier.length ];
			for(int i = 0; i < this.orderedIdentifier.length; i++){
				vals[i] = this.orderedIdentifier[i].createValue();
			}
			//return new Structure(this, vals);
			return Structure.create(this, vals);
		}
		
		public Structure construct(Value<?>[] theValues) {
			if(theValues.length == 0){
				// TODO Exception
				return null;
			}			
			if(theValues.length != this.orderedIdentifier.length){
				// TODO Exception
				return null;
			}		
			//return new Structure(this, theValues);
			return Structure.create(this, theValues);
		}
		
		public Structure construct(String[] theValues) {
			Value<?>[] vals = new Value<?>[ this.orderedIdentifier.length ];
			try {
				for(int i = 0; i < this.orderedIdentifier.length; i++){
					Value<?> re = this.orderedIdentifier[i].createValue();
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
			if(theValues.size() != this.orderedIdentifier.length){
				// TODO Exception
				return null;
			}
			Value<?>[] vals = new Value<?>[ this.orderedIdentifier.length ];
			try {
				for(int i = 0; i < this.orderedIdentifier.length; i++){
					Value<?> re = this.orderedIdentifier[i].createValue();
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
		
		
		
		
		

		
		
	

		

		@Override
		public Identifier<Value<?>[]> createIdentifier(Label theName)
				throws NullLabelException, EmptyLabelException {
			return AbstractType.<Value<?>[]>createIdentifier(theName, this);
		}

		@Override
		public Class<? extends Value<Value<?>[]>> getClazz() {
			return Structure.class;
		}

		@Override
		public Value<Value<?>[]> createValue() {
			return Structure.create(this);
		}

		
	}		
