package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Structure;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Value.Interface;
import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;

public class Reference2Value<T> implements Value.Interface<T>{

	public static <T> Reference2Value<T> create(Structure theObject, String thePath, Class<? extends Value.Interface<T>> theClass){
		if(theObject == null || thePath == null){
			return null;
		}
		return new Reference2Value<T>(theObject, thePath, theClass);
	}
	
	private Structure thisObject;
	
	private String path;
	
	private final Class<? extends Value.Interface<T>> clazz;

	private Reference2Value(Structure theObject, String thePath, Class<? extends Value.Interface<T>> theClass){
		this.thisObject = theObject;
		this.path = thePath;
		this.clazz = theClass;
	}
		
	@Override
	public T get() {
		Interface<T> valueWrapper = this.thisObject.getValue( this.path, this.clazz );
		return valueWrapper.get();
	}

	@Override
	public void set(T value) throws ValueException {
		Interface<T> valueWrapper = this.thisObject.getValue( this.path, this.clazz );
		valueWrapper.set(value);
	}

	@Override
	public void transSet(String value) throws ValueTransformException, ValueException {
		Interface<T> valueWrapper = this.thisObject.getValue( this.path, this.clazz );
		valueWrapper.transSet(value);	
	}

	@Override
	public T getCopy() {
		Interface<T> valueWrapper = this.thisObject.getValue( this.path, this.clazz );
		return valueWrapper.getCopy();
	}

	@Override
	public Interface<T> createValue() {
		// TODO sinnlos, das dies hier mal aufgerufen wird ... sollte also nicht das Value Interface implementieren...
		return new Reference2Value<T>(this.thisObject, this.path, this.clazz);
	}
	
	
}
