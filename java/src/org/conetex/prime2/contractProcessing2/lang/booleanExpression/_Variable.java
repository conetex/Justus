package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

public class _Variable<T> implements Value<T>{

	public static <T> _Variable<T> create(Identifier<T> id){
		if(id == null){
			return null;
		}
		Value<T> value = id.createValue();
		if(value == null){
			return null;
		}
		return new _Variable<T>(id, value);
	}

	Value<T> value;
	
	Identifier<T> identifier;
		
	private _Variable(Identifier<T> theId, Value<T> theValue){
		this.identifier = theId;
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

	@Override
	public T getCopy() {
		return this.value.getCopy();
	}

}
