package org.conetex.prime2.contractProcessing2.lang;

public class And implements BooleanExpression{

	public static And create(BooleanExpression theA, BooleanExpression theB){
		if(theA == null || theB == null){
			return null;
		}
		return new And(theA, theB);
	}
		
	private BooleanExpression a;
	private BooleanExpression b;
	
	private And(BooleanExpression theA, BooleanExpression theB){
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
