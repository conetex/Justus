package org.conetex.prime2.contractProcessing2.data;

import org.conetex.prime2.contractProcessing2.data.Data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Data.Value;
import org.conetex.prime2.contractProcessing2.data.Data.Value.ValueException;

public class Variable <T>{

	public static <T> Variable<T> create(Identifier<T> id){
		if(id == null){
			return null;
		}
		return new Variable<T>(id);
	}

	Value.Interface<T> value;
	
	Identifier<T> id;
		
	private Variable(Identifier<T> theId){
		this.id = theId;
		this.value = theId.createValue();	
	}
	
	public void set(T value) throws ValueException{
		this.value.set(value);
	}
	
	public T get(){
		return this.value.get();
	}	
}
