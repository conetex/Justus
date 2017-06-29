package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;

public class Not implements AbstractBooleanExpression{

	public static Not create(AbstractBooleanExpression theSub){
		if(theSub == null){
			return null;
		}
		return new Not(theSub);
	}
		
	private AbstractBooleanExpression sub;
	
	private Not(AbstractBooleanExpression theSub){
		this.sub = theSub;
	}

	
	public boolean _compute() {
		boolean b = this.sub._compute();
		if( b ){
			return false;
		}		
		return true;
	}


	
}
