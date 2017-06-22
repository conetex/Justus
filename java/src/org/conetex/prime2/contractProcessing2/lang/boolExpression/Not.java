package org.conetex.prime2.contractProcessing2.lang.boolExpression;

import org.conetex.prime2.contractProcessing2.lang.BoolExpression;

public class Not implements BoolExpression{

	public static Not create(BoolExpression theSub){
		if(theSub == null){
			return null;
		}
		return new Not(theSub);
	}
		
	private BoolExpression sub;
	
	private Not(BoolExpression theSub){
		this.sub = theSub;
	}

	@Override
	public boolean compute() {
		boolean b = this.sub.compute();
		if( b ){
			return false;
		}		
		return true;
	}	
	
}
