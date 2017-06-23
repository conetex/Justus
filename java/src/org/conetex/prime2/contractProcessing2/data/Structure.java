package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.Type.ComplexDataType;

public class Structure {
		
		private final ComplexDataType type;
		
		private final Value.Interface<?>[] values;
		
		public static Structure _createState(){
			Identifier<?>[] theOrderedAttributes = {};
			ComplexDataType complexType = null;
			
			try {
				complexType = Type.ComplexDataType.createComplexDataType(theOrderedAttributes);
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
		
		static Structure create(final ComplexDataType theAttributeTuple, final Value.Interface<?>[] theValues){
			if(theAttributeTuple != null && theValues != null){
				return new Structure(theAttributeTuple, theValues);
			}
			return null;
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
