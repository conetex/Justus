package com.conetex.contract.build;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.conetex.contract.build.BuildFunctions.Assign;
import com.conetex.contract.build.BuildFunctions.Constant;
import com.conetex.contract.build.BuildFunctions.Control;
import com.conetex.contract.build.BuildFunctions.Data;
import com.conetex.contract.build.BuildFunctions.Expression;
import com.conetex.contract.build.BuildFunctions.Fun;
import com.conetex.contract.build.BuildFunctions.FunCall;
import com.conetex.contract.build.BuildFunctions.FunReturn;
import com.conetex.contract.build.BuildFunctions.Reference;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.build.exceptionLang.OperationMeansNotCalled;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.type.FunctionAttributes;
import com.conetex.contract.data.value.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.lang.control.Function;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class Build {

	public abstract static class Main {
		public abstract void run() throws AbstractRuntimeException;
	}

	public static Main create(CodeNode code) throws AbstractInterpreterException {
		
		CodeModel.build();
		
		List<Complex> complexTyps = BuildTypes.createComplexTypes(code);
		System.out.println("Builder " + code.getCommand());
		if (complexTyps != null) {
			Complex complexTypeRoot = Complex.getInstance(code.getName());
			Structure rootStructure = complexTypeRoot.createValue(null);
			if (rootStructure != null) {
				BuildValues.createValues(code, complexTypeRoot, rootStructure);
				rootStructure.fillMissingValues();

				FunctionAttributes.fillMissingPrototypeValues();

				Function<?> mainFunction = BuildFunctions.build(code, complexTypeRoot);
				if (mainFunction != null) {
					return new Main() {
						@Override
						public void run() throws AbstractRuntimeException {
							mainFunction.getFromRoot(rootStructure);
							// mainFunction.getFrom(rootStructure);
						}
					};
				}
			}
		}

		return null;

	}

	
	

	
}
