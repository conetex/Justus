package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;
import org.conetex.prime2.contractProcessing2.data.Variable;

public class Reference2Variable<T> implements Value.Interface<T>{

	public static <T> Reference2Variable<T> create(Variable<T> theVariable){
		if(theVariable == null){
			return null;
		}
		return new Reference2Variable<T>(theVariable);
	}	
	
	private Variable<T> variable;

	private Reference2Variable(Variable<T> theVariable){
		this.variable = theVariable;	
	}	
		
	@Override
	public T get() {
		return this.variable.get();
	}

	@Override
	public void set(T value) throws ValueException {
		this.variable.set(value);
	}

	@Override
	public void transSet(String value) throws ValueTransformException, ValueException {
		this.variable.transSet(value);
	}
	
	
}
