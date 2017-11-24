package com.conetex.contract.build;

import java.util.List;

import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.EmptyLabelException;
import com.conetex.contract.build.exceptionFunction.NullLabelException;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.function.control.Function;
import com.conetex.contract.lang.type.Attribute;
import com.conetex.contract.lang.type.TypeComplex;
import com.conetex.contract.lang.type.TypeComplexOfFunction;
import com.conetex.contract.lang.type.TypePrimitive;
import com.conetex.contract.lang.value.implementation.Structure;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;
import com.conetex.contract.runNew.Main;
import com.conetex.contract.runNew.Writer;

public class Build{

	public static Main create(CodeNode code) throws AbstractInterpreterException {
		TypePrimitive.init();
		List<TypeComplex> complexTyps = BuildTypes.createComplexTypes(code);
		System.out.println("Builder " + code.getCommand());
		if(complexTyps != null){
			TypeComplex complexTypeRoot = TypeComplex.getInstance(code.getParameter(Symbols.paramName()));
			Structure rootStructure = complexTypeRoot.createValue(null);
			if(rootStructure != null){
				BuildValues.createValues(code, complexTypeRoot, rootStructure);
				rootStructure.fillMissingValues();
				TypeComplexOfFunction.fillMissingPrototypeValues();
				Function<?> mainFunction = BuildFunctions.build(code, complexTypeRoot);
				if(mainFunction != null){
					return new Main(){
						@Override
						public void run(Writer w) throws AbstractRuntimeException, UnknownCommandParameter, UnknownCommand, NullLabelException, EmptyLabelException {
							mainFunction.getFromRoot(rootStructure);
							if(w != null){
								CodeNode cnTyps = complexTypeRoot.createCodeNode();
								w.write(cnTyps);
								/*
								for(TypeComplex tc : TypeComplex.getInstances()){
									if( tc.getCommand().equals( TypeComplex.staticGetCommand() ) ){
										CodeNode cnTyps = tc.createCodeNode();
										w.write(cnTyps);
									}
									// else // functions are done by tc.createCodeNode() internal
								}
								*/
								/*
								for(TypeComplexOfFunction tcf : TypeComplexOfFunction.getInstances()){
									CodeNode cnTyps = tcf.persist();
									w.write(cnTyps);
								}
								*/			
								Attribute<?> r = complexTypeRoot.createComplexAttribute(complexTypeRoot.getName());
								CodeNode cnVal = rootStructure.createCodeNode(r);
								w.write(cnVal);								
								/*
								List<CodeNode> cnVals = rootStructure.createCodeNodes();
								for(CodeNode cnVal : cnVals){
									w.write(cnVal);
								}
								*/
							}
							else{
								// TODO exception ...
							}
						}
					};
				}
			}
		}
		return null;
	}

}