package org.conetex.prime2.contractProcessing2.interpreter.build;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing2.data.Attribute;
import org.conetex.prime2.contractProcessing2.data.AttributeComplex;
import org.conetex.prime2.contractProcessing2.data.AttributePrimitive;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Attribute.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.Attribute.NullIdentifierException;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.type.Complex.ComplexWasInitializedExeption;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.interpreter.SyntaxNode;
import org.conetex.prime2.contractProcessing2.interpreter.build.functions.Functions;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleConstant;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;
import org.conetex.prime2.contractProcessing2.lang._AccessibleValueNew;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.assignment.Copy;
import org.conetex.prime2.contractProcessing2.lang.assignment.Reference;
import org.conetex.prime2.contractProcessing2.lang.bool.expression._Comparison;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.ComparisonNumber;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.ComparisonString;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Binary;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Not;
import org.conetex.prime2.contractProcessing2.lang.control.function.Call;
import org.conetex.prime2.contractProcessing2.lang.control.function._Function;
import org.conetex.prime2.contractProcessing2.lang.control.function.FunctionNew;
import org.conetex.prime2.contractProcessing2.lang.control.function.Return;
import org.conetex.prime2.contractProcessing2.lang.math._ElementaryArithmetic;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic2;
import org.conetex.prime2.contractProcessing2.runtime.Program;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class BuildMain {
	
	public static List<Complex> create(SyntaxNode r2) throws ParserConfigurationException, SAXException, IOException, Invalid {

		List<Complex> complexTyps = Types.createComplexTypes(r2);
System.out.println("Builder " + r2.getTag());
		if(complexTyps != null){
			Complex complexTypeRoot = Complex.getInstance( r2.getName() );
			Structure v = complexTypeRoot.createValue(null);
			List<Value<?>> values = Values.createValues(r2, complexTypeRoot, v);
			/* old
			Value<?>[] theValues = new Value<?>[ values.size() ];
			values.toArray( theValues );
			v.set(theValues);
			*/

			
	
			
			
			
			
			
						List<Accessible<?>> functions = Functions.createFunctions(r2, complexTypeRoot);
						
						Accessible<?>[] theSteps = new Accessible<?>[ functions.size() ];
						Accessible<?> main = FunctionNew.createObj(//data, 
								functions.toArray( theSteps ), complexTypeRoot.getName() ); // "contract4u"
						main.getFrom(v);
/*						
						for(Accessible<?> f : functions){
							if(f instanceof Function<?>){
								//continue;
							}
							Object re = f.getFrom(v);
							if(re != null){							
System.out.println("Builder function ==> " + f + " -> " + re.toString());
}
else{
System.out.println("Builder function ==> " + f + " -> " + re);
}							
						}
System.out.println("Builder " + r2.getNodeName());
*/
		}
		
		return complexTyps;
		
	}	
	

	
}
