package org.conetex.prime2.contractProcessing2.data;

import java.util.StringTokenizer;

import org.conetex.prime2.contractProcessing2.data.Type.ComplexDataType;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Label;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Struct;

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
			int attributeIdx = this.type.getAttributeIndex(aName);
			if( attributeIdx > -1 ){
				return getValue( attributeIdx, c );
			}
			else{
			    int i = aName.indexOf(Label.NAME_SEPERATOR);
			    if(i > -1 && i < aName.length()){
			    	String nameOfSubStructure = aName.substring(0, i);
			    	if(i + Label.NAME_SEPERATOR.length() < aName.length()){
			    		attributeIdx = this.type.getAttributeIndex( nameOfSubStructure );
			    		Value.Interface<Structure> subStructure = getValue(attributeIdx, Struct.class);
			    		if(subStructure != null){
			    			Structure s = subStructure.get();
			    			if(s != null){
			    				aName = aName.substring(i+Label.NAME_SEPERATOR.length());
			    				return s.getValue(aName, c);
			    			}
			    		}
			    	}
			    }
			}
			return null;
			
		}
		
		public Value.Interface<?> getValue (String aName){
			return this.getValue( aName, Value.Interface.class );
		}

		@SuppressWarnings("unchecked")
		private <V extends Value.Interface<?>> V getValue (int i, Class<V> c){
			Value.Interface<?> v = getValue(i);
			if( v != null ){
				return (V) v;
			}
			return null;
		}		
		
		private Value.Interface<?> getValue (int i){
			if(i > -1 && i < this.values.length){
				return this.values[i];
			}
			return null;
		}

		public Structure createCopy() {
			// TODO Auto-generated method stub
			Value.Interface<?>[] theValues = new Value.Interface<?>[ this.values.length ];
			for(int i = 0; i < theValues.length, i++){
				theValues[i] = this.values[i].getCopy();
			}
			return create(type, theValues);
		}
		
	}
