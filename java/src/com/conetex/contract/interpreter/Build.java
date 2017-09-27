package com.conetex.contract.interpreter;

import java.util.List;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.interpreter.exception.OperationInterpreterException;
import com.conetex.contract.lang.access.Accessible;

public class Build {

	public static List<Complex> create(CodeNode r2) throws Invalid // TODO wird
																	// das
																	// wirklich
																	// geworfen?
			, OperationInterpreterException {

		List<Complex> complexTyps = BuildTypes.createComplexTypes(r2);
		System.out.println("Builder " + r2.getCommand());
		if (complexTyps != null) {
			Complex complexTypeRoot = Complex.getInstance(r2.getName());
			Structure v = complexTypeRoot.createValue(null);
			// List<Value<?>> values =
			BuildValues.createValues(r2, complexTypeRoot, v);
			Accessible<?> mainNEW = BuildFunctions.build(r2, complexTypeRoot);
			mainNEW.getFrom(v);
		}

		return complexTyps;

	}

}
