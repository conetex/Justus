package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.lang.AbstractPairComputer;
import org.conetex.prime2.contractProcessing2.lang.Reference2Value;

public abstract class AbstractAssigment<T> extends AbstractPairComputer<T>{
		
	protected AbstractAssigment(Reference2Value<T> trg, Reference2Value<T> src){
		super(trg, src);
	}

	public abstract boolean doCopy();
	
	public boolean compute(Structure thisObject) {
		T value = null;
		if(this.doCopy()){
			value = super.getB().getCopy(thisObject);			
		}
		else{
			value = super.getB().get(thisObject);
		}

		try {
			super.getB().set( thisObject, value );
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
			
		return true;
	}	
	
}
