package org.conetex.prime2.study;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleConstant;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.assignment.Copy;
import org.conetex.prime2.contractProcessing2.lang.assignment.Reference;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.Comparison;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.ComparisonNum;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Binary;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Not;
import org.conetex.prime2.contractProcessing2.lang.control.function.Function;
import org.conetex.prime2.contractProcessing2.lang.control.function.Return;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic;
import org.conetex.prime2.contractProcessing2.runtime.Program;
import org.conetex.prime2.study.ReadXML.FunctionBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML_func {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, Invalid {

		FileInputStream is = new FileInputStream( "input01.xml" );

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );

		List<Complex> complexTyps = null;
		//List<Value<?>> values = null; 
		//List<Accessible<?>> functions = null; 		
		
		NodeList children = document.getChildNodes();			
		for(int i = 0; i < children.getLength(); i++){	
			Node r = children.item(i);
			short typOfNode = children.item(i).getNodeType();
			if(typOfNode == Node.ELEMENT_NODE){
				if(complexTyps == null){
					complexTyps = ReadXML_types.createComplexTypes(r);
System.out.println("ReadXML_func " + r.getNodeName());
					if(complexTyps != null){
						Complex complexTypeRoot = Complex.getInstance(ReadXMLtools.getRootType(r));
						List<Value<?>> values = ReadXML_values.createValues(r, complexTypeRoot);
						Value<?>[] theValues = new Value<?>[ values.size() ];
						values.toArray( theValues );
						Structure v = complexTypeRoot.createValue();
						v.set(theValues);
						
						List<Accessible<?>> functions = ReadXML_func.createFunctions(r, complexTypeRoot);
						for(Accessible<?> f : functions){
							Object re = f.getFrom(v);
System.out.println("ReadXML_func function ==> " + re.toString());												
						}
System.out.println("ReadXML_func " + r.getNodeName());						
					}
				}
				else{
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}
		
	}	
	
	
	public static abstract class FunctionBuilder{
		protected Node node;
		protected FunctionBuilder(Node n){
			this.node = n;
		}
		public abstract Accessible<?> build(Complex d);
	}
	
	public static List<Accessible<?>> createFunctions(Node n, Complex type){
		String name = n.getNodeName();
		if(type == null){
			System.err.println("can not recognize type of " + name);
			return null;
		}
		
		List<Accessible<?>> acc = new LinkedList<Accessible<?>>();		
		
		NodeList children = n.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			if( ReadXMLtools.isFunction(c) ){
//System.out.println("createFunctions " + c.getNodeName());
				Accessible<?> v = createFunc( c, type);
				if(v != null){
					acc.add( v );
				}				
			}				
		}		
		
		return acc;
	}
	
	
	public static Accessible<?> createFunc(Node n, Complex parentTyp//, List<FunctionBuilder> functionBuilders//, List<Value<?>> values, Map<Complex, List<FunctionBuilder>> complexTyps
			){
		
		
		//NodeList children = n.getChildNodes();
//		for(int i = 0; i < children.getLength(); i++){
//			Node cn = children.item(i);
			short type = n.getNodeType();
//			System.out.println("createState2  " + n.getNodeName() + " - " + type);
			if(type == Node.ELEMENT_NODE){
				
				String name = n.getNodeName();


				if( name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES) || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS) ) {
									Accessible<? extends Number> x = //createNumExpression( super.node, theClass );
											createAccessible( n, parentTyp, Integer.class );// TODO mach das zu number ...
									return x;
				}
				else if( name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) || name.equals(Symbol.NOT) || 
						 name.equals(Symbol.SMALLER) || name.equals(Symbol.EQUAL) || name.equals(Symbol.GREATER) || name.equals(Symbol.ISNULL) ) {
									Accessible<Boolean> x = //createBoolExpression( super.node );
									//ReadXML.createAccessible( super.node, c, Boolean.class );
									createAccessible( n, parentTyp, Boolean.class );
							return x;					
				}
				else if( name.equals(Symbol.REFERENCE) || name.equals(Symbol.COPY) ) {
									Accessible<?> x = //createBoolExpression( super.node );
									//ReadXML.createAccessible( super.node, c, Boolean.class );
									createAccessible( n, parentTyp, Object.class );
							return x;					
				}				
				else if( name.equals(Symbol.FUNCTION) || name.equals(Symbol.RETURN) ){
					Accessible<?> x = //createBoolExpression( super.node );
					ReadXML.createAccessible( n, parentTyp, Object.class );
					return x;					
				}
				
				
		
				
			}				
//		}		
		

		
		//Value.Interface<?>[] theValues = new Value.Interface<?>[ values.size() ];
		//values.toArray( theValues );		
		
		return null;
		
	}

	
	public static <RE> Accessible<RE> createAccessible(Node n, Complex parentTyp, Class<RE> expectedBaseTyp){
		
		String name = n.getNodeName();
		
		// ASSIGNMENT
		if( name.equals("copy") || name.equals("ref") ) {	
		   //createAssignment(name, n, parentTyp);
			Node c0 = getChildElementByIndex(n, 0);
			System.out.println(ReadXMLtools.getNodeValue(c0));
			Identifier<RE> id0 = parentTyp.getSubIdentifier( ReadXMLtools.getNodeValue(c0) );// TODO geht nich

			Node c1 = getChildElementByIndex(n, 1);
			System.out.println(ReadXMLtools.getNodeValue(c1));
			Identifier<RE> id1 = parentTyp.<RE>getSubIdentifier( ReadXMLtools.getNodeValue(c1) );
			
			String c0DataType = ReadXMLtools.getAttribute(c0, Symbol.TYPE);
			if( c0DataType != null ){
				// TODO nicht nötig das auszulesen! Sollte aber ne Warnung erzeugen, wenns nicht zum typ des referenzierten Feld passt ...
				//Class<Value<Object>> c0ClassX = Primitive.getClass(c0DataType);
				Class<?> c0ClassX = Primitive.getClass(c0DataType);				
			}
		
			// TODO: der teil hier könnte ausgelagert werden nach AbstractAssigment
			
			Class<? extends Value<RE>> c0Class = null;
			if( id0 != null){
				AbstractType<RE> t = id0.getType();
				c0Class = t.getClazz();				
			}			
			Class<? extends Value<RE>> c1Class = null;
			if( id1 != null){
				AbstractType<RE> t = id1.getType();
				c1Class = t.getClazz();				
			}			
			
			// TODO hier reicht eigentlich gleicher base-typ !!!
			if(c0Class != null && c1Class != null && c0Class == c1Class){
				String path0 = ReadXMLtools.getNodeValue(c0);
				SetableValue<RE> trg = SetableValue.<RE>create(path0, c0Class);
				
				Primitive<RE> pri1 = Primitive.getInstance( c1Class );
				Class<RE> baseType1 = pri1.getBaseType();	
				Accessible<RE> src = createAccessible(c1, parentTyp, baseType1);
				if(src != null && trg != null){
					if(name.equals(Symbol.COPY)){
						return Copy.<RE>create(trg, src);					
					}
					if(name.equals(Symbol.REFERENCE)){
						return Reference.<RE>create(trg, src);					
					}				
				}
				return null;
			}
			else{
				// TODO: classes do not match
				return null;
			}			
		}
		// MATH
		else if( name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES) || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS) ) {
			if(expectedBaseTyp == BigInteger.class){
				Accessible<BigInteger> a = createAccessible( getChildElementByIndex(n, 0), parentTyp, BigInteger.class );
				Accessible<BigInteger> b = createAccessible( getChildElementByIndex(n, 1), parentTyp, BigInteger.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<BigInteger,RE>create(a, b, name, expectedBaseTyp );
				}				
			}
			else if(expectedBaseTyp == Long.class){
				Accessible<Long> a = ReadXML_func.<Long>createAccessible( getChildElementByIndex(n, 0), parentTyp, Long.class );
				Accessible<Long> b = ReadXML_func.<Long>createAccessible( getChildElementByIndex(n, 1), parentTyp, Long.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<Long,RE>create(a, b, name, expectedBaseTyp );
				}				
			}			
			else if(expectedBaseTyp == Integer.class){
				Accessible<Integer> a = ReadXML_func.<Integer>createAccessible( getChildElementByIndex(n, 0), parentTyp, Integer.class );
				Accessible<Integer> b = ReadXML_func.<Integer>createAccessible( getChildElementByIndex(n, 1), parentTyp, Integer.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<Integer,RE>create(a, b, name, expectedBaseTyp );
				}				
			}
			else if(expectedBaseTyp == Comparable.class || expectedBaseTyp == Object.class){
				Accessible<Number> a = createAccessible( getChildElementByIndex(n, 0), parentTyp, Number.class );
				Accessible<Number> b = createAccessible( getChildElementByIndex(n, 1), parentTyp, Number.class );
				if(a != null && b != null){
					Accessible<Number> re = ElementaryArithmetic.<Number,Number>create(a, b, name, Number.class );
					return (Accessible<RE>) re;
				}				
			}
			/*
			else if(expectedBaseTyp == Comparable.class){
				Accessible<BigInteger> a = createAccessible( getChildElementByIndex(n, 0), parentTyp, BigInteger.class );
				Accessible<BigInteger> b = createAccessible( getChildElementByIndex(n, 1), parentTyp, BigInteger.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<BigInteger,RE>create(a, b, name, expectedBaseTyp );
				}				
			}	
			*/
		}
		
		// VARIABLE
		else if( name.equals(Symbol.BOOL) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<Boolean>create2(Boolean.class, ReadXMLtools.getNodeValue(n));
		}
		else if( name.equals(Symbol.BINT) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<BigInteger>create2(BigInteger.class, ReadXMLtools.getNodeValue(n));
		}
		else if( name.equals(Symbol.INT) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<Integer>create2(Integer.class, ReadXMLtools.getNodeValue(n));
		}
		else if( name.equals(Symbol.LNG) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<Long>create2(Long.class, ReadXMLtools.getNodeValue(n));
		}
		else if( name.equals(Symbol.STR) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<String>create2(String.class, ReadXMLtools.getNodeValue(n));
		}

		/*else if( name.equals(Symbol.CONST) ) {	
			Primitive<RE> theClass = null;			
			if(expectedBaseTyp == BigInteger.class){
				theClass = Primitive.<RE>getInstance(BigInt.class);
			}
			else if(expectedBaseTyp == Long.class){
				theClass = Primitive.<RE>getInstance(Lng.class);
			}			
			else if(expectedBaseTyp == Integer.class){
				theClass = Primitive.<RE>getInstance(Int.class);
			}			
			else if(expectedBaseTyp == String.class){
				theClass = Primitive.<RE>getInstance(SizedASCII.class);
			}			
			else if(expectedBaseTyp == Boolean.class){
				theClass = Primitive.<RE>getInstance(Bool.class);
			}
			if(theClass != null){
				Value<RE> constVal = theClass.createValue();
				String valueOfNode = getNodeValue(n);
				try {
					constVal.setConverted(valueOfNode);
				} catch (Inconvertible | Invalid e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return null;
				}
				AccessibleConstant<RE> re = AccessibleConstant.<RE>create(constVal);
				return re;				
			}
		}*/
		// COMPARISON
		else if( name.equals(Symbol.SMALLER) || name.equals(Symbol.GREATER) || name.equals(Symbol.EQUAL) ){	
			Accessible<?> a = createAccessible( getChildElementByIndex(n, 0), parentTyp, Comparable.class );
			if(a != null){
				Class<?> expectedBaseTypB = a.getBaseType();
				if(expectedBaseTypB == BigInteger.class){
					Accessible<BigInteger> b = ReadXML_func.createAccessible( getChildElementByIndex(n, 1), parentTyp, BigInteger.class );
					if(b != null){
						Accessible<Boolean> re = Comparison.create( (Accessible<BigInteger>)a, b, name );
						return (Accessible<RE>)re;
					}			
				}
				else if(expectedBaseTypB == Long.class){
					Accessible<Long> b = ReadXML_func.createAccessible( getChildElementByIndex(n, 1), parentTyp, Long.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Long>)a, b, name );
					}			
				}
				else if(expectedBaseTypB == Integer.class){
					Accessible<Integer> b = ReadXML_func.createAccessible( getChildElementByIndex(n, 1), parentTyp, Integer.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Integer>)a, b, name );
					}				
				}
				else if(expectedBaseTypB == Byte.class){
					Accessible<Byte> b = ReadXML_func.createAccessible( getChildElementByIndex(n, 1), parentTyp, Byte.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Byte>)a, b, name );
					}				
				}				
				else if(expectedBaseTypB == Number.class){
					Accessible<Number> b = ReadXML_func.createAccessible( getChildElementByIndex(n, 1), parentTyp, Number.class );
					if(b != null){
						return (Accessible<RE>)ComparisonNum.create( (Accessible<Number>)a, b, name );
					}				
				}				
				else if(expectedBaseTypB == String.class){
					Accessible<String> b = ReadXML_func.createAccessible( getChildElementByIndex(n, 1), parentTyp, String.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<String>)a, b, name );
					}				
				}
			}
		}
		// BOOL
		else if( name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) ) {	
			Accessible<Boolean> a = ReadXML_func.createAccessible( getChildElementByIndex(n, 0), parentTyp, Boolean.class );
			Accessible<Boolean> b = ReadXML_func.createAccessible( getChildElementByIndex(n, 1), parentTyp, Boolean.class );
			if(a != null && b != null){
				return (Accessible<RE>)Binary.<RE>create(a, b, name);
			}
		}
		else if( name.equals(Symbol.NOT) ) {			
			Accessible<Boolean> sub = ReadXML_func.createAccessible( getChildElementByIndex(n, 0), parentTyp, Boolean.class );
			if(sub != null){
				return (Accessible<RE>) Not.create(sub);
			}
		}
		else if( name.equals(Symbol.ISNULL) ) {			
			// TODO

		}
		// CONTROL FUNCTION
		else if( name.equals(Symbol.RETURN) ) {			
			Accessible<RE> e = ReadXML_func.createAccessible( getChildElementByIndex(n, 0), parentTyp, expectedBaseTyp );
			//Class<?> expectedBaseTypExp = e.getBaseType();
			return Return.create2(e);
			//return ReadXML.createAccessible( e, parentTyp, expectedBaseTypExp );
		}		
		/*
		else if( name.equals(Symbol.FUNCTION) ) {		
			Map<Complex, List<FunctionBuilder>> complexTyps = new HashMap<Complex, List<FunctionBuilder>>();
		
			List<Accessible<?>> steps = new LinkedList<Accessible<?>>();
			
	        
	        List<FunctionBuilder> functionBuilders = new LinkedList<FunctionBuilder>();
	        List<Value<?>> values = new LinkedList<Value<?>>();		
			Complex complexType = createState2(n, functionBuilders, values, complexTyps);
			if(complexType != null){
				
				Value<?>[] theValues = new Value<?>[ values.size() ];
				values.toArray( theValues );			
				
				Structure data = complexType.construct(theValues);	
	            
				for (FunctionBuilder fb : functionBuilders){
	            	steps.add( fb.build(complexType) );
	            }
		        Accessible<?>[] theSteps = new Accessible<?>[ steps.size() ];
		     
		        return (Accessible<RE>) Function.create(//data, 
		        		steps.toArray( theSteps ) );
			}	        
	        
	        return null;
		
		}
		*/
		
		//  REFERENCE
		else{
			System.out.println("get_id from " + name + " (" + ReadXMLtools.getNodeValue(n) + ")");
			Identifier<?> id = parentTyp.getSubIdentifier( ReadXMLtools.getNodeValue(n) );
			if( id != null){
				AbstractType<?> t = id.getType();
				Class<? extends Value<?>> clazzChild = t.getClazz();
				Primitive<?> pri = Primitive.getInstance( clazzChild );
				Class<?> baseType = pri.getBaseType();
				if( expectedBaseTyp.isAssignableFrom(baseType) ){ // TODO dann sollte noch Long Int implementieren ...
					String path = ReadXMLtools.getNodeValue(n);
					//AccessibleValueNew<RE> re = AccessibleValueNew.create(path, expectedBaseTyp);			
					AccessibleValue<RE> re = (AccessibleValue<RE>) AccessibleValue.create(path, baseType);
					return re;				
				}
				else{
					System.out.println("ERR: can not reference '" + ReadXMLtools.getNodeValue(n) + "'");
				}
			}
			else{
				System.out.println("ERR: can not find '" + ReadXMLtools.getNodeValue(n) + "'");
			}
		}
		return null;
	}
	
	public static Node getChildElementByIndex(Node n, int index){
		NodeList children = n.getChildNodes();
		int idx = 0;
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			if(c.getNodeType() == Node.ELEMENT_NODE){
				if(idx == index){
					return c;
				}
				idx++;
			}				
		}
		return null;
	}
	
}
