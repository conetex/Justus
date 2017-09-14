package com.conetex.contract.interpreter.build;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.conetex.contract.data.Value;
import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.Structure;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.interpreter.SyntaxNode;
import com.conetex.contract.interpreter.build.functions.Functions;
import com.conetex.contract.lang.Accessible;
import com.conetex.contract.lang.control.function.Function;

public class BuildMain {

    public static List<Complex> create(SyntaxNode r2)
            throws ParserConfigurationException, SAXException, IOException, Invalid {

        List<Complex> complexTyps = Types.createComplexTypes(r2);
        System.out.println("Builder " + r2.getTag());
        if (complexTyps != null) {
            Complex complexTypeRoot = Complex.getInstance(r2.getName());
            Structure v = complexTypeRoot.createValue(null);
            List<Value<?>> values = Values.createValues(r2, complexTypeRoot, v);
            /*
             * old Value<?>[] theValues = new Value<?>[ values.size() ]; values.toArray(
             * theValues ); v.set(theValues);
             */

            List<Accessible<?>> functions = Functions.createFunctions(r2, complexTypeRoot);

            Accessible<?>[] theSteps = new Accessible<?>[functions.size()];
            Accessible<?> main = Function.createObj(// data,
                    functions.toArray(theSteps), complexTypeRoot.getName()); // "contract4u"
            main.getFrom(v);
            /*
             * for(Accessible<?> f : functions){ if(f instanceof Function<?>){ //continue; }
             * Object re = f.getFrom(v); if(re != null){
             * System.out.println("Builder function ==> " + f + " -> " + re.toString()); }
             * else{ System.out.println( "Builder function ==> " + f + " -> " + re); } }
             * System.out.println("Builder " + r2.getNodeName());
             */
        }

        return complexTyps;

    }

}
