package org.conetex.prime2.study;

import java.time.LocalDateTime;
import java.util.Date;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.conetex.prime2.contractProcessing.Asset;

public class CallJs {

	public static void main(String[] args) throws ScriptException, NoSuchMethodException{
	
		String script = 
		"var MyJavaClass = Java.type('org.conetex.prime2.study.CallJs');"

		+"function fun1(name) {"
		+"    print('Hi there from Javascript, ' + name);"
		+"    return \"greetings from javascript\";"
		+"};"

		+"var fun2 = function (object) {"
		//+ "var result = MyJavaClass.fun1('John Doe');"
		//+ "print(result);"
		+"    print(\"  JS Class Definition: \" + Object.prototype.toString.call(object));"
		+"};"
		;
		
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(script);

		Invocable invocable = (Invocable) engine;

		//Object result = invocable.invokeFunction("fun1", "Peter Parker");
		//Object result = invocable.invokeFunction("fun2", new Date());
		 invocable.invokeFunction("fun2", LocalDateTime.now());
		//Object result = invocable.invokeFunction("fun2", new Asset());
		// [object com.winterbe.java8.Person]
		//System.out.println(result);
		//System.out.println(result.getClass());
		
	}
	
	static String fun1(String name) {
	    System.out.format("Hi there from Java, %s", name);
	    return "greetings from java";
	}
}
