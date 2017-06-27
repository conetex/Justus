package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Structure;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Value.Interface;
import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;

public class Reference2Value<T> //extends Subject 
//implements Value.Interface<T>
{

	public static <T> Reference2Value<T> create(String thePath, Class<? extends Value.Interface<T>> theClass){
		if(thePath == null){
			return null;
		}
		return new Reference2Value<T>(thePath, theClass);
	}
	
	private String path;
	
	private final Class<? extends Value.Interface<T>> clazz;

	private Reference2Value(String thePath, Class<? extends Value.Interface<T>> theClass){
		this.path = thePath;
		this.clazz = theClass;
	}
		
	public T get(Structure thisObject) {
		Interface<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		return valueWrapper.get();
	}

	public void set(Structure thisObject, T value) throws ValueException {
		Interface<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		valueWrapper.set(value);
	}

	public void transSet(Structure thisObject, String value) throws ValueTransformException, ValueException {
		Interface<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		valueWrapper.transSet(value);	
	}

	public T getCopy(Structure thisObject) {
		Interface<T> valueWrapper = thisObject.getValue( this.path, this.clazz );
		return valueWrapper.getCopy();
	}


	
	
}
