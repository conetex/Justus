package com.conetex.contract.lang.value;

import java.util.LinkedList;

import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.Type;
import com.conetex.contract.runNew.Writer;

public abstract class PrimitiveValue<T> implements Value<T>{
	
	// TODO dies wird hoffentlich nicht gebraucht. Loeschen!
	protected final CodeNode node;
	
	protected PrimitiveValue(CodeNode theNode){
		this.node = theNode;
	}
	
	public static final String[] params = new String[] { Symbols.paramName(), Symbols.paramValue(), Symbols.paramType() };
	
	@Override
	public void persist(Writer w, Attribute<?> a) throws UnknownCommandParameter, UnknownCommand {
		String name = a.getLabel().get();
		Type<?> t = a.getType();
		String type = t.getName();
		T value = this.get();
		if(value == null) {
			CodeNode cn = new CodeNode(Symbols.comValue(), new String[] {name, null, type}, new LinkedList<>());
			w.write(cn);
		}
		else {
			CodeNode cn = new CodeNode(Symbols.comValue(), new String[] {name, value.toString(), type}, new LinkedList<>());
			w.write(cn);
		}
	}

}
