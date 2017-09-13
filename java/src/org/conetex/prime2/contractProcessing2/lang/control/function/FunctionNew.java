package org.conetex.prime2.contractProcessing2.lang.control.function;

import java.util.HashMap;
import java.util.Map;

import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;

public class FunctionNew<V> implements Accessible<V>{
	
	private static Map<String, FunctionNew<? extends Number>> instancesNum = new HashMap<String, FunctionNew<? extends Number>>();
	private static Map<String, FunctionNew<Boolean>> instancesBoolean = new HashMap<String, FunctionNew<Boolean>>();

	public static Accessible<Boolean> getInstanceBool(String name) {
		FunctionNew<Boolean> f = instancesBoolean.get(name);
		if(f == null){
			return null;
		}
		return f;
	}
	
	public static Accessible<? extends Number> getInstanceNum(String name) {
		FunctionNew<? extends Number> f = instancesNum.get(name);
		if(f == null){
			return null;
		}
		return f;
	}	
	
	public static Accessible<?> getInstance(String name) {
		FunctionNew<?> f = instancesNum.get(name);
		if(f == null){
			f = instancesBoolean.get(name);;
		}
		return f;
	}	
	
	public static <SV extends Number> FunctionNew<SV> createNum( Accessible<?>[] theSteps, String theName){
		if(theSteps == null){
			System.err.println("theSteps is null");
			return null;
		}
		if(theName == null || theName.length() < 1){
			System.err.println("theName is null");
			return null;
		}		
		if(FunctionNew.instancesNum.containsKey(theName)){
			System.err.println("duplicate function " + theName);
			return null;
		}
		FunctionNew<SV> re = new FunctionNew<SV>(theSteps, theName);
		FunctionNew.instancesNum.put(theName, re);
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
	
	public String toString(){
		return "function " + this.name;
	}
	
	private Accessible<?>[] steps;
	
	private FunctionNew(//Structure theData, 
			Accessible<?>[] theSteps, String theName){
		//this.data = theData;
		this.steps = theSteps;
		this.name = theName;
	}

	@Override
	public V getFrom(Structure thisObject) {
System.out.println("Function getFrom " + this.name);
		Structure thisObjectB = (Structure) thisObject.getValue(this.name);
		if(thisObjectB == null){
System.err.println("Function Structure getFrom: no access to data for function " + this.name);			
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
