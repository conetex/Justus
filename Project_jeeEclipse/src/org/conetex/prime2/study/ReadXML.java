package org.conetex.prime2.study;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.valueImplement.BigInt;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Bool;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Int;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Lng;
import org.conetex.prime2.contractProcessing2.data.valueImplement.SizedASCII;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleConstant;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValueNew;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.assignment.AbstractAssigment;
import org.conetex.prime2.contractProcessing2.lang.assignment.Copy;
import org.conetex.prime2.contractProcessing2.lang.assignment.Reference;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.Comparison;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.ComparisonNum;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Binary;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Not;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic;
import org.conetex.prime2.contractProcessing2.runtime.Heap;
import org.conetex.prime2.contractProcessing2.runtime.Program;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/*

Variable<T>
{
  Identifier<T> identifier
  {
    {
      ASCII8 label           : "Contact"
      Type.PrimitiveDataType<T> type
      {
        clazz                : Structure              
      }
    }	    
  }
  Value.Interface<T> value
  {
    Value.Implementation.Struct
    {
      Structure value: Structure
		{
		  Type.ComplexDataType type
		  {
		    // TODO: name the type
		    Identifier<?>[] orderedIdentifiers 
		    {
		      {
		        ASCII8 label           : "name"
		        Type.PrimitiveDataType<T> type
		        {
		          clazz                : ASCII16              
		        }
		      }
		      {
		        ASCII8 label           : "mail"
		        Type.PrimitiveDataType<T> type
		        {
		          clazz                : MailAddress              
		        }                                  
		      }
		    }
		  }
		  Value.Interface<?>[] values 
		  {
		    Value.Implementation.ASCII16
		    {
		      String value: "john"
		    }
		    Value.Implementation.MailAddress
		    {
		      String value: "nobody@nocorp.com"
		    }
		  }
		}
    }	    
  }
}
 
*/

public class ReadXML {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException  {
		
		String xml = ""
	             //+ "<!ENTITY mfg \"mit freundlichen Gruessen\" >"
				
                 + "<?xml version=\"1.0\" standalone=\"yes\" ?>"
                 + "<!DOCTYPE credentials [             " 
		         + "  <!ELEMENT author (#PCDATA)>       "
		         + "  <!ENTITY js \"Jo Smith\">         "
		         + "  <!ENTITY js \"Jo Smith\">         "
		         + "  <!ENTITY js \"Jo Smith\">         "
		         + "]>                                  "
				
				 + "<cred>  &js;               "
	             + "  <author>ein &js;</author>		  "
	             + "  <user>testusr</user>        "
	             + "  <password>testpwd</password>"
	               
	        // ASSIGNMENT OK
	             + "  <aAddress typ='MailAddress64'>12@32543.com</aAddress>"
	             + "  <a2Addres typ='MailAddress64'>ab@cdefg.com</a2Addres>"
	             + "  <copy>"
	             + "    <source typ='MailAddress64'>aAddress</source>"
	             + "    <target typ='MailAddress64'>a2Addres</target>"
	             + "  </copy>"	           
	             
	             + "  <copy>"
	             + "    <source typ='MailAddress64'>sub.aAddress</source>"
	             + "    <target typ='MailAddress64'>sub.a2Addres</target>"
	             + "  </copy>"
	             
                 + "  <sub>"	             
	             + "    <aAddress typ='MailAddress64'>yy@yyy.com</aAddress>"
	             + "    <a2Addres typ='MailAddress64'>xx@xxx.com</a2Addres>"
	             //+ "    <copy>"
	             //+ "      <source typ='MailAddress64'>sub.aAddress</source>"
	             //+ "      <target typ='MailAddress64'>sub.a2Addres</target>"
	             //+ "    </copy>"
                 + "  </sub>"	             
	             
  // MATH OK
	
	             + "  <ai typ='Int'>3</ai>"
	             + "  <bi typ='Int'>4</bi>"	             
          
	             + "  <plus>" // 7
	             + "    <a>ai</a>"	             
	             + "    <b>bi</b>"	             
	             + "  </plus>"   
	                         
	             + "  <minus>" // -4
	             + "    <a>ai</a>"	             
	             + "    <plus>"
	             + "      <a>ai</a>"	             
	             + "      <b>bi</b>"	             
	             + "    </plus>"              
	             + "  </minus>" 	             

	             + "    <remains>" // 4
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" 	             
	             + "      <Integer>8</Integer>"	             
	             + "    </remains>" 	             
	             
  // Comparsion / IsNull OK
	              
	             + "    <smaller>" 
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" // 12 	             
	             + "      <Integer>9</Integer>"	        // false         
	             + "    </smaller>"  
	             
	             + "    <greater>" 
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" // 12 	             
	             + "      <Integer>9</Integer>"	        // true         
	             + "    </greater>" 
	             + "    <smaller>" 
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" // 12 	             
	             + "      <Integer>13</Integer>"	    // true         
	             + "    </smaller>" 
	               
	             + "    <greater>" 
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" // 12 	             
	             + "      <Integer>13</Integer>"	// false         
	             + "    </greater>"
	             
	             + "    <greater>" 
	             + "      <Integer>13</Integer>"       
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" // 12 	             
	             + "    </greater>"	                // true 
	             
	             + "    <greater>" 
	             + "      <Integer>11</Integer>"       
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" // 12 	             
	             + "    </greater>"	                // false  	             
	             
// BOOL STUFF OK         
	             + "  <bTrue typ='Bool'>true</bTrue>"	             
	             + "  <and>" // true
	             + "    <a>bTrue</a>"	             
	             + "    <b>bTrue</b>"	             
	             + "  </and>"
	             	             
	             + "  <bFalse typ='Bool'>false</bFalse>"	             
	             + "  <and>" // false
	             + "    <a>bTrue</a>"	             
	             + "    <b>bFalse</b>"	             
	             + "  </and>"	
	             
	             + "  <or>" // true
	             + "    <a>bTrue</a>"	             
	             + "    <b>bFalse</b>"	             
	             + "  </or>"	 
	             + "  <and>" // false
	             + "    <a>bFalse</a>"	             
	             + "    <b>bFalse</b>"	             
	             + "  </and>"
	             
	             + "  <not><and>" // true
	             + "    <a>bFalse</a>"	             
	             + "    <or>" 
	             + "      <a>bTrue</a>"	             
	             + "      <b>bFalse</b>"	             
	             + "    </or>"	             
	             + "  </and></not>"	
	            
	             + "</cred>                " 
				;
		InputStream is = new ByteArrayInputStream( xml.getBytes(StandardCharsets.UTF_8) );
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );
		//String usr = document.getElementsByTagName("user").item(0).getTextContent();
		//String pwd = document.getElementsByTagName("password").item(0).getTextContent();
		
		Map<Complex, List<FunctionBuilder>> complexTyps = 
				new HashMap<Complex, List<FunctionBuilder>>();
		
		Structure root = null;
		NodeList children = document.getChildNodes();			
		for(int i = 0; i < children.getLength(); i++){	
			Node c = children.item(i);
			short typOfNode = children.item(i).getNodeType();
			if(typOfNode == Node.ELEMENT_NODE){
				if(root == null){
					root = createState( c, complexTyps );
				}
				else{
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}
		
        for (Entry<Complex, List<FunctionBuilder>> pair : complexTyps.entrySet()){
        	// TODO hier ist die Reihenfolge entscheidend!
        	Complex d = pair.getKey();
        	List<FunctionBuilder> fbs = pair.getValue();
            for (FunctionBuilder fb : fbs){
            	fb.build(d);
            } 
        }
		
		Heap heap = Heap.create(root);
		
		System.out.println("ASSIG");	
		for(Accessible<?> c : Program.assignments){
			Object re = c.getFrom(root);
			System.out.println(re);
		}
		
		System.out.println("BOOL");		
		for(Accessible<Boolean> a : Program.boolExpress){
			Boolean re = a.getFrom(root);
			System.out.println(re);
		}		
		
		System.out.println("MATH");		
		for(Accessible<? extends Number> a : Program.mathExpress){
			Number re = a.getFrom(root);
			System.out.println(re);
		}		
		
		//StringTokenizer st = new StringTokenizer("xyz", Label.NAME_SEPERATOR);
		//while(st.hasMoreTokens()){
			//System.out.println(st.nextToken());
		//}
		
		//String[] result = "this.is.a.test".split("\\.");
		// for (int x=0; x<result.length; x++)
		//     System.out.println(result[x]);
		
	   // String x = "this";

	    
		
		//List<Attribute<?>> res = parseAttributes(children.item(0));
				
		System.out.println("ENDE");
		
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
	
	public static Node getChildElementByName(Node n, String name){
		NodeList children = n.getChildNodes();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			short type = c.getNodeType();
			if(type == Node.ELEMENT_NODE){
				String cn = c.getNodeName();
				if(name.equals(cn)){
					return c;
				}
			}				
		}
		return null;
	}
	
	public static String getNodeValue(Node n){
		String re = getAttribute(n, "value");
		if(re != null){
			return re;				
		}
		NodeList children = n.getChildNodes();
		if(children != null && children.getLength() == 1){
			Node textValueNode = children.item(0);
			short textValueNodeType = textValueNode.getNodeType();
			if(textValueNodeType == Node.TEXT_NODE){		
				return textValueNode.getNodeValue();
			}
		}
		return null;
	}	
	
	public static String getAttribute(Node n, String a){
		NamedNodeMap attributes = n.getAttributes();
		Node attributeNode = attributes.getNamedItem( a );			
		if(attributeNode != null){
			return attributeNode.getNodeValue();
		}
		return null;
	}
	
	public static boolean isAttribute(Node n, String attributeName, String attributeValue){
		String actualAttributeValue = getAttribute(n, attributeName);
		if(  attributeValue.equals( actualAttributeValue )  ){
			return true;				
		}
		return false;
	}	
	
	
	public static abstract class FunctionBuilder{
		protected Node node;
		protected FunctionBuilder(Node n){
			this.node = n;
		}
		public abstract void build(Complex d);
	}
	
	// <T, V extends Value.Interface<T>> 
	public static <T> AbstractAssigment<T> _createAssignment(String name, Node n, Complex parentTyp){
		if(n == null){
			return null;
		}		
		// TODO dies hier nach createAccessible einbaun, Assigment muss Accessible implementieren ...
		if( name.equals("copy") || name.equals("ref") ) {			
			Node c0 = getChildElementByIndex(n, 0);
			System.out.println(getNodeValue(c0));
			Identifier<T> id0 = parentTyp.getSubIdentifier( getNodeValue(c0) );// TODO geht nich

			Node c1 = getChildElementByIndex(n, 1);
			System.out.println(getNodeValue(c1));
			Identifier<T> id1 = parentTyp.<T>getSubIdentifier( getNodeValue(c1) );
			
			String c0DataType = getAttribute(c0, Symbol.TYP);
			if( c0DataType != null ){
				// TODO nicht nötig das auszulesen! Sollte aber ne Warnung erzeugen, wenns nicht zum typ des referenzierten Feld passt ...
				//Class<Value<Object>> c0ClassX = Primitive.getClass(c0DataType);
				Class<?> c0ClassX = Primitive.getClass(c0DataType);				
			}
		
			// TODO: der teil hier könnte ausgelagert werden nach AbstractAssigment
			
			Class<? extends Value<T>> c0Class = null;
			if( id0 != null){
				AbstractType<T> t = id0.getType();
				c0Class = t.getClazz();				
			}			
			Class<? extends Value<T>> c1Class = null;
			if( id1 != null){
				AbstractType<T> t = id1.getType();
				c1Class = t.getClazz();				
			}			
			
			// TODO hier reicht eigentlich gleicher base-typ !!!
			if(c0Class != null && c1Class != null && c0Class == c1Class){
				String path0 = getNodeValue(c0);
				SetableValue<T> trg = SetableValue.<T>create(path0, c0Class);
				
				Primitive<T> pri1 = Primitive.getInstance( c1Class );
				Class<T> baseType1 = pri1.getBaseType();	
				Accessible<T> src = createAccessible(c1, parentTyp, baseType1);
				if(src != null && trg != null){
					if(name.equals(Symbol.COPY)){
						return Copy.<T>create(trg, src);					
					}
					if(name.equals(Symbol.REFERENCE)){
						return Reference.<T>create(trg, src);					
					}				
				}
				return null;
			}
			else{
				// TODO: classes do not match
				return null;
			}
		}
		
		
		return null;
	}
	
	private static <T> AbstractAssigment<T> _createAssignment(String name, Node c0, Node c1, Complex c, Class<? extends Value<T>> c1Class){
		String path0 = getNodeValue(c0);
		SetableValue<T> trg = //createReference2Value( c0, c1Class );
				SetableValue.<T>create(path0, c1Class);
		//String path1 = getNodeValue(c1);
		//AccessibleValue<T> src = SetableValue.<T>create(path1, c1Class);
		Primitive<T> pri = Primitive.getInstance( c1Class );
		Class<T> baseType = pri.getBaseType();	
		Accessible<T> src = createAccessible(c1, c, baseType);
		if(src != null && trg != null){
			if(name.equals(Symbol.COPY)){
				return Copy.<T>create(trg, src);					
			}
			if(name.equals(Symbol.REFERENCE)){
				return Reference.<T>create(trg, src);					
			}				
		}
		return null;
	}
		
	

	
	
	public static <I extends Number, RE> Accessible<RE> _createAccessibleNum(Node n, Complex parentTyp, Class<I> inBaseTyp, Class<RE> expectedBaseTyp){
		// TODO drop this ...
		if(expectedBaseTyp == BigInteger.class){
			Accessible<I> a = createAccessible( getChildElementByIndex(n, 0), parentTyp, inBaseTyp );
			Accessible<I> b = createAccessible( getChildElementByIndex(n, 1), parentTyp, inBaseTyp );
			if(a != null && b != null){
				return ElementaryArithmetic.<I,RE>create(a, b, n.getNodeName(), expectedBaseTyp );
			}				
		}	
		return null;
	}

	public static <RE> Accessible<RE> createAccessible(Node n, Complex parentTyp, Class<RE> expectedBaseTyp){
		
		String name = n.getNodeName();
		
		// ASSIGNMENT
		if( name.equals("copy") || name.equals("ref") ) {	
		   //createAssignment(name, n, parentTyp);
			Node c0 = getChildElementByIndex(n, 0);
			System.out.println(getNodeValue(c0));
			Identifier<RE> id0 = parentTyp.getSubIdentifier( getNodeValue(c0) );// TODO geht nich

			Node c1 = getChildElementByIndex(n, 1);
			System.out.println(getNodeValue(c1));
			Identifier<RE> id1 = parentTyp.<RE>getSubIdentifier( getNodeValue(c1) );
			
			String c0DataType = getAttribute(c0, Symbol.TYP);
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
				String path0 = getNodeValue(c0);
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
				Accessible<Long> a = ReadXML.<Long>createAccessible( getChildElementByIndex(n, 0), parentTyp, Long.class );
				Accessible<Long> b = ReadXML.<Long>createAccessible( getChildElementByIndex(n, 1), parentTyp, Long.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<Long,RE>create(a, b, name, expectedBaseTyp );
				}				
			}			
			else if(expectedBaseTyp == Integer.class){
				Accessible<Integer> a = ReadXML.<Integer>createAccessible( getChildElementByIndex(n, 0), parentTyp, Integer.class );
				Accessible<Integer> b = ReadXML.<Integer>createAccessible( getChildElementByIndex(n, 1), parentTyp, Integer.class );
				if(a != null && b != null){
					return ElementaryArithmetic.<Integer,RE>create(a, b, name, expectedBaseTyp );
				}				
			}
			else if(expectedBaseTyp == Comparable.class){
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
			return (AccessibleConstant<RE>) AccessibleConstant.<Boolean>create2(Boolean.class, getNodeValue(n));
		}
		else if( name.equals(Symbol.BINT) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<BigInteger>create2(BigInteger.class, getNodeValue(n));
		}
		else if( name.equals(Symbol.INT) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<Integer>create2(Integer.class, getNodeValue(n));
		}
		else if( name.equals(Symbol.LNG) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<Long>create2(Long.class, getNodeValue(n));
		}
		else if( name.equals(Symbol.STR) ) {	
			return (AccessibleConstant<RE>) AccessibleConstant.<String>create2(String.class, getNodeValue(n));
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
			Accessible<?> a = ReadXML.createAccessible( getChildElementByIndex(n, 0), parentTyp, Comparable.class );
			if(a != null){
				Class<?> expectedBaseTypB = a.getBaseType();
				if(expectedBaseTypB == BigInteger.class){
					Accessible<BigInteger> b = ReadXML.createAccessible( getChildElementByIndex(n, 1), parentTyp, BigInteger.class );
					if(b != null){
						Accessible<Boolean> re = Comparison.create( (Accessible<BigInteger>)a, b, name );
						return (Accessible<RE>)re;
					}			
				}
				else if(expectedBaseTypB == Long.class){
					Accessible<Long> b = ReadXML.createAccessible( getChildElementByIndex(n, 1), parentTyp, Long.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Long>)a, b, name );
					}			
				}
				else if(expectedBaseTypB == Integer.class){
					Accessible<Integer> b = ReadXML.createAccessible( getChildElementByIndex(n, 1), parentTyp, Integer.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Integer>)a, b, name );
					}				
				}
				else if(expectedBaseTypB == Byte.class){
					Accessible<Byte> b = ReadXML.createAccessible( getChildElementByIndex(n, 1), parentTyp, Byte.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<Byte>)a, b, name );
					}				
				}				
				else if(expectedBaseTypB == Number.class){
					Accessible<Number> b = ReadXML.createAccessible( getChildElementByIndex(n, 1), parentTyp, Number.class );
					if(b != null){
						return (Accessible<RE>)ComparisonNum.create( (Accessible<Number>)a, b, name );
					}				
				}				
				else if(expectedBaseTypB == String.class){
					Accessible<String> b = ReadXML.createAccessible( getChildElementByIndex(n, 1), parentTyp, String.class );
					if(b != null){
						return (Accessible<RE>)Comparison.create( (Accessible<String>)a, b, name );
					}				
				}
			}
		}
		// BOOL
		else if( name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) ) {	
			Accessible<Boolean> a = ReadXML.createAccessible( getChildElementByIndex(n, 0), parentTyp, Boolean.class );
			Accessible<Boolean> b = ReadXML.createAccessible( getChildElementByIndex(n, 1), parentTyp, Boolean.class );
			if(a != null && b != null){
				return (Accessible<RE>)Binary.<RE>create(a, b, name);
			}
		}
		else if( name.equals(Symbol.NOT) ) {			
			Accessible<Boolean> sub = ReadXML.createAccessible( getChildElementByIndex(n, 0), parentTyp, Boolean.class );
			if(sub != null){
				return (Accessible<RE>) Not.create(sub);
			}
		}
		else if( name.equals(Symbol.ISNULL) ) {			
			// TODO

		}		
		//|| name.equals(Symbol.NOT)
		// is null
		//Accessible<OUT> a = null;
		//Accessible<OUT> b = null;								
		
		
		//  REFERENCE
		else{
			System.out.println("get_id from " + name + " (" + getNodeValue(n) + ")");
			Identifier<?> id = parentTyp.getSubIdentifier( getNodeValue(n) );
			if( id != null){
				AbstractType<?> t = id.getType();
				Class<? extends Value<?>> clazzChild = t.getClazz();
				Primitive<?> pri = Primitive.getInstance( clazzChild );
				Class<?> baseType = pri.getBaseType();
				if( expectedBaseTyp.isAssignableFrom(baseType) ){ // TODO dann sollte noch Long Int implementieren ...
					String path = getNodeValue(n);
					//AccessibleValueNew<RE> re = AccessibleValueNew.create(path, expectedBaseTyp);			
					AccessibleValueNew<RE> re = (AccessibleValueNew<RE>) AccessibleValueNew.create(path, baseType);
					return re;				
				}
				else{
					System.out.println("ERR: can not reference '" + getNodeValue(n) + "'");
				}
			}
			else{
				System.out.println("ERR: can not find '" + getNodeValue(n) + "'");
			}
		}
		return null;
	}

	
		
	public static <V extends Number, Z extends Number & Comparable<Z>> Accessible<Z> _createNumExpression(Node n, Primitive<Z> theClass){
		// TODO drop this ...
		if(n == null){
			return null;
		}
	
		String name = n.getNodeName();
		if( name.equals(Symbol.PLUS) ) {			
			Accessible<Z> a = _createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = _createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.PLUS, theClass.getBaseType() );
			}
		}
		else if( name.equals(Symbol.MINUS) ) {			
			Accessible<Z> a = _createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = _createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.MINUS, theClass.getBaseType() );
			}
		}
		else if( name.equals(Symbol.TIMES) ) {			
			Accessible<Z> a = _createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = _createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.TIMES, theClass.getBaseType() );
			}
		}		
		else if( name.equals(Symbol.DIVIDED_BY) ) {			
			Accessible<Z> a = _createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = _createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.DIVIDED_BY, theClass.getBaseType() );
			}
		}		
		else if( name.equals(Symbol.REMAINS) ) {			
			Accessible<Z> a = _createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = _createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.REMAINS, theClass.getBaseType() );
			}
		}
		/*
		else if( name.equals(Symbol.CONST) ) {			
			Value<Z> constVal = theClass.createValue();
			String valueOfNode = getNodeValue(n);
			try {
				constVal.setConverted(valueOfNode);
			} catch (Inconvertible | Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			AccessibleConstant<Z> re = AccessibleConstant.<Z>create(constVal);
			return re;
		}
		*/
	/*
		else{
			AccessibleValue<Z> re = createReference2Value(n, theClass.getClazz() );
			return re;
		}
	*/

		
		return null;
	}
	

	public static Structure createState(Node n, Map<Complex, List<FunctionBuilder>> complexTyps){
		
		List<FunctionBuilder> functionBuilders = new LinkedList<FunctionBuilder>();
		
		List<Value<?>> values = new LinkedList<Value<?>>();		
		
		Complex complexType = createState2(n, functionBuilders, values, complexTyps);
		if(complexType != null){
			// TODO keine doppelten complexTyps erlauben ... gib ihnen auch namen...
			//return complexType;
			complexTyps.put(complexType, functionBuilders);
			
			Value<?>[] theValues = new Value<?>[ values.size() ];
			values.toArray( theValues );			
			
			return complexType.construct(theValues);			
		}
		return null;
	}
	
	public static Complex createState2(Node n, List<FunctionBuilder> functionBuilders, List<Value<?>> values, Map<Complex, List<FunctionBuilder>> complexTyps){
		
		//List<FunctionBuilder> functionBuilders = new LinkedList<FunctionBuilder>();
		
		NodeList children = n.getChildNodes();
		List<Identifier<?>> identifiers = new LinkedList<Identifier<?>>();
		//List<Value.Interface<?>> values = new LinkedList<Value.Interface<?>>();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			short type = c.getNodeType();
			System.out.println(c.getNodeName() + " - " + type);
			if(type == Node.ELEMENT_NODE){
				
				String name = c.getNodeName();

				if( name.equals(Symbol.REFERENCE) || name.equals(Symbol.COPY) ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									//AbstractAssigment<?> x = createAssignment(Symbol.REFERENCE, super.node, c);
									Accessible<?> x = ReadXML.createAccessible( super.node, c, Object.class );									
									if(x != null){
										Program.assignments.add(x);										
									}
								}
							}
						);					
				}
				else if( name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) || name.equals(Symbol.NOT) || 
						 name.equals(Symbol.SMALLER) || name.equals(Symbol.EQUAL) || name.equals(Symbol.GREATER) || name.equals(Symbol.ISNULL) ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									Accessible<Boolean> x = //createBoolExpression( super.node );
									ReadXML.createAccessible( super.node, c, Boolean.class );
									if(x != null){
										Program.boolExpress.add(x);										
									}
								}
							}
						);					
				}
				else if( name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES) || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS) ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									//Primitive<Integer> theClass = Primitive.<Integer>getInstance(Int.class);
									Accessible<? extends Number> x = //createNumExpression( super.node, theClass );
											ReadXML.createAccessible( super.node, c, Integer.class );// TODO mach das zu number ...
									if(x != null){
										Program.mathExpress.add(x);										
									}
								}
							}
						);					
				}
				
				
				else{
					createAttributesValues( c, identifiers, values, complexTyps );
				}
				
			}				
		}		
		
		Identifier<?>[] theOrderedIdentifiers = new Identifier<?>[ identifiers.size() ];
		identifiers.toArray( theOrderedIdentifiers );
		
		//Value.Interface<?>[] theValues = new Value.Interface<?>[ values.size() ];
		//values.toArray( theValues );		
		
		Complex complexType = null;
		try {
			complexType = Complex.createComplexDataType(theOrderedIdentifiers);
		} catch (DuplicateIdentifierNameExeption | Identifier.NullIdentifierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
		
		if(complexType != null){
			// TODO keine doppelten complexTyps erlauben ... gib ihnen auch namen...
			complexTyps.put(complexType, functionBuilders);
			return complexType;
			//return complexType.construct(theValues);			
		}
		
		return null;
		
	}
	

	
	public static boolean createAttributesValues(Node n, List<Identifier<?>> dattributes, List<Value<?>> values, Map<Complex, List<FunctionBuilder>> complexTyps){
		
		String name = n.getNodeName();// + " (local: " + n.getLocalName() + ")";
		
		NamedNodeMap attributes = n.getAttributes();
		Node dataTypNode = attributes.getNamedItem( Symbol.TYP );
		
		//String valueNode = null;		
		if(dataTypNode != null){			
			String valueNode = getNodeValue(n);
			if(valueNode != null){
				// ... Primitiv	
				String type = dataTypNode.getNodeValue();
				//String value = valueNode;
				Identifier<?> attribute = createSimpleAttribute(name, type); //
				if(attribute != null){
					Value<?> v = createSimpleValue(attribute, valueNode);
					if(v != null){
						dattributes.add(attribute);
						values.add(v);		
						return true;
					}
				}
				return false;			
			}			
		}
		
		// ... Complex
		//Structure s = createState(n, complexTyps);
		List<FunctionBuilder> functionBuilders = new LinkedList<FunctionBuilder>();
		List<Value<?>> subvalues = new LinkedList<Value<?>>();		
		Complex ct = createState2(n, functionBuilders, subvalues, complexTyps);
		
		if(ct != null){
			// TODO keine doppelten complexTyps erlauben ... gib ihnen auch namen...
			//return complexType;
			complexTyps.put(ct, functionBuilders);
			
			Value<?>[] theValues = new Value<?>[ subvalues.size() ];
			subvalues.toArray( theValues );			
			
			//Structure s = ct.construct(theValues);		
			
			//if(s != null){
			    Identifier<Value<?>[]> attribute = createComplexAttribute(name, ct); //
				if(attribute != null){
					Value<Value<?>[]> v = createComplexValue(attribute, ct, theValues);
					if(v != null){
						dattributes.add(attribute);
						values.add(v);						
						return true;
					}
				}
			//}			
			
		}

		return false;			
				
	}
	

	
	public static Value<Value<?>[]> createComplexValue(Identifier<Value<?>[]> attribute
			  , Complex ct
			  , Value<?>[] theValues
			){
		//new PrimitiveDataType< Complex  , State >  ( Complex.class  , new ValueFactory<State>()   { public Complex   createValueImp() { return new Complex()  ; } } )
		if(attribute != null){
			Value<Value<?>[]> v = attribute.createValue();
			//Structure value = ct.construct(theValues);
			try {
				v.set(theValues);
			} catch (Invalid e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return v;
		}		
		
		return null;
	}	
	
	public static Identifier<Value<?>[]> createComplexAttribute(String name, Complex t){
		//PrimitiveDataType<Structure> simpleType = PrimitiveDataType.getInstance( Value.Implementation.Struct.class.getSimpleName() );
		
		Label str = new Label(); 
		try {
			str.set(name);
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Identifier<Value<?>[]> attribute = null;
		try {
			attribute = t.createIdentifier( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}	
		
		return attribute;
		
	}	
	
	public static <T> Value<T> createSimpleValue(Identifier<T> attribute, String value){
		
		if(attribute != null){
			Value<T> v = attribute.createValue();
			try {
				v.setConverted(value);
			} catch (Inconvertible | Invalid e) {
				// TODO Auto-generated catch block
				System.err.println(e.getMessage());
				//e.printStackTrace();
				return null;
			}
			return v;
		}		
		
		return null;
	}	

	public static <T> Identifier<T> createSimpleAttribute(String name, String type){
		Primitive<T> simpleType = Primitive.<T>getInstance( type );				
		Label str = new Label(); 
		try {
			str.set(name);
		} catch (Invalid e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Identifier<T> attribute = null;
		try {
			attribute = simpleType.createIdentifier( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}	
		
		return attribute;
		
	}		
	
}
