package com.conetex.contract.build;

import java.util.List;

import com.conetex.contract.build.BuildFunctions.Data;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.data.Attribute;
import com.conetex.contract.data.AttributeComplex;
import com.conetex.contract.data.AttributePrimitive;
import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.AbstractType;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.FunctionAttributes;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.run.exceptionValue.Invalid;

public class BuildValues{

	public static List<Value<?>> createValues(CodeNode n, Complex type, Structure data) throws AbstractInterpreterException {
		String name = n.getCommand();
		if(type == null){
			System.err.println("can not recognize type of " + name);
			return null;
		}

		for(CodeNode c : n.getChildNodes()){
			Data.complex.valueCreateChild(c, type, data);
		}

		data.fillMissingValues();
		return null;
	}

	public static Value<?> createValue(CodeNode n, Complex parentTyp, Structure parentData) throws AbstractInterpreterException {
		String name = n.getParameter(Symbols.paramName());

		Attribute<?> id = parentTyp.getSubAttribute(name); //

		if(id == null){
			System.err.println("createValue: can not identify " + name);
			return null;
		}

		AbstractType<?> type = id.getType();
		if(type.getClass() == Complex.class){
			Structure re = ((AttributeComplex) id).createValue(parentData);
			// new
			createValues(n, (Complex) type, re);
			try{
				parentData.set(name, re);
			}
			catch(Invalid e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return re;
		}
		else{
			String valueNode = n.getParameter(Symbols.paramValue());
			System.out.println("createValue " + name + " " + valueNode);
			if(valueNode != null){
				Value<?> re = ((AttributePrimitive<?>) id).createValue(valueNode, parentData);
				try{
					parentData.set(name, re);
				}
				catch(Invalid e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return re;
			}
		}

		return null;
	}

	public static Value<?> createValue4Function(CodeNode n, Complex parentTyp, Structure parentData) throws AbstractInterpreterException {

		String name = n.getParameter(Symbols.paramName());

		FunctionAttributes type = FunctionAttributes.getInstance(parentTyp.getName() + "." + name);
		if(type != null){
			Structure re = Structure.create(type, null);
			// new
			createValues(n, (Complex) type, re);
			try{
				// TODO hier passier garnix weil parentData keinen platz für die funktion hat...
				parentData.set(name, re);
			}
			catch(Invalid e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// TODO der cast ist scheißßß
			((FunctionAttributes) type).setPrototype(re);

			return re;
		}

		return null;
	}

}
