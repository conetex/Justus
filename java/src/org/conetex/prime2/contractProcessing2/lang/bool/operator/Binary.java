package org.conetex.prime2.contractProcessing2.lang.bool.operator;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public abstract class Binary extends ComputablePair<Boolean> implements Accessible<Boolean>{

	public static Binary createAdd(Accessible<Boolean> theA, Accessible<Boolean> theB){
		if(theA == null || theB == null){
			return null;
		}
		return new Binary(theA, theB){
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if( a && b ){
						return Boolean.TRUE;
					}		
					return Boolean.FALSE;
				}
			};
	}
	
	public static Binary createOr(Accessible<Boolean> theA, Accessible<Boolean> theB){
		if(theA == null || theB == null){
			return null;
		}
		return new Binary(theA, theB){
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if (a || b) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
	}	
	
	public static Binary createXOr(Accessible<Boolean> theA, Accessible<Boolean> theB){
		if(theA == null || theB == null){
			return null;
		}
		return new Binary(theA, theB){
				@Override
				protected Boolean calc(Boolean a, Boolean b) {
					if (a ^ b) {
						return Boolean.TRUE;
					}
					return Boolean.FALSE;
				}
			};
	}		
			
	private Binary(Accessible<Boolean> theA, Accessible<Boolean> theB){
		super(theA, theB);
	}

	protected abstract Boolean calc(Boolean a, Boolean b);
	
	@Override
	public Boolean getFrom(Structure thisObject) {
		Boolean a = super.getA().getFrom(thisObject);
		Boolean b = super.getB().getFrom(thisObject);
		if( a == null || b == null ){
			return null;
		}
		return this.calc(a, b);
	}

	@Override
	public void setTo(Structure thisObject, Boolean value) throws Invalid {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public boolean compute(Structure thisObject) {
		getFrom(thisObject); // TODO compute ist nur fürs debuggen ... ansonsten ist das ja sinnlos hier!
		return true;
	}

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}
	
}
