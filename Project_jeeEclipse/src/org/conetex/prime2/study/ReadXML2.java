package org.conetex.prime2.study;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
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
import org.conetex.prime2.contractProcessing2.data.valueImplement.Bool;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Int;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Label;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Inconvertible;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleConstant;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;
import org.conetex.prime2.contractProcessing2.lang.Computable;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.assignment.AbstractAssigment;
import org.conetex.prime2.contractProcessing2.lang.assignment.Copy;
import org.conetex.prime2.contractProcessing2.lang.assignment.Reference;
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

public class ReadXML2 {

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
	             
	        // ASSIGNMENT
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
	             
  // MATH     
	             + "  <ai typ='Int'>3</ai>"
	             + "  <bi typ='Int'>4</bi>"	             
	             + "  <plus>" // 7
	             + "    <a>ai</a>"	             
	             + "    <b>bi</b>"	             
	             + "  </plus>"   
	                         
	             + "  <minus>" // -4
	             + "    <a>ai</a>"	             
	             + "    <plus>" // 7
	             + "      <a>ai</a>"	             
	             + "      <b>bi</b>"	             
	             + "    </plus>"              
	             + "  </minus>" 	             

	             + "    <remains>" 
	             + "      <times>" 
	             + "        <a>ai</a>"	             
	             + "        <b>bi</b>"	             
	             + "      </times>" // 12 	             
	             + "      <const>9</const>"	             
	             + "    </remains>" 	             
  
  // Comparsion / IsNull 
	             
/*// BOOL STUFF	             
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
	             + "    <or>" // true
	             + "      <a>bTrue</a>"	             
	             + "      <b>bFalse</b>"	             
	             + "    </or>"	             
	             + "  </and></not>"	             
*/         
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
		
		for(Computable c : Program.steps){
			c.compute(root);
		}
		for(Accessible<Boolean> a : Program.boolExpress){
			Boolean re = a.getFrom(root);
			System.out.println(re);
		}		
		for(Accessible<Number> a : Program.mathExpress){
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
	public static <T> AbstractAssigment<T> createAssignment(String name, Node n, Complex c){
		if(n == null){
			return null;
		}		
		//String aName = n.getNodeName();
		//if( name.equals("copy") || name.equals("ref") ) {			
			Node c0 = getChildElementByIndex(n, 0);
			Class<? extends Value<T>> c0Class = null;
			
			String c0DataType = getAttribute(c0, Symbol.TYP);
			if( c0DataType != null ){
				// TODO nicht nötig das auszulesen! Sollte aber ne Warnung erzeugen, wenns nicht zum typ des referenzierten Feld passt ...
				//Class<Value<Object>> c0ClassX = Primitive.getClass(c0DataType);
				Class<?> c0ClassX = Primitive.getClass(c0DataType);
				
			}
			System.out.println(getNodeValue(c0));
			Identifier<T> id0 = c.getSubIdentifier( getNodeValue(c0) );// TODO geht nich
			if( id0 != null){
				AbstractType<T> t = id0.getType();
				c0Class = t.getClazz();				
			}
				
	
			Node c1 = getChildElementByIndex(n, 1);
			Class<? extends Value<T>> c1Class = null;
			
			String c1DataType = getAttribute(c1, Symbol.TYP);
			if( c1DataType != null ){
				// TODO nicht nötig das auszulesen! Sollte aber ne Warnung erzeugen, wenns nicht zum typ des referenzierten Feld passt ...
				//Class<Value<Object>> c1ClassX = Primitive.getClass(c1DataType);
				Class<?> c1ClassX = Primitive.getClass(c1DataType);
			}
			System.out.println(getNodeValue(c1));
			Identifier<T> id1 = c.<T>getSubIdentifier( getNodeValue(c1) );
			if( id1 != null){
				AbstractType<T> t = id1.getType();
				c1Class = t.getClazz();				
			}			
			
			
			if(c0Class != null && c1Class != null && c0Class == c1Class){
				return createAssignment(name, c0, c1, c1Class);
			}
			else{
				// TODO: classes do not match
				return null;
			}
		//}
		//return null;
	}
	
	private static <T> AbstractAssigment<T> createAssignment(String name, Node c0, Node c1, Class<? extends Value<T>> cClass){
		AccessibleValue<T> trg = createReference2Value( c0, cClass );
		AccessibleValue<T> src = createReference2Value( c1, cClass );
		if(src != null && trg != null){
			if(name.equals(Symbol.COPY)){
				return Copy.<T>create(src, trg);					
			}
			if(name.equals(Symbol.REFERENCE)){
				return Reference.<T>create(src, trg);					
			}				
		}
		return null;
	}
	
	public static <T> AccessibleValue<T> createReference2Value(Node n, Class<? extends Value<T>> theClass){
		// TODO: whats this object ? now its null ...
		String path = getNodeValue(n);

		return AccessibleValue.<T>create(path, theClass);
	}	
	
	public static Accessible<Boolean> createBoolExpression(Node n){
		if(n == null){
			return null;
		}
	
		String name = n.getNodeName();
		if( name.equals(Symbol.AND) ) {			
			Accessible<Boolean> a = createBoolExpression( getChildElementByIndex(n, 0) );
			Accessible<Boolean> b = createBoolExpression( getChildElementByIndex(n, 1) );
			if(a != null && b != null){
				return Binary.createAdd(a, b);
			}
		}
		else if( name.equals(Symbol.OR) ) {			
			Accessible<Boolean> a = createBoolExpression( getChildElementByIndex(n, 0) );
			Accessible<Boolean> b = createBoolExpression( getChildElementByIndex(n, 1) );
			if(a != null && b != null){
				return Binary.createOr(a, b);
			}
		}
		else if( name.equals(Symbol.XOR) ) {			
			Accessible<Boolean> a = createBoolExpression( getChildElementByIndex(n, 0) );
			Accessible<Boolean> b = createBoolExpression( getChildElementByIndex(n, 1) );
			if(a != null && b != null){
				return Binary.createXOr(a, b);
			}
		}		
		else if( name.equals(Symbol.NOT) ) {			
			Accessible<Boolean> sub = createBoolExpression( getChildElementByIndex(n, 0) );
			if(sub != null){
				return Not.create(sub);
			}
		}
		else{
			return createReference2Value( n, Bool.class );
		}
		
		/*
		else if( isAttribute(n, "typ", "Bool") ) {
			Identifier<Boolean> id = ReadXML2.<Boolean>createSimpleAttribute(name, "Bool");
			_Variable<Boolean> var = _Variable.<Boolean>create(id);
			if(var != null){
				Boolean v = Bool.getTrans( n.getNodeValue() );
				if( v != null ){
					try {
						var.set(v);
					} catch (Invalid e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		*/
		
		return null;
	}
	
	public static <V extends Number, Z extends Number> Accessible<Z> createNumExpression(Node n, Primitive<Z> theClass){
		if(n == null){
			return null;
		}
	
		String name = n.getNodeName();
		if( name.equals(Symbol.PLUS) ) {			
			Accessible<Z> a = createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.PLUS, theClass.getBaseType() );
			}
		}
		else if( name.equals(Symbol.MINUS) ) {			
			Accessible<Z> a = createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.MINUS, theClass.getBaseType() );
			}
		}
		else if( name.equals(Symbol.TIMES) ) {			
			Accessible<Z> a = createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.TIMES, theClass.getBaseType() );
			}
		}		
		else if( name.equals(Symbol.DIVIDED_BY) ) {			
			Accessible<Z> a = createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.DIVIDED_BY, theClass.getBaseType() );
			}
		}		
		else if( name.equals(Symbol.REMAINS) ) {			
			Accessible<Z> a = createNumExpression( getChildElementByIndex(n, 0), theClass );
			Accessible<Z> b = createNumExpression( getChildElementByIndex(n, 1), theClass );
			if(a != null && b != null){
				return ElementaryArithmetic.<Z,Z>create(a, b, ElementaryArithmetic.REMAINS, theClass.getBaseType() );
			}
		}
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
		else{
			AccessibleValue<Z> re = createReference2Value(n, theClass.getClazz() );
			return re;
		}
	

		
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
				if( name.equals(Symbol.COPY) ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									AbstractAssigment<?> x = createAssignment(Symbol.COPY, super.node, c);
									if(x != null){
										Program.steps.add(x);										
									}
								}
							}
						);
				}
				else if( name.equals(Symbol.REFERENCE) ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									AbstractAssigment<?> x = createAssignment(Symbol.REFERENCE, super.node, c);
									if(x != null){
										Program.steps.add(x);										
									}
								}
							}
						);					
				}
				else if( name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.NOT) || name.equals(Symbol.XOR) ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									Accessible<Boolean> x = createBoolExpression( super.node );
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
									Accessible<Number> x = createNumExpression( super.node, Primitive.getInstance(Int.class) );
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
