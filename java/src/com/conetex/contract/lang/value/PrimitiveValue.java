package com.conetex.contract.lang.value;

import java.math.BigInteger;
import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.build.CodeModel;
import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbols;
import com.conetex.contract.build.CodeModel.Egg;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.Type;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.Inconvertible;
import com.conetex.contract.run.exceptionValue.Invalid;
import com.conetex.contract.run.exceptionValue.ValueCastException;
import com.conetex.contract.lang.value.Value;

public abstract class PrimitiveValue<T> implements Value<T>{
	
	protected CodeNode node;
	
	protected PrimitiveValue(CodeNode theNode){
		this.node = theNode;
	}
	
	public static final String[] params = new String[] { Symbols.paramName(), Symbols.paramValue(), Symbols.paramType() };
	
	@Override
	public void persist(Attribute<?> a) throws UnknownCommandParameter, UnknownCommand {
		String name = a.getLabel().get();
		Type<?> t = a.getType();
		String type = t.getName();
		T value = this.get();
		if(value == null) {
			CodeNode cn = new CodeNode(Symbols.comValue(), new String[] {name, null, type}, new LinkedList<>());
			//return this.type.createValue(cn);
		}
		else {
			CodeNode cn = new CodeNode(Symbols.comValue(), new String[] {name, value.toString(), type}, new LinkedList<>());
			//return this.type.createValue(cn);			
		}
	}

}
