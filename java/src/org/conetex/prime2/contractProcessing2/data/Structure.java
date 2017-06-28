package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.Type.ComplexDataType;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.Label;
import org.conetex.prime2.contractProcessing2.data.Value.Interface;
import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;

public class Structure implements Value.Interface<Value.Interface<?>[]>{
		
		private final ComplexDataType type;
		
		private Value.Interface<?>[] values;
				
		static Structure create(final ComplexDataType theAttributeTuple, final Value.Interface<?>[] theValues){
			if(theAttributeTuple != null && theValues != null){
				return new Structure(theAttributeTuple, theValues);
			}
			return null;
		}				

		static Structure create(final ComplexDataType theAttributeTuple){
			if(theAttributeTuple != null){
				return new Structure(theAttributeTuple, null);
			}
			return null;
		}	
		
		private Structure(final ComplexDataType theAttributeTuple, final Value.Interface<?>[] theValues){
			this.type = theAttributeTuple;
			this.values = theValues;		
		}	
		
		public static String[] split(String aName){
			String[] re = new String[2];
			if(aName == null){
				return re;
			}
		    int i = aName.indexOf(Label.NAME_SEPERATOR);
			if(i > -1 && i < aName.length()){
				re[0] = aName.substring(0, i);
		    	if(i + Label.NAME_SEPERATOR.length() < aName.length()){
		    		re[1] = aName.substring(i+Label.NAME_SEPERATOR.length());
		    	}
		    }
			return re;
		}
				
		public <V extends Value.Interface<?>> V getValue (String aName, Class<V> c){
			// TODO do xpath syntax. access parent objects ???
			int attributeIdx = this.type.getAttributeIndex(aName);
			if( attributeIdx > -1 ){
				return getValue( attributeIdx, c );
			}
			else{
				/*
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
			    */
				String[] names = Structure.split(aName);
			    if(names[0] != null){
					if(names[1] != null){
			    		attributeIdx = this.type.getAttributeIndex( names[0] );
			    		// TODO wenn hier die typen nicht passen und keine structure da liegt, sondern was anderes...
			    		// sollte das vernünftig gemeldet werden!!!
			    		Structure subStructure = getValue(attributeIdx, Structure.class);
			    		if(subStructure != null){
			    			return subStructure.getValue(names[1], c);
			    		}
					}
			    }				
				
			}
			return null;
		}
		
		public Value.Interface<?> getValue (String aName){
			return this.getValue( aName, Value.Interface.class );// TODO seltsam sieht das aus ...
		}

		@SuppressWarnings("unchecked")
		private <V extends Value.Interface<?>> V getValue (int i, Class<V> c){
			Value.Interface<?> v = getValue(i);
			if( v != null ){
				// TODO check this cast
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

		public Structure _createCopy() {
			Value.Interface<?>[] theValues = new Value.Interface<?>[ this.values.length ];
			for(int i = 0; i < theValues.length; i++){
				theValues[i] = this.values[i].createValue();
			}
			return create(type, theValues);
		}



		
		private static <T> Interface<T> clone(Interface<T> in){
			Interface<T> newV = in.createValue();
			T val = in.getCopy();
			try {
				newV.set( val );
			} catch (ValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return newV;
		}

		@Override
		public void transSet(String value) throws ValueTransformException, ValueException {
			throw new ValueTransformException("can not create Structure from String!");
		}

		@Override
		public Interface<Interface<?>[]> createValue() {
			// TODO drop those methods! sinnlos!
			return null;
		}

		@Override
		public Interface<?>[] get() {
			if(this.values == null){
				return null;
			}
			return this.values;
		}

		@Override
		public Interface<?>[] getCopy() {
			if(this.values == null){
				return null;
			}			
			Value.Interface<?>[] theValues = new Value.Interface<?>[ this.values.length ];
			for(int i = 0; i < theValues.length; i++){
				theValues[i] = clone(this.values[i]);
				if(theValues[i] == null){
					return null;
				}
			}
			return theValues;
		}

		@Override
		public void set(Interface<?>[] svalues) throws ValueException {
			// TODO typcheck ...
			if(this.values == null || this.values.length == svalues.length){
				this.values = svalues;
			}
			
		}
		
	}
