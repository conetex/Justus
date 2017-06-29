package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

public class And implements AbstractBooleanExpression{

	public static And create(AbstractBooleanExpression theA, AbstractBooleanExpression theB){
		if(theA == null || theB == null){
			return null;
		}
		return new And(theA, theB);
	}
		
	private AbstractBooleanExpression a;
	private AbstractBooleanExpression b;
	
	private And(AbstractBooleanExpression theA, AbstractBooleanExpression theB){
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
