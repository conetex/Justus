package org.conetex.prime2.study;

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

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.IdentifierComplex;
import org.conetex.prime2.contractProcessing2.data.IdentifierPrimitive;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullIdentifierException;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.type.Complex.ComplexWasInitializedExeption;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.interpreter.SyntaxNode;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleConstant;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValueNew;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.assignment.Copy;
import org.conetex.prime2.contractProcessing2.lang.assignment.Reference;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.Comparison;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.ComparisonNum;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Binary;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Not;
import org.conetex.prime2.contractProcessing2.lang.control.function.Call;
import org.conetex.prime2.contractProcessing2.lang.control.function.Function;
import org.conetex.prime2.contractProcessing2.lang.control.function.Return;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic;
import org.conetex.prime2.contractProcessing2.runtime.Program;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Builder {

	
	
		
	
	public static List<Complex> create(SyntaxNode r2) throws ParserConfigurationException, SAXException, IOException, Invalid {

		List<Complex> complexTyps = TypBuilder.createComplexTypes(r2);
System.out.println("Builder " + r2.getNodeName());
		if(complexTyps != null){
			Complex complexTypeRoot = Complex.getInstance( r2.getNameAttribute() );
			Structure v = complexTypeRoot.createValue(null);
			List<Value<?>> values = ValBuilder.createValues(r2, complexTypeRoot, v);
			/* old
			Value<?>[] theValues = new Value<?>[ values.size() ];
			values.toArray( theValues );
			v.set(theValues);
			*/

						List<Accessible<?>> functions = FunBuilder.createFunctions(r2, complexTypeRoot);
						for(Accessible<?> f : functions){
							Object re = f.getFrom(v);
							if(re != null){							
System.out.println("Builder function ==> " + f + " -> " + re.toString());
}
else{
System.out.println("Builder function ==> " + f + " -> " + re);
}							
						}
System.out.println("Builder " + r2.getNodeName());						
		}
		
		return complexTyps;
		
	}	
	
	
	public static class TypBuilder {
	
	private static class Recursive<I>{ public I function; }
	
	private static interface Run { public void run(SyntaxNode node, Complex parent); }
	
	public static List<Complex> createComplexTypes(SyntaxNode n){
		Complex.clearInstances();
		Map<String, Complex> unformedComplexTypes = new HashMap<String, Complex>();
		Set<String> referringComplexTypeNames = new TreeSet<String>();
		List<Complex> re = new LinkedList<Complex>();
		
		Recursive<Run> recursive = new Recursive<Run>();
		recursive.function = (SyntaxNode node, Complex parent) -> 
		{
			for(SyntaxNode c : node.getChildNodes()){
				if( c.isType() ) {
					Complex complexType = createComplexType(c, parent, unformedComplexTypes, referringComplexTypeNames);
					if(complexType != null){
						re.add(complexType);			
						recursive.function.run(c, complexType);
					}
				}
			}
		};
		Complex complexTypeRoot = createComplexType(n, null, unformedComplexTypes, referringComplexTypeNames);
		if(complexTypeRoot != null){
			re.add(complexTypeRoot);			
			recursive.function.run(n, complexTypeRoot);// root == null
		}		
		//recursive.function.run(n, null);
		
for(String createdComplex : Complex.getInstanceNames()){
System.out.println("createComplexList known: " + createdComplex);			
}
		
		if(unformedComplexTypes.size() > 0){
			Complex.clearInstances();
			for(String unformedComplex : unformedComplexTypes.keySet()){
				System.err.println("createComplexList unknown Complex: " + unformedComplex);			
			}
			// TODO throw Exception: wir konnten nicht alles kompilieren!!!
			return null;
		}
		


		boolean error = false;
		for(String typeName : referringComplexTypeNames){
System.out.println("createComplexList referenced complex Type " + typeName);
			if( Complex.getInstance(typeName) == null ){
				error = true;
				System.err.println("createComplexList unkown Complex: " + typeName);
				// TODO throw Exception: wir kennen einen Typen nicht ...
			}
		}
		if(error){
			Complex.clearInstances();
			return null;
		}
		
		return re;
	}
	
	private static Complex createComplexType(SyntaxNode n, Complex parent, Map<String, Complex> unformedComplexTypes, Set<String> referringComplexTypeNames){
		String typeName = n.getNameAttribute();
		if(typeName == null){
			// TODO Exception
			System.err.println("no typeName for complex");			
			return null;
		}
		if(parent != null){
			typeName = parent.getName() + "." + typeName;		
		}
			
System.out.println("createComplexType " + typeName);
if( typeName.endsWith("contract4u") ){
System.out.println("createComplexType " + typeName);			
}
		List<Identifier<?>> identifiers = new LinkedList<Identifier<?>>();
		for(SyntaxNode c : n.getChildNodes()){
			String idTypeName = null;
			String idName = null;				
			Identifier<?> id = null;
			if( c.isIdentifier() ){ 
				idTypeName = c.getNodeType();
				idName = c.getNameAttribute();
				if(idTypeName == null){
					System.err.println("can not get Type of " + c.getNodeName() + " " + idName );
				}
				else{
					if(idTypeName.startsWith(Symbol.SIMPLE_TYPE_NS)){
						// Simple
						id = Primitive.createIdentifier( idName, idTypeName.substring(Symbol.SIMPLE_TYPE_NS.length()) );	
					}
					else{
						// Complex
						//referringComplexTypeNames.add(idTypeName);
						id = Complex.createIdentifier( idName, idTypeName, unformedComplexTypes );						
					}
				}
			}
			else if(c.getNodeName() == Symbol.FUNCTION){ 
				// Complex
				idTypeName = c.getNodeType();
				idName = c.getNameAttribute();
				//referringComplexTypeNames.add(typeName + "." + idTypeName);// TODO BUG !!!
				id = Complex.createIdentifier( idName, idTypeName, unformedComplexTypes );				
			}
			else{
				continue;
			}
			if(id != null){
				identifiers.add(id);
			}
			else{
				// TODO Exception
				System.err.println("createComplexType can not create Identifier " + idName + " (" + idTypeName + ")");
			}
		}		
		Identifier<?>[] theOrderedIdentifiers = new Identifier<?>[ identifiers.size() ];
		identifiers.toArray( theOrderedIdentifiers );
		
		// TODO doppelte definitionen abfangen ...
		Complex complexType = unformedComplexTypes.get(typeName);
		if(complexType == null){
			try {
				complexType = Complex.createInit(typeName, theOrderedIdentifiers); // TODO theOrderedIdentifiers müssen elemente enthalten, sonst gibts keinen typ
			} catch (DuplicateIdentifierNameExeption | Identifier.NullIdentifierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (DublicateComplexException e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				e.printStackTrace();
				return null;
			}							
			return complexType;			
		}
		else{
			try {
				complexType.init(typeName, theOrderedIdentifiers);
			} catch (DuplicateIdentifierNameExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (NullIdentifierException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (ComplexWasInitializedExeption e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			} catch (DublicateComplexException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			unformedComplexTypes.remove(typeName);
			return complexType;		
		}
	}	
	
	}
	
	

	public static class FunBuilder {
		
	public static abstract class FunctionBuilder{
		protected Node node;
		protected FunctionBuilder(Node n){
			this.node = n;
		}
		public abstract Accessible<?> build(Complex d);
	}
	
	public static List<Accessible<?>> createFunctions(SyntaxNode n, Complex type){
		String name = n.getNodeName();
		if(type == null){
			System.err.println("can not recognize type of " + name);
			return null;
		}
		
		List<Accessible<?>> acc = new LinkedList<Accessible<?>>();		
		
		List<SyntaxNode> children = n.getChildNodes();
		for(SyntaxNode c : children){
		//for(int i = 0; i < children.getLength(); i++){
			//Node c = children.item(i);
			if( c.isFunction() ){ 
				
System.out.println("createFunctions " + c.getNodeName() + " - " + c.getNameAttribute() );
				Accessible<?> v = createFunction( c, type);
				if(v != null){
					acc.add( v );
				}				
			}
			
			if( c.isType() ){
				String cname = c.getNameAttribute();// ReadXMLtools.getAttribute(c, Symbol.TYPE_NAME);
				/*
			    Identifier<?> cid = type.getSubIdentifier(cname); //
			    if(cid == null){
			    	System.err.println("createFunctions: can not identify " + cname);
			    	continue;
			    }
			    AbstractType<?> ctype = cid.getType();
			    if( ctype.getClass() == Complex.class ){
			    	createFunctions(c, (Complex) ctype);
			    }
			    */
			    Complex ctype = Complex.getInstance(type.getName() + "." + cname);
			    if(ctype == null){
			    	System.err.println("createFunctions: can not identify " + type.getName() + "." + cname);
			    	continue;
			    }
			    else{
			    	createFunctions(c, ctype);
			    }			    
			}
		}		
		
		return acc;
	}
	
	
	public static Accessible<?> createFunction(SyntaxNode n, Complex parentTyp//, List<FunctionBuilder> functionBuilders//, List<Value<?>> values, Map<Complex, List<FunctionBuilder>> complexTyps
			){
		
				
				String name = n.getNodeName();


				if( name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES) || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS) ) {
									Accessible<? extends Number> x = //createNumExpression( super.node, theClass );
											createFunctionAccessible( n, parentTyp, Integer.class );// TODO mach das zu number ...
									return x;
				}
				else if( name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) || name.equals(Symbol.NOT) || 
						 name.equals(Symbol.SMALLER) || name.equals(Symbol.EQUAL) || name.equals(Symbol.GREATER) || name.equals(Symbol.ISNULL) ) {
									Accessible<Boolean> x = //createBoolExpression( super.node );
									//ReadXML.createAccessible( super.node, c, Boolean.class );
									createFunctionAccessible( n, parentTyp, Boolean.class );
							return x;					
				}
				else if( name.equals(Symbol.REFERENCE) || name.equals(Symbol.COPY) ) {
									Accessible<?> x = //createBoolExpression( super.node );
									//ReadXML.createAccessible( super.node, c, Boolean.class );
									createFunctionAccessible( n, parentTyp, Object.class );
							return x;					
				}				
				else if( name.equals(Symbol.FUNCTION) || name.equals(Symbol.RETURN) || name.equals(Symbol.CALL) ){
					Accessible<?> x = //createBoolExpression( super.node );
					createFunctionAccessible( n, parentTyp, Object.class );
					return x;					
				}
				
				
		
				
		
		return null;
		
	}

	
	public static <RE> Accessible<RE> createFunctionAccessible(SyntaxNode n, Complex parentTyp, Class<RE> expectedBaseTyp){
		
		String name = n.getNodeName();
		
		// ASSIGNMENT
		if( name.equals("copy") || name.equals("ref") ) {	
		   //createAssignment(name, n, parentTyp);
			SyntaxNode c0 = n.getChildElementByIndex(0);
			System.out.println(c0.getNodeValue());
			Identifier<RE> id0 = parentTyp.getSubIdentifier( c0.getNodeValue() );// TODO geht nich

			SyntaxNode c1 = n.getChildElementByIndex(1);
			System.out.println(c1.getNodeValue());
			Identifier<RE> id1 = parentTyp.<RE>getSubIdentifier( c1.getNodeValue() );
			
			String c0DataType = c0.getNodeType();
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
				String path0 = c0.getNodeValue();
				SetableValue<RE> trg = SetableValue.<RE>create(path0, c0Class);
				
				Primitive<RE> pri1 = Primitive.getInstance( c1Class );
				Class<RE> baseType1 = pri1.getBaseType();	
				Accessible<RE> src = createFunctionAccessible(c1, parentTyp, baseType1);
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
				Accessible<BigInteger> a = createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, BigInteger.class );
				Accessible<BigInteger> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, BigInteger.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<BigInteger,RE>create(a, b, name, expectedBaseTyp );
				}				
			}
			else if(expectedBaseTyp == Long.class){
				Accessible<Long> a = FunBuilder.<Long>createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, Long.class );
				Accessible<Long> b = FunBuilder.<Long>createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Long.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<Long,RE>create(a, b, name, expectedBaseTyp );
				}				
			}			
			else if(expectedBaseTyp == Integer.class){
				Accessible<Integer> a = FunBuilder.<Integer>createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, Integer.class );
				Accessible<Integer> b = FunBuilder.<Integer>createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Integer.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<Integer,RE>create(a, b, name, expectedBaseTyp );
				}				
			}
			else if(expectedBaseTyp == Comparable.class || expectedBaseTyp == Object.class){
				Accessible<Number> a = createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, Number.class );
				Accessible<Number> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Number.class );
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
			return (AccessibleConstant<RE>) AccessibleConstant.<Boolean>create2(Boolean.class, n.getNodeValue());
		}
		else if( name.equals(Symbol.BINT) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<BigInteger>create2(BigInteger.class, n.getNodeValue());
		}
		else if( name.equals(Symbol.INT) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<Integer>create2(Integer.class, n.getNodeValue());
		}
		else if( name.equals(Symbol.LNG) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<Long>create2(Long.class, n.getNodeValue());
		}
		else if( name.equals(Symbol.STR) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<String>create2(String.class, n.getNodeValue());
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
			Accessible<?> a = createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, Comparable.class );
			if(a != null){
				Class<?> expectedBaseTypB = a.getBaseType();
				if(expectedBaseTypB == BigInteger.class){
					Accessible<BigInteger> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, BigInteger.class );
					if(b != null){
						Accessible<Boolean> re = Comparison.create( (Accessible<BigInteger>)a, b, name );
						return (Accessible<RE>)re;
					}			
				}
				else if(expectedBaseTypB == Long.class){
					Accessible<Long> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Long.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Long>)a, b, name );
					}			
				}
				else if(expectedBaseTypB == Integer.class){
					Accessible<Integer> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Integer.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Integer>)a, b, name );
					}				
				}
				else if(expectedBaseTypB == Byte.class){
					Accessible<Byte> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Byte.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Byte>)a, b, name );
					}				
				}				
				else if(expectedBaseTypB == Number.class){
					Accessible<Number> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Number.class );
					if(b != null){
						return (Accessible<RE>)ComparisonNum.create( (Accessible<Number>)a, b, name );
					}				
				}				
				else if(expectedBaseTypB == String.class){
					Accessible<String> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, String.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<String>)a, b, name );
					}				
				}
			}
		}
		// BOOL
		else if( name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) ) {	
			Accessible<Boolean> a = createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, Boolean.class );
			Accessible<Boolean> b = createFunctionAccessible( n.getChildElementByIndex(1), parentTyp, Boolean.class );
			if(a != null && b != null){
				return (Accessible<RE>)Binary.<RE>create(a, b, name);
			}
		}
		else if( name.equals(Symbol.NOT) ) {			
			Accessible<Boolean> sub = createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, Boolean.class );
			if(sub != null){
				return (Accessible<RE>) Not.create(sub);
			}
		}
		else if( name.equals(Symbol.ISNULL) ) {			
			// TODO

		}
		// CONTROL FUNCTION
		else if( name.equals(Symbol.RETURN) ) {			
			Accessible<RE> e = createFunctionAccessible( n.getChildElementByIndex(0), parentTyp, expectedBaseTyp );
			//Class<?> expectedBaseTypExp = e.getBaseType();
			return Return.create2(e);
			//return ReadXML.createAccessible( e, parentTyp, expectedBaseTypExp );
		}
		else if( name.equals(Symbol.CALL) ) {	
			String functionObj = n.getNodeType();
			AccessibleValueNew<Structure> re = AccessibleValueNew.create(functionObj, Structure.class);
			
			String functionName = n.getNameAttribute();
			Accessible<RE> e = (Accessible<RE>) Function.getInstance(functionName);
			//Class<?> expectedBaseTypExp = e.getBaseType();
			return Call.create2(e, re);
			//return ReadXML.createAccessible( e, parentTyp, expectedBaseTypExp );
		}		
		else if( name.equals(Symbol.FUNCTION) ) {
			String functionName = n.getNameAttribute();
			String idTypeName = n.getNodeType();
			Complex complexType = Complex.getInstance(idTypeName);
			if(complexType != null){
				List<Accessible<?>> steps = createFunctions(n, complexType);
				Accessible<?>[] theSteps = new Accessible<?>[ steps.size() ];
			    return (Accessible<RE>) Function.create(//data, 
			        		steps.toArray( theSteps ), functionName );				 
			}
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
			System.out.println("get_id from " + name + " (" + n.getNodeValue() + ")");
			String typName = parentTyp.getName();
			String idName =  n.getNodeValue();
			Complex pTyp = parentTyp;
			Identifier<?> id = pTyp.getSubIdentifier( idName );
			while( id == null ){
				String[] names = Complex.splitRight(typName);
			    if(names[0] == null){
			    	break;
			    }
			    typName = names[0];
		    	pTyp = Complex.getInstance( typName );
		    	if(pTyp == null){
		    		break;
		    	}
		    	id = pTyp.getSubIdentifier( idName );
			}
			
			if( id != null){
				AbstractType<?> t = id.getType();
				Class<? extends Value<?>> clazzChild = t.getClazz();
				Primitive<?> pri = Primitive.getInstance( clazzChild );
				Class<?> baseType = pri.getBaseType();
				if( expectedBaseTyp.isAssignableFrom(baseType) ){ // TODO dann sollte noch Long Int implementieren ...
					String path = n.getNodeValue();
					//AccessibleValueNew<RE> re = AccessibleValueNew.create(path, expectedBaseTyp);			
					AccessibleValue<RE> re = (AccessibleValue<RE>) AccessibleValue.create(path, baseType);
					return re;				
				}
				else{
					System.out.println("ERR: can not reference '" + n.getNodeValue() + "'");
				}
			}
			else{
				System.out.println("ERR: can not find '" + n.getNodeValue() + "'");
			}
		}
		return null;
	}
	
	}

	
	
	public static class ValBuilder {
		public static List<Value<?>> createValues(SyntaxNode n, Complex type, Structure data){
			String name = n.getNodeName();
			if(type == null){
				System.err.println("can not recognize type of " + name);
				return null;
			}
			
			/* old
			List<Value<?>> values = new LinkedList<Value<?>>();
			*/		
			
			for(SyntaxNode c : n.getChildNodes()){
				if( c.isValue() ){
	System.out.println("createValues " + c.getNodeName());
					Value<?> v = createValue( c, type, data );
					if(v != null){
						/* old
						values.add( v );
						*/			
					}
				}				
			}		
			
			/* old
			Value<?>[] theValues = new Value<?>[ values.size() ];
			values.toArray( theValues );	
			try {
				data.set(theValues);
			} catch (Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
			
			
			return values;
			*/
			
			//new
			return null;
		}
		
		
		public static Value<?> createValue(SyntaxNode n, Complex parentTyp, Structure parentData){
			
			// + " (local: " + n.getLocalName() + ")";
			
			String name = n.getNodeName();
			
			if( n.isIdentifier() ){
				name =  n.getNameAttribute();
			}
			else if ( name.equals(Symbol.FUNCTION) ){
				name =  n.getNameAttribute();
			}
			
		    Identifier<?> id = parentTyp.getSubIdentifier(name); //
		    if(id == null){
		    	System.err.println("createValue: can not identify " + name);
		    	return null;
		    }
		    
		    AbstractType<?> type = id.getType();
			if( type.getClass() == Complex.class ){
				Structure re = ( (IdentifierComplex)id ).createValue(parentData);
				
				// new
				createValues(n, (Complex)type, re);
				try {
					parentData.set(name, re);
				} catch (Invalid e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				/* old
				List<Value<?>> subvalues = createValues(n, (Complex) type, re);
				Value<?>[] theValues = new Value<?>[ subvalues.size() ];
				subvalues.toArray( theValues );	
				try {
					re.set(theValues);
				} catch (Invalid e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				*/
				
				return re;
			}
			else{
				String valueNode = n.getNodeValue();
				if(valueNode != null){
					Value<?> re = ( (IdentifierPrimitive<?>)id ) .createValue(valueNode, parentData);
					try {
						parentData.set(name, re);
					} catch (Invalid e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return re;
				}
			}
			
			
			
			return null;			
					
		}
		
				
	}
	
}
