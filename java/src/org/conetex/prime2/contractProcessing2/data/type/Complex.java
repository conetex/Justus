package org.conetex.prime2.contractProcessing2.data.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.values.ASCII8;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

	public class Complex extends AbstractType<Value<?>[]> implements ValueFactory<Value<?>[]>{
		
		private final Map<String, Integer> index;
		
		private final Identifier<?>[] orderedAttributes;
		 
		public static Complex create(final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedAttributeTypes){
			if(theIndex != null && theOrderedAttributeTypes != null){
				return new Complex(theIndex, theOrderedAttributeTypes);
			}
			return null;
		}			
		
		public static Complex createComplexDataType(final Identifier<?>[] theOrderedAttributeTypes) throws Identifier.DuplicateAttributeNameExeption, Identifier.NullAttributeException{
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
			return Complex.create(theIndex, theOrderedAttributeTypes);
		}			
		
		private Complex(final Map<String, Integer> theIndex, final Identifier<?>[] theOrderedAttributeTypes){
			this.index = theIndex;
			this.orderedAttributes = theOrderedAttributeTypes;			
		}
		
		
		
		public Structure _createState() {
			Value<?>[] vals = new Value<?>[ this.orderedAttributes.length ];
			for(int i = 0; i < this.orderedAttributes.length; i++){
				vals[i] = this.orderedAttributes[i].createValue();
			}
			//return new Structure(this, vals);
			return Structure.create(this, vals);
		}
		
		public Structure construct(Value<?>[] theValues) {
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
			Value<?>[] vals = new Value<?>[ this.orderedAttributes.length ];
			try {
				for(int i = 0; i < this.orderedAttributes.length; i++){
					Value<?> re = this.orderedAttributes[i].createValue();
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
			Value<?>[] vals = new Value<?>[ this.orderedAttributes.length ];
			try {
				for(int i = 0; i < this.orderedAttributes.length; i++){
					Value<?> re = this.orderedAttributes[i].createValue();
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
		
		@SuppressWarnings("unchecked")
		public <V> Identifier<V> getIdentifier(String aName){
			Integer i = this.index.get(aName);
			if(i != null){
				if(i < 0 || i >= this.orderedAttributes.length){
					// TODO i < this.orderedAttributes.length darf auf keinen fall vorkommen! hier bitte schwere Exception werfen!
					return null;
				}
				// TODO typ-check !!!
				return (Identifier<V>)this.orderedAttributes[i];
			}
			else{
				
				String[] names = Structure.split(aName);
			    if(names[0] != null){
					if(names[1] != null){
			    		
						i = this.index.get( names[0] );
						if(i != null){
							if(i < 0 || i >= this.orderedAttributes.length){
								// TODO i < this.orderedAttributes.length darf auf keinen fall vorkommen! hier bitte schwere Exception werfen!
								return null;
							}
							
				    		AbstractType<?> dt = this.orderedAttributes[i].getType();
				    		if(dt != null){
				    			return dt.getIdentifier( names[1] );
				    		}
							
						}				    		
					}
			    }				
				
			}
			
			return null;
		}	
		
		
		
		
		

		
		
	

		

		@Override
		public Identifier<Value<?>[]> createAttribute(ASCII8 theName)
				throws NullLabelException, EmptyLabelException {
			return AbstractType.<Value<?>[]>createAttribute(theName, this);
		}

		@Override
		public Class<? extends Value<Value<?>[]>> getClazz() {
			return Structure.class;
		}

		@Override
		public Value<Value<?>[]> createValue() {
			return this.createValueImp();
		}

		@Override
		public Value<Value<?>[]> createValueImp() {
			return Structure.create(this);
		}	
		
	}		
