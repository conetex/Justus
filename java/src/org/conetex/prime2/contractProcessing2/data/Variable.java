package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;

public class Variable<T> implements Value.Interface<T>{

	public static <T> Variable<T> create(Identifier<T> id){
		if(id == null){
			return null;
		}
		Value.Interface<T> value = id.createValue();
		if(value == null){
			return null;
		}
		return new Variable<T>(id, value);
	}

	Value.Interface<T> value;
	
	Identifier<T> id;
		
	private Variable(Identifier<T> theId, Value.Interface<T> theValue){
		this.id = theId;
		this.value = theValue;	
	}
	
	@Override
	public void set(T value) throws ValueException{
		this.value.set(value);
	}
	
	@Override
	public void transSet(String value) throws ValueTransformException, ValueException{
		this.value.transSet(value);
	}
	
	@Override
	public T get(){
		return this.value.get();
	}	
}
