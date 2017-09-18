package com.conetex.contract.interpreter;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.interpreter.functions.Factory;
import com.conetex.contract.lang.Accessible;

public class BuildMain {

    public static List<Complex> create(CodeNode r2)
            throws ParserConfigurationException, SAXException, IOException, Invalid {

        List<Complex> complexTyps = Types.createComplexTypes(r2);
        System.out.println("Builder " + r2.getTag());
        if (complexTyps != null) {
            Complex complexTypeRoot = Complex.getInstance(r2.getName());
            Structure v = complexTypeRoot.createValue(null);
            List<Value<?>> values = Values.createValues(r2, complexTypeRoot, v);

            /*
             * List<Accessible<?>> functions = Functions.createFunctions(r2,
             * complexTypeRoot); Accessible<?>[] theSteps = new
             * Accessible<?>[functions.size()]; Accessible<?> main =
             * Function.createObj(functions.toArray(theSteps), complexTypeRoot.getName());
             * main.getFrom(v);
             */

            Accessible<?> mainNEW = Factory.sbuild(r2, complexTypeRoot);
            mainNEW.getFrom(v);

        }

        return complexTyps;

    }

}
