package org.conetex.prime2.contractProcessing2.lang;

import java.math.BigInteger;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.BigInt;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Bool;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Int;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Lng;
import org.conetex.prime2.contractProcessing2.data.valueImplement.SizedASCII;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;

public class AccessibleConstant<T> extends AccessibleAbstract<T> {

	
	public static <RE> AccessibleConstant<RE> create2(Class<RE> expectedBaseTyp, String value){
		Primitive<RE> theClass = null;			
		if(expectedBaseTyp == BigInteger.class){
			theClass = Primitive.<RE>getInstance(BigInt.class);
		}
		else if(expectedBaseTyp == Long.class){
			theClass = Primitive.<RE>getInstance(Lng.class);
		}			
		else if(expectedBaseTyp == Integer.class){
			theClass = Primitive.<RE>getInstance(Int.class);
		}			
		else if(expectedBaseTyp == Byte.class){
			// TODO Typen klären ...
			return null;
		}		
		else if(expectedBaseTyp == String.class){
			theClass = Primitive.<RE>getInstance(SizedASCII.class);
		}			
		else if(expectedBaseTyp == Boolean.class){
			theClass = Primitive.<RE>getInstance(Bool.class);
		}
		if(theClass != null){
			Value<RE> constVal = theClass.createValue();
			try {
				constVal.setConverted(value);
			} catch (Inconvertible | Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			AccessibleConstant<RE> re = AccessibleConstant.<RE>create(constVal);
			return re;				
		}
		return null;		
	}
	
	private static <T> AccessibleConstant<T> create(Value<T> theValue) {
		if (theValue == null) {
			return null;
		}
		return new AccessibleConstant<T>(theValue);
	}

	private Value<T> value;


	private AccessibleConstant(Value<T> theValue) {
		this.value = theValue;
	}

	@Override
	public T getFrom(Structure thisObject) {
		return this.value.get();
	}

	/*
	@Override
	public void setTo(Structure thisObject, T newValue) throws Invalid {
		this.value.set(newValue);
	}

	public void transSet(Structure thisObject, String newValue) throws Inconvertible, Invalid {
		this.value.setConverted(newValue);
	}
	*/

	@Override
	public T copyFrom(Structure thisObject) throws Invalid {
		return this.value.copy();
	}

	@Override
	public Class<T> getBaseType() {
		return this.value.getBaseType();
	}

}
