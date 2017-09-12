package org.conetex.prime2.contractProcessing2.lang.control.function;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValueNew;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic;

public class Call<V>  implements Accessible<V>{ // V extends Value<?>

	public static <SV> Call<SV> create2(Accessible<SV> theFunction, AccessibleValueNew<Structure> theReference){
		// TODO drop this
		if(theFunction == null || theReference == null){
			return null;
		}
		return new Call<SV>(theFunction, theReference);
	}
	
	public static <SV extends Value<?>> Call<SV> create(Accessible<SV> theFunction, AccessibleValueNew<Structure> theReference){
		if(theFunction == null || theReference == null){
			return null;
		}
		return new Call<SV>(theFunction, theReference);
	}	
	
	private Accessible<V> function;
	
	private AccessibleValueNew<Structure> reference;
	
	public String toString(){
		return "call function " + this.function;
	}	
	
	private Call(Accessible<V> theExpression, AccessibleValueNew<Structure> theReference){
		this.function = theExpression;
		this.reference = theReference;
	}	
	
	@Override
	public V getFrom(Structure thisObject) {
		
		Structure obj = this.reference.getFrom(thisObject);
		if(obj == null){
			System.err.println("Call getFrom ERROR" );
			return null;
		}
		return this.function.getFrom(obj);
		
		
		//return this.function.getFrom(thisObject);
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO copyFrom is obsolet
		return null;
	}

	@Override
	public Class<V> getBaseType() {
		// TODO V ist oft noch java typ. das sollte sich jetzt nach Value ändern. Hier ist es schon so: V extends Value<?>
		return null;
	}

}
