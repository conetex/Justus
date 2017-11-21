package com.conetex.contract.build;

import java.util.List;

import com.conetex.contract.build.exceptionFunction.AbstractInterpreterException;
import com.conetex.contract.build.exceptionFunction.UnknownCommand;
import com.conetex.contract.build.exceptionFunction.UnknownCommandParameter;
import com.conetex.contract.lang.function.control.Function;
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
						public void run(Writer w) throws AbstractRuntimeException, UnknownCommandParameter, UnknownCommand {
							mainFunction.getFromRoot(rootStructure);
							if(w != null){
								CodeNode cn = rootStructure.persist(w, null);
								w.write(cn);
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