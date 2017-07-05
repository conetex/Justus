package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;

public abstract class AbstractAssigment<T> extends ComputablePair<T>{
		
	protected AbstractAssigment(AccessibleValue<T> trg, AccessibleValue<T> src){
		super(trg, src);
	}

	public abstract boolean doCopy();
	
	public boolean compute(Structure thisObject)  {
		T value = null;
		try {
			
			if(this.doCopy()){
				value = super.getB().copyFrom(thisObject);			
			}
			else{
				value = super.getB().getFrom(thisObject);
			}

			super.getB().setTo( thisObject, value );
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
			
		return true;
	}	
	
}
