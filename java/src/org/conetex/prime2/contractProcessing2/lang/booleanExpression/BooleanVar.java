package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.Value;

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

	@Override
	public boolean compute() {
		Boolean b = this.var.get();
		if( b != null && b.booleanValue() ){
			return true;
		}		
		return false;
	}
	
}
