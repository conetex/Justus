package org.conetex.prime2.contractProcessing2.lang.assignment;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Setable;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

/*
public interface Accessible<T> {//accessible

	public T getFrom(Structure thisObject);

	public T copyFrom(Structure thisObject) throws Invalid;
		
    public Class<T> getBaseType();
	
}
*/

public abstract class AbstractAssigment<T> implements Accessible<T>{//Computable{// extends ComputablePair<T>{

	private Setable<T> target;

	private Accessible<T> source;
	
	protected AbstractAssigment(Setable<T> trg, Accessible<T> src){
		this.target = trg;
		this.source = src;
	}

	public abstract boolean doCopy();
	
	@Override
	public T getFrom(Structure thisObject) {
		T value = null;
		try {
			
			if(this.doCopy()){
				value = this.source.copyFrom(thisObject);			
			}
			else{
				value = this.source.getFrom(thisObject);
			}

			value = this.target.setTo( thisObject, value );
			
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
			
		return value;
	}

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		// TODO: das wird ja nur selten gebraucht...
		return getFrom(thisObject);
	}

	@Override
	public Class<T> getBaseType() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
