package org.conetex.prime2.contractProcessing2.data.valueImplement;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class Structure implements Value<Value<?>[]>{
		
		private final Complex type;
		
		private Structure parent;
		
		private Value<?>[] values;
				
		public static Structure _create(final Complex theAttributeTuple, final Value<?>[] theValues, final Structure theParent){
			if(theAttributeTuple != null && theValues != null){
				return null;//new Structure(theAttributeTuple, theValues, theParent);
			}
			return null;
		}				

		public static Structure create(final Complex theAttributeTuple, final Structure theParent){
			if(theAttributeTuple != null){
				return new Structure(theAttributeTuple, theParent);
			}
			return null;
		}	
		
		/*
		private Structure(final Complex theAttributeTuple, final Value<?>[] theValues, final Structure theParent){
			this.type = theAttributeTuple;
			this.values = theValues;
			this.parent = theParent;
		}	*/
		
		private Structure(final Complex theAttributeTuple, final Structure theParent){
			this.type = theAttributeTuple;
			this.values = new Value<?>[ theAttributeTuple.getIdentifiersSize() ];
			this.parent = theParent;
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
				
		public <V extends Value<?>> V getValue (String aName, Class<V> c){
			// TODO do xpath syntax. access parent objects ???
			int idIndex = this.type.getSubIdentifierIndex(aName);
			if( idIndex > -1 ){
				return getValue( idIndex, c );
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
						idIndex = this.type.getSubIdentifierIndex( names[0] );
			    		// TODO wenn hier die typen nicht passen und keine structure da liegt, sondern was anderes...
			    		// sollte das vernünftig gemeldet werden!!!
			    		Structure subStructure = getValue(idIndex, Structure.class);
			    		if(subStructure != null){
			    			return subStructure.getValue(names[1], c);
			    		}
					}
			    }				
				
			}
			return null;
		}
		
		public Value<?> getValue (String aName){
			return this.getValue( aName, Value.class );// TODO seltsam sieht das aus ...
		}

		@SuppressWarnings("unchecked")
		private <V extends Value<?>> V getValue (int i, Class<V> c){
			Value<?> v = getValue(i);
			if( v != null ){
				// TODO check this cast
				return (V) v;
			}
			return null;
		}
		
		
		private <R> Value<R> getValueNew (int i, Class<R> c){
			Value<?> v = getValue(i);
			if( v != null ){
				// TODO check this cast
				return (Value<R>) v;
			}
			return null;
		}		
		
		public <R extends Value<?>> R getValueNewNew(String aName, Class<R> clazz) {
		//public <V extends Value<?>> V getValue (String aName, Class<V> c){
				// TODO do xpath syntax. access parent objects ???
				int idIndex = this.type.getSubIdentifierIndex(aName);
				if( idIndex > -1 ){
					// TODO check this cast
					return (R) getValue( idIndex );
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
							idIndex = this.type.getSubIdentifierIndex( names[0] );
				    		// TODO wenn hier die typen nicht passen und keine structure da liegt, sondern was anderes...
				    		// sollte das vernünftig gemeldet werden!!!
				    		Structure subStructure = getValue(idIndex, Structure.class);
				    		if(subStructure != null){
				    			return subStructure.getValueNewNew(names[1], clazz);
				    		}
						}
				    }				
				    else {
				    	if(this.parent != null) {
				    		return this.parent.getValueNewNew(aName, clazz);
				    	}
				    }
				}
				return null;
			}		
		
		public <R> Value<R> getValueNew(String aName, Class<R> clazz) {
		//public <V extends Value<?>> V getValue (String aName, Class<V> c){
				// TODO do xpath syntax. access parent objects ???
				int idIndex = this.type.getSubIdentifierIndex(aName);
				if( idIndex > -1 ){
					return getValueNew( idIndex, clazz );
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
							idIndex = this.type.getSubIdentifierIndex( names[0] );
				    		// TODO wenn hier die typen nicht passen und keine structure da liegt, sondern was anderes...
				    		// sollte das vernünftig gemeldet werden!!!
				    		Structure subStructure = getValue(idIndex, Structure.class);
				    		if(subStructure != null){
				    			return subStructure.getValueNew(names[1], clazz);
				    		}
						}
				    }
				    else {
				    	if(this.parent != null) {
				    		return this.parent.getValueNew(aName, clazz);
				    	}
				    }
					
				}
				return null;
			}		
		
		
		private Value<?> getValue (int i){
			if(i > -1 && i < this.values.length){
				return this.values[i];
			}
			return null;
		}
		
		@Override
		public Value<?>[] setConverted(String value) throws Inconvertible, Invalid {
			throw new Inconvertible("can not create Structure from String!");
		}

		@Override
		public Value<?>[] get() {
			if(this.values == null){
				return null;
			}
			return this.values;
		}

		// @Override
		public Value<?>[] copy() throws Invalid {
			if(this.values == null){
				return null;
			}			
			Value<?>[] theValues = new Value<?>[ this.values.length ];
			for(int i = 0; i < theValues.length; i++){
				theValues[i] = clone(this.values[i]);
			}
			return theValues;
		}

		private static <T> Value<T> clone(Value<T> src) throws Invalid{
			Primitive<T> type = Primitive.<T>getInstance(src.getClass());
			Value<T> re = type.createValue();
			T val = src.copy();
			re.set( val ); // throws the Exception
			return re;
		}		
		
		
		@Override
		public Value<?>[] set(Value<?>[] svalues) throws Invalid {
			// TODO typcheck ...
			if(this.values == null || this.values.length == svalues.length){
				this.values = svalues;
			}
			return this.values;
		}

		public boolean set(String id, Value<?> value) throws Invalid {
			int i = this.type.getSubIdentifierIndex(id);
			if(i > -1 && i < this.values.length){
				this.values[i] = value;
				return true;
			}
			return false;					
		}
		
		@Override
		public Class<Value<?>[]> getBaseType() {
			return null;
		}


		
	}
