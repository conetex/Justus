package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.lang.Reference2Value;

public abstract class AbstractAssigment<T> {

	private Reference2Value<T> source;
	
	private Reference2Value<T> target;
		
	protected AbstractAssigment(Reference2Value<T> src, Reference2Value<T> trg){
		this.source = src;
		this.target = trg;
	}

	public abstract boolean doCopy();
	
	public boolean compute(Structure thisObject) {
		T value = null;
		if(this.doCopy()){
			value = source.getCopy(thisObject);			
		}
		else{
			value = source.get(thisObject);
		}

		try {
			target.set( thisObject, value );
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
			
		return true;
	}	
	
}
