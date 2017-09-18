package com.conetex.contract.lang.control.function;

import java.util.HashMap;
import java.util.Map;

import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.lang.Accessible;

public class Function<V> extends Accessible<V> {

    private static Map<String, Function<? extends Number>> instancesNum = new HashMap<String, Function<? extends Number>>();

    private static Map<String, Function<Boolean>> instancesBoolean = new HashMap<String, Function<Boolean>>();

    private static Map<String, Function<String>> instancesString = new HashMap<String, Function<String>>();
    
    private static Map<String, Function<? extends Structure>> instancesStructure = new HashMap<String, Function<? extends Structure>>();
    
    private static Map<String, Function<?>> instancesObject = new HashMap<String, Function<? extends Object>>();
    
    public static Accessible<Boolean> getInstanceBool(String name) {
        Function<Boolean> f = instancesBoolean.get(name);
        if (f == null) {
            return null;
        }
        return f;
    }

    public static Accessible<? extends Number> getInstanceNum(String name) {
        Function<? extends Number> f = instancesNum.get(name);
        if (f == null) {
            return null;
        }
        return f;
    }

    public static Accessible<? extends Structure> getInstanceStructure(String name) {
        Function<? extends Structure> f = instancesStructure.get(name);
        if (f == null) {
            return null;
        }
        return f;
    }
    
    public static Accessible<? extends Object> getInstanceWhatEver(String name) {
        Function<? extends Number> fn = instancesNum.get(name);
        if (fn != null) {
            return fn;
        }
        Function<? extends Boolean> fb = instancesBoolean.get(name);
        if (fb != null) {
            return fb;
        }
        Function<? extends String> fs = instancesString.get(name);
        if (fs != null) {
            return fs;
        }
        Function<? extends Structure> fstruct = instancesStructure.get(name);
        if (fstruct != null) {
            return fstruct;
        }
        return null;
    }

    public static Accessible<?> getInstance(String name) {
        Function<?> f = instancesNum.get(name);
        if (f == null) {
            f = instancesBoolean.get(name);
            ;
        }
        return f;
    }

    public static <SV extends Number> Function<SV> createNum(Accessible<?>[] theSteps, String theName) {
        if (theSteps == null) {
            System.err.println("theSteps is null");
            return null;
        }
        if (theName == null || theName.length() < 1) {
            System.err.println("theName is null");
            return null;
        }
        if (Function.instancesNum.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }
        Function<SV> re = new Function<SV>(theSteps, theName);
        Function.instancesNum.put(theName, re);
        return re;
    }

    public static Function<Boolean> createBool(Accessible<?>[] theSteps, String theName) {
        if (theSteps == null) {
            System.err.println("theSteps is null");
            return null;
        }
        if (theName == null || theName.length() < 1) {
            System.err.println("theName is null");
            return null;
        }
        if (Function.instancesBoolean.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }
        Function<Boolean> re = new Function<Boolean>(theSteps, theName);
        Function.instancesBoolean.put(theName, re);
        return re;
    }

    public static <SV extends Structure> Function<SV> createStructure(Accessible<?>[] theSteps, String theName) {
        if (theSteps == null) {
            System.err.println("theSteps is null");
            return null;
        }
        if (theName == null || theName.length() < 1) {
            System.err.println("theName is null");
            return null;
        }
        if (Function.instancesStructure.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }
        Function<SV> re = new Function<SV>(theSteps, theName);
        Function.instancesStructure.put(theName, re);
        return re;
    }

    public static Function<Object> createWhatEver(Accessible<?>[] theSteps, String theName) {
        if (theSteps == null) {
            System.err.println("theSteps is null");
            return null;
        }
        if (theName == null || theName.length() < 1) {
            System.err.println("theName is null");
            return null;
        }
        if (Function.instancesStructure.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }
        if (Function.instancesBoolean.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }
        if (Function.instancesString.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }
        if (Function.instancesNum.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }   
        if (Function.instancesObject.containsKey(theName)) {
            System.err.println("duplicate function " + theName);
            return null;
        }
        Function<Object> re = new Function<Object>(theSteps, theName);
        Function.instancesObject.put(theName, re);
        return re;
    }    
    
    /*
     * public static <SV extends Value<?>> Function<SV> create(Structure theData,
     * Accessible<?>[] theSteps){ if(theData == null || theSteps == null){ return
     * null; } return new Function<SV>(theData, theSteps); }
     */
    // private Value<?>[] values;
    // private String[] valueNames;
    // private Structure data;

    private String name;

    @Override
    public String toString() {
        return "function " + this.name;
    }

    private Accessible<?>[] steps;

    private Function(// Structure theData,
            Accessible<?>[] theSteps, String theName) {
        // this.data = theData;
        this.steps = theSteps;
        this.name = theName;
    }

    @Override
    public V getFrom(Structure thisObject) {
        System.out.println("Function getFrom " + this.name);
        Structure thisObjectB = (Structure) thisObject.getValue(this.name);
        if (thisObjectB == null) {
            System.err.println("Function Structure getFrom: no access to data for function " + this.name);
            return null;
        }
        for (int i = 0; i < this.steps.length; i++) {
            if (this.steps[i].getClass() == Return.class) {
                // TODO der cast ist scheiße!
                // return (V) this.steps[i].getFrom(thisObject);
                System.out.println("MOCK return: " + this.steps[i].getFrom(thisObjectB));
                return null;
            }
            System.out.println("MOCK: " + this.steps[i].getFrom(thisObjectB));
            // this.steps[i].getFrom(thisObjectB);
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
