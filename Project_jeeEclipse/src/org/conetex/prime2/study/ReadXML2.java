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
import org.conetex.prime2.contractProcessing2.data.values.Label;
import org.conetex.prime2.contractProcessing2.data.values.Bool;
import org.conetex.prime2.contractProcessing2.data.values.Structure;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueException;
import org.conetex.prime2.contractProcessing2.data.values.exception.ValueTransformException;
import org.conetex.prime2.contractProcessing2.lang.Reference2Value;
import org.conetex.prime2.contractProcessing2.lang.assignment.AbstractAssigment;
import org.conetex.prime2.contractProcessing2.lang.assignment.Copy;
import org.conetex.prime2.contractProcessing2.lang.assignment.Ref;
import org.conetex.prime2.contractProcessing2.lang.booleanExpression.AbstractBooleanExpression;
import org.conetex.prime2.contractProcessing2.lang.booleanExpression.And;
import org.conetex.prime2.contractProcessing2.lang.booleanExpression.Not;
import org.conetex.prime2.contractProcessing2.lang.booleanExpression.Or;
import org.conetex.prime2.contractProcessing2.lang.booleanExpression._Variable;
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
	             
	             + "  <v typ='Bool'>true</v>"	             
	             + "  <And>"
	             + "    <a>v</a>"	             
	             + "    <b>v</b>"	             
	             + "  </And>"
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
		
		for(AbstractAssigment<?> a : Program.steps){
			a.compute(root);
		}
		
		
		//StringTokenizer st = new StringTokenizer("xyz", Label.NAME_SEPERATOR);
		//while(st.hasMoreTokens()){
			//System.out.println(st.nextToken());
		//}
		
		//String[] result = "this.is.a.test".split("\\.");
		// for (int x=0; x<result.length; x++)
		//     System.out.println(result[x]);
		
	    String x = "this";

	    
		
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
	public static <T> AbstractAssigment<?> createAssignment(String name, Node n, Complex c){
		if(n == null){
			return null;
		}		
		//String aName = n.getNodeName();
		//if( name.equals("copy") || name.equals("ref") ) {			
			Node c0 = getChildElementByIndex(n, 0);
			Class<? extends Value<T>> c0Class = null;
			
			String c0DataType = getAttribute(c0, "typ");
			if( c0DataType != null ){
				// TODO nicht nötig das auszulesen! Sollte aber ne Warnung erzeugen, wenns nicht zum typ des referenzierten Feld passt ...
				Class<Value<Object>> c0ClassX = Primitive.getClass(c0DataType);
			}
			System.out.println(getNodeValue(c0));
			Identifier<T> id0 = c.getSubIdentifier( getNodeValue(c0) );// TODO geht nich
			if( id0 != null){
				AbstractType<T> t = id0.getType();
				c0Class = t.getClazz();				
			}
				
	
			Node c1 = getChildElementByIndex(n, 1);
			Class<? extends Value<T>> c1Class = null;
			
			String c1DataType = getAttribute(c1, "typ");
			if( c1DataType != null ){
				// TODO nicht nötig das auszulesen! Sollte aber ne Warnung erzeugen, wenns nicht zum typ des referenzierten Feld passt ...
				Class<Value<Object>> c1ClassX = Primitive.getClass(c1DataType);
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
		Reference2Value<T> trg = createReference2Value( c0, cClass );
		Reference2Value<T> src = createReference2Value( c1, cClass );
		if(src != null && trg != null){
			if(name.equals("copy")){
				return Copy.<T>create(src, trg);					
			}
			if(name.equals("ref")){
				return Ref.<T>create(src, trg);					
			}				
		}
		return null;
	}
	
	public static <T> Reference2Value<T> createReference2Value(Node n, Class<? extends Value<T>> theClass){
		// TODO: whats this object ? now its null ...
		String path = getNodeValue(n);

		return Reference2Value.<T>create(path, theClass);
	}	
	
	public static AbstractBooleanExpression createExpression(Node n){
		if(n == null){
			return null;
		}
	
		String name = n.getNodeName();
		if( name.equals("and") ) {			
			AbstractBooleanExpression a = createExpression( getChildElementByIndex(n, 0) );
			AbstractBooleanExpression b = createExpression( getChildElementByIndex(n, 1) );
			if(a != null && b != null){
				return And.create(a, b);
			}
		}
		else if( name.equals("or") ) {			
			AbstractBooleanExpression a = createExpression( getChildElementByIndex(n, 0) );
			AbstractBooleanExpression b = createExpression( getChildElementByIndex(n, 1) );
			if(a != null && b != null){
				return Or.create(a, b);
			}
		}
		else if( name.equals("not") ) {			
			AbstractBooleanExpression sub = createExpression( getChildElementByIndex(n, 0) );
			if(sub != null){
				return Not.create(sub);
			}
		}
		// TODO refernce2
		else if( isAttribute(n, "typ", "Bool") ) {
			Identifier<Boolean> id = ReadXML2.<Boolean>createSimpleAttribute(name, "Bool");
			_Variable<Boolean> var = _Variable.<Boolean>create(id);
			if(var != null){
				Boolean v = Bool.getTrans( n.getNodeValue() );
				if( v != null ){
					try {
						var.set(v);
					} catch (ValueException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
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
				if( name.equals("copy") ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									AbstractAssigment<?> x = createAssignment("copy", super.node, c);
									if(x != null){
										Program.steps.add(x);										
									}
								}
							}
						);
				}
				else if( name.equals("ref") ) {
					functionBuilders.add( 
							new FunctionBuilder(c){
								@Override
								public void build(Complex c) {
									AbstractAssigment<?> x = createAssignment("ref", super.node, c);
									if(x != null){
										Program.steps.add(x);										
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
		Node dataTypNode = attributes.getNamedItem( "typ" );
		
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
			} catch (ValueException e) {
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
		} catch (ValueException e) {
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
				v.transSet(value);
			} catch (ValueTransformException | ValueException e) {
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
		} catch (ValueException e) {
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
