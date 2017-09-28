package com.conetex.contract.build;

import java.util.List;

import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.lang.access.Accessible;
import com.conetex.contract.runtime.exceptionValue.Invalid;
import com.conetex.contract.runtime.exceptionValue.ValueCastException;

public class Build {

	public static List<Complex> create(CodeNode r2) throws Invalid // TODO wird
																	// das
																	// wirklich
																	// geworfen?
			, AbstractInterpreterException, ValueCastException {

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
