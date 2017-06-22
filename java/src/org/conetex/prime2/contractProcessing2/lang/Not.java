package org.conetex.prime2.contractProcessing2.lang;

public class Not implements BooleanExpression{

	public static Not create(BooleanExpression theSub){
		if(theSub == null){
			return null;
		}
		return new Not(theSub);
	}
		
	private BooleanExpression sub;
	
	private Not(BooleanExpression theSub){
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
