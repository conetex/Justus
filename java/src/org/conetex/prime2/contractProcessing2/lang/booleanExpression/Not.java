package org.conetex.prime2.contractProcessing2.lang.booleanExpression;

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

	@Override
	public boolean compute() {
		boolean b = this.sub.compute();
		if( b ){
			return false;
		}		
		return true;
	}	
	
}
