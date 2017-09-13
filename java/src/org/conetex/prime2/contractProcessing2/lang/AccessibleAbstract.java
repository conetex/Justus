package org.conetex.prime2.contractProcessing2.lang;

import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public abstract class AccessibleAbstract<T> implements Accessible<T>{
	
	@Override
	public abstract T getFrom(Structure thisObject);

	@Override
	public abstract T copyFrom(Structure thisObject) throws Invalid;
		
	@Override
	public abstract Class<T> getBaseType();
    
    @Override
	public Accessible<T> as(Class<?> baseType) {
		if(baseType == this.getBaseType()){
			return this;
		}
		return null;
	}
    
	public <X> Accessible<? extends X> as2(Class<X> baseType) {
		if(baseType.isAssignableFrom(this.getBaseType())){
			return (Accessible<? extends X>) this;
		}
		return null;
	}
	
}
