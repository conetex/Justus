package org.conetex.prime2.contractProcessing2.lang.bool.expression;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleAbstract;

public class IsNull extends AccessibleAbstract<Boolean>{

	public static IsNull create(Accessible<?> theSub){
		if(theSub == null){
			return null;
		}
		return new IsNull(theSub);
	}
		
	private Accessible<?> sub;
	
	private IsNull(Accessible<?> theSub){
		this.sub = theSub;
	}

	@Override
	public Boolean getFrom(Structure thisObject) {
		Object b = this.sub.getFrom(thisObject);
		if( b == null ){
			return Boolean.TRUE;
		}		
		return Boolean.FALSE;
	}

	@Override
	public Boolean copyFrom(Structure thisObject) {
		return this.getFrom(thisObject);
	}

	@Override
	public Class<Boolean> getBaseType() {
		return Boolean.class;
	}

}