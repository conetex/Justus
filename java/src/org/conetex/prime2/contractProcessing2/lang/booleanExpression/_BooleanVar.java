package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;

public class _BooleanVar implements _AbstractBooleanExpression{

	public static _BooleanVar create(Value<Boolean> theVar){
		if(theVar == null){
			return null;
		}
		return new _BooleanVar(theVar);
	}

	private Value<Boolean> var;
		
	private _BooleanVar(Value<Boolean> theVar){
		this.var = theVar;
	}

	public boolean _compute() {
		Boolean b = this.var.get();
		if( b != null && b.booleanValue() ){
			return true;
		}		
		return false;
	}

	@Override
	public void set(Structure thisObject, Object value) throws Invalid {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean get(Structure thisObject) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void set(Structure thisObject, Boolean value) throws Invalid {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean copy(Structure thisObject) {
		// TODO Auto-generated method stub
		return null;
	}


	
}
