package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.ComputablePair;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class Equals<T> extends ComputablePair<T> implements Accessible<Boolean>{

	public static <V> Equals<V> create(Accessible<V> theA, Accessible<V> theB){
		if(theA == null || theB == null){
			return null;
		}
		return new Equals<V>(theA, theB);
	}
			
	private Equals(Accessible<T> theA, Accessible<T> theB){
		super(theA, theB);
	}

	@Override
	public Boolean get(Structure thisObject) {
		T a = super.getA().get(thisObject);
		T b = super.getB().get(thisObject);
		if( a == b ){
			return Boolean.TRUE;
		}		
		return Boolean.FALSE;
	}

	@Override
	public void set(Structure thisObject, Boolean value) throws Invalid {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Boolean copy(Structure thisObject) {
		return this.get(thisObject);
	}

	@Override
	public boolean compute(Structure thisObject) {
		get(thisObject); // TODO compute ist nur fürs debuggen ... ansonsten ist das ja sinnlos hier!
		return true;
	}
	
}
