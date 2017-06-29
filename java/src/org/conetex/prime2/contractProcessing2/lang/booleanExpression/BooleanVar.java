package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;

public class BooleanVar implements AbstractBooleanExpression{

	public static BooleanVar create(Value<Boolean> theVar){
		if(theVar == null){
			return null;
		}
		return new BooleanVar(theVar);
	}

	private Value<Boolean> var;
		
	private BooleanVar(Value<Boolean> theVar){
		this.var = theVar;
	}

	public boolean _compute() {
		Boolean b = this.var.get();
		if( b != null && b.booleanValue() ){
			return true;
		}		
		return false;
	}


	
}
