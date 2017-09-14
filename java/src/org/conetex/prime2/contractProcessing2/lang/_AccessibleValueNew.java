package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class _AccessibleValueNew<T extends Value<?>> extends AccessibleAbstract<T> {

	public static <T extends Value<?>> _AccessibleValueNew<T> __create(String thePath, Class<T> theClass) {
		if (thePath == null) {
			return null;
		}
		return new _AccessibleValueNew<T>(thePath, theClass);
	}

	private String path;

	private final Class<T> clazz;

	private _AccessibleValueNew(String thePath, Class<T> theClass) {
		this.path = thePath;
		this.clazz = theClass;
	}

	public T getFrom(Structure thisObject) {
		/*
		Value<T> valueA = thisObject.getValueNew(this.path, this.clazz);
		if(valueA == null){
			return null;
		}
		return valueA.get();
		*/
		return thisObject.getValueNewNew(this.path, this.clazz);
		
		//return value.get();
	}

	/*
	@Override
	public void setTo(Structure thisObject, T newValue) throws Invalid {
		Value<T> value = thisObject.getValueNew(this.path, this.clazz);
		value.set(newValue);
	}

	public void transSet(Structure thisObject, String newValue) throws Inconvertible, Invalid {
		Value<T> value = thisObject.getValueNew(this.path, this.clazz);
		value.setConverted(newValue);
	}
	*/

	
	public T copyFrom(Structure thisObject) throws Invalid {
		Value<T> value = thisObject.getValueNew(this.path, this.clazz);
		return value.copy();
	}

	
	public Class<T> getBaseType() {
		return this.clazz;
	}



}
