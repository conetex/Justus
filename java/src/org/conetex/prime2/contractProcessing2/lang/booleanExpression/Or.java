package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

public class Or implements AbstractBooleanExpression{

	public static Or create(AbstractBooleanExpression theA, AbstractBooleanExpression theB){
		if(theA == null || theB == null){
			return null;
		}
		return new Or(theA, theB);
	}
		
	private AbstractBooleanExpression a;
	private AbstractBooleanExpression b;
	
	private Or(AbstractBooleanExpression theA, AbstractBooleanExpression theB){
		this.a = theA;
		this.b = theB;
	}

	@Override
	public boolean compute() {
		boolean a = this.a.compute();
		boolean b = this.b.compute();
		if( a || b ){
			return true;
		}		
		return false;
	}
	
}
