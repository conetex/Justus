package org.conetex.prime2.contractProcessing2.lang.boolExpression;

import org.conetex.prime2.contractProcessing2.data.Variable;
import org.conetex.prime2.contractProcessing2.lang.BoolExpression;

public class BooleanVar implements BoolExpression{

	public static BooleanVar create(Variable<Boolean> theVar){
		if(theVar == null){
			return null;
		}
		return new BooleanVar(theVar);
	}

	private Variable<Boolean> var;
		
	private BooleanVar(Variable<Boolean> theVar){
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
