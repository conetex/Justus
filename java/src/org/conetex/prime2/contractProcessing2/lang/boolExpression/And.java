package org.conetex.prime2.contractProcessing2.lang.boolExpression;

import org.conetex.prime2.contractProcessing2.lang.BoolExpression;

public class And implements BoolExpression{

	public static And create(BoolExpression theA, BoolExpression theB){
		if(theA == null || theB == null){
			return null;
		}
		return new And(theA, theB);
	}
		
	private BoolExpression a;
	private BoolExpression b;
	
	private And(BoolExpression theA, BoolExpression theB){
		this.a = theA;
		this.b = theB;
	}

	@Override
	public boolean compute() {
		boolean a = this.a.compute();
		boolean b = this.b.compute();
		if( a && b ){
			return true;
		}		
		return false;
	}
	
}
