package org.conetex.prime2.contractProcessing2.lang.control.function;

import java.util.HashMap;
import java.util.Map;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class Function<V extends Value<?>> implements Accessible<V>{

	private static Map<String, Function<?>> instances = new HashMap<String, Function<?>>();
	
	public static Function<?> getInstance(String name){
		return instances.get(name);
	}
	
	public static <SV extends Value<?>> Function<SV> create( Accessible<?>[] theSteps, String theName){
		if(theSteps == null || theName == null || theName.length() < 1){
			return null;
		}
		if(Function.instances.containsKey(theName)){
			System.err.println("duplicate function " + theName);
			return null;
		}
		Function<SV> re = new Function<SV>(theSteps, theName);
		Function.instances.put(theName, re);
		return re;
	}
	/*
	public static <SV extends Value<?>> Function<SV> create(Structure theData, Accessible<?>[] theSteps){
		if(theData == null || theSteps == null){
			return null;
		}
		return new Function<SV>(theData, theSteps);
	}
	*/
	//private Value<?>[] values;
	//private String[] valueNames;
	//private Structure data;
	
	private String name;
	
	private Accessible<?>[] steps;
	
	private Function(//Structure theData, 
			Accessible<?>[] theSteps, String theName){
		//this.data = theData;
		this.steps = theSteps;
		this.name = theName;
	}

	@Override
	public V getFrom(Structure thisObject) {
System.out.println("getFrom " + this.name);		
		Structure thisObjectB = (Structure) thisObject.getValue(this.name);
		if(thisObjectB == null){
System.err.println("no access to data for function " + this.name);			
			return null;
		}
		for(int i = 0; i < this.steps.length; i++){
			if(this.steps[i].getClass() == Return.class){
				// TODO der cast ist scheiße!
				//return (V) this.steps[i].getFrom(thisObject);
				System.out.println("MOCK return: " + this.steps[i].getFrom(thisObjectB));
				return null;
			}
			System.out.println("MOCK: " + this.steps[i].getFrom(thisObjectB));
			//this.steps[i].getFrom(thisObjectB);
		}
		return null;
	}

	@Override
	public V copyFrom(Structure thisObject) throws Invalid {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class<V> getBaseType() {
		// TODO Auto-generated method stub
		return null;
	}

}
