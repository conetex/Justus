package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.Setable;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;
import org.conetex.prime2.contractProcessing2.lang.Computable;

public abstract class AbstractAssigment<T> implements Computable{// extends ComputablePair<T>{

	private Setable<T> target;

	private Accessible<T> source;
	
	protected AbstractAssigment(Setable<T> trg, Accessible<T> src){
		this.source = src;
		this.target = trg;
	}

	public abstract boolean doCopy();
	
	public boolean compute(Structure thisObject)  {
		T value = null;
		try {
			
			if(this.doCopy()){
				value = this.source.copyFrom(thisObject);			
			}
			else{
				value = this.source.getFrom(thisObject);
			}

			this.target.setTo( thisObject, value );
			
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
			
		return true;
	}	
	
}
