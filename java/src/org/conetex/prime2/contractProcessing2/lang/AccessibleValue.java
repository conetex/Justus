package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;

public class Reference2Value<T> implements AbstractValueAccess<T> //extends Subject 
//implements Value.Interface<T>
{

	public static <T> Reference2Value<T> create(String thePath, Class<? extends Value<T>> theClass){
		if(thePath == null){
			return null;
		}
		return new Reference2Value<T>(thePath, theClass);
	}
	
	private String path;
	
	private final Class<? extends Value<T>> clazz;

	private Reference2Value(String thePath, Class<? extends Value<T>> theClass){
		this.path = thePath;
		this.clazz = theClass;
	}
		
	@Override
	public T get(Structure thisObject) {
		Value<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		return valueWrapper.get();
	}

	@Override
	public void set(Structure thisObject, T value) throws ValueException {
		Value<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		valueWrapper.set(value);
	}

	public void transSet(Structure thisObject, String value) throws ValueTransformException, ValueException {
		Value<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		valueWrapper.transSet(value);	
	}

	@Override
	public T getCopy(Structure thisObject) {
		Value<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		return valueWrapper.getCopy();
	}


	
	
}
