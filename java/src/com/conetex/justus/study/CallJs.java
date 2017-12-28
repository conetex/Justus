package com.conetex.justus.study;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

class CallJs {

	public static void main(String[] args) throws ScriptException, NoSuchMethodException {

		String script = "var MyJavaClass = Java.type('org.conetex.prime2.study.CallJs');"

				+ "function fun1(name) {" + "    print('Hi there from Javascript, ' + name);" + "    return \"Hi \" + name + \"! greetings from javascript \";"
				+ "};"

				+ "var fun2 = function (object) {" + "var result = object.fun1('John Doe');" // does
																								// not
																								// work
				+ "return result;"
				// + "print(result);"
				// +" print(\" JS Class Definition: \" +
				// Object.prototype.toString.call(object));"
				+ "};";

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		engine.eval(script);

		Invocable invocable = (Invocable) engine;

		// Object result = invocable.invokeFunction("fun1", "Peter Parker");
		// Object result = invocable.invokeFunction("fun2", new Date());

		// invocable.invokeFunction("fun2", LocalDateTime.now()); // works

		Object result = invocable.invokeFunction("fun2", new CallJs());
		System.out.println(result); // works

		// [object com.winterbe.java8.Person]
		// System.out.println(result);
		// System.out.println(result.getClass());

	}

	public static String fun1(String name) {
		System.out.format("Hi there from Java, %s", name);
		return "greetings from java";
	}
}
