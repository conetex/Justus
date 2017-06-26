package org.conetex.prime2.study;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing2.data.Identifier;
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateAttributeNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.EmptyLabelException;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullLabelException;
import org.conetex.prime2.contractProcessing2.data.Structure;
import org.conetex.prime2.contractProcessing2.data.Type;
import org.conetex.prime2.contractProcessing2.data.Type.ComplexDataType;
import org.conetex.prime2.contractProcessing2.data.Type.PrimitiveDataType;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Value.Implementation.*;
import org.conetex.prime2.contractProcessing2.data.Value.ValueException;
import org.conetex.prime2.contractProcessing2.data.Value.ValueTransformException;
import org.conetex.prime2.contractProcessing2.lang.BoolExpression;
import org.conetex.prime2.contractProcessing2.lang.Variable;
import org.conetex.prime2.contractProcessing2.lang.boolExpression.And;
import org.conetex.prime2.contractProcessing2.lang.boolExpression.BooleanVar;
import org.conetex.prime2.contractProcessing2.lang.boolExpression.Not;
import org.conetex.prime2.contractProcessing2.lang.boolExpression.Or;
import org.conetex.prime2.contractProcessing2.runtime.Heap;
import org.conetex.prime2.study_contractProcessing.Data;
import org.conetex.prime2.study_contractProcessing.Data.*;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	             + "  <a2Address typ='MailAddress64'>ab@cdefg.com</a2Address>"
	             + "  <copy>"
	             + "    <source>aAddress</source>"
	             + "    <target>a2Address</target>"
	             + "  </copy>"
	             + "  <And>"
	             + "    <a><v typ='Bool'>true</v></a>"	             
	             + "    <b><v typ='Bool'>false</v></b>"	             
	             + "  </And>"
	             + "</cred>                " 
				;
		InputStream is = new ByteArrayInputStream( xml.getBytes(StandardCharsets.UTF_8) );
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );
		//String usr = document.getElementsByTagName("user").item(0).getTextContent();
		//String pwd = document.getElementsByTagName("password").item(0).getTextContent();
		
		Structure root = null;
		NodeList children = document.getChildNodes();			
		for(int i = 0; i < children.getLength(); i++){	
			Node c = children.item(i);
			short typOfNode = children.item(i).getNodeType();
			if(typOfNode == Node.ELEMENT_NODE){
				if(root == null){
					root = createState( c );
				}
				else{
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}
		
		Heap heap = Heap.create(root);
		
		
		//StringTokenizer st = new StringTokenizer("xyz", Label.NAME_SEPERATOR);
		//while(st.hasMoreTokens()){
			//System.out.println(st.nextToken());
		//}
		
		//String[] result = "this.is.a.test".split("\\.");
		// for (int x=0; x<result.length; x++)
		//     System.out.println(result[x]);
		
	    String x = "this";

	    
		
		//List<Attribute<?>> res = parseAttributes(children.item(0));
				
		System.out.println("asdfasdf");
		
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
	
	
	public static BoolExpression createExpression(Node n){
		if(n == null){
			return null;
		}
	
		String name = n.getNodeName();
		if( name.equals("and") ) {			
			BoolExpression a = createExpression( getChildElementByIndex(n, 0) );
			BoolExpression b = createExpression( getChildElementByIndex(n, 1) );
			if(a != null && b != null){
				return And.create(a, b);
			}
		}
		else if( name.equals("or") ) {			
			BoolExpression a = createExpression( getChildElementByIndex(n, 0) );
			BoolExpression b = createExpression( getChildElementByIndex(n, 1) );
			if(a != null && b != null){
				return Or.create(a, b);
			}
		}
		else if( name.equals("not") ) {			
			BoolExpression sub = createExpression( getChildElementByIndex(n, 0) );
			if(sub != null){
				return Not.create(sub);
			}
		}
		else if( isAttribute(n, "typ", "Bool") ) {
			Identifier<Boolean> id = ReadXML2.<Boolean>createSimpleAttribute(name, "Bool");
			Variable<Boolean> var = Variable.<Boolean>create(id);
			if(var != null){
				try {
					var.transSet(n.getNodeValue());
				} catch (DOMException | ValueTransformException | ValueException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	public static Structure createState(Node n){
		
		NodeList children = n.getChildNodes();
		List<Identifier<?>> attributes = new LinkedList<Identifier<?>>();
		List<Value.Interface<?>> values = new LinkedList<Value.Interface<?>>();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			short type = c.getNodeType();
			System.out.println(c.getNodeName() + " - " + type);
			if(type == Node.ELEMENT_NODE){
				createAttributesValues( c, attributes, values );
			}				
		}		
		
		Identifier<?>[] theOrderedAttributes = new Identifier<?>[ attributes.size() ];
		attributes.toArray( theOrderedAttributes );
		
		Value.Interface<?>[] theValues = new Value.Interface<?>[ values.size() ];
		values.toArray( theValues );		
		
		ComplexDataType complexType = null;
		try {
			complexType = Type.ComplexDataType.createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | Identifier.NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
		
		if(complexType != null){
			return complexType.construct(theValues);			
		}
		
		return null;
		
	}
	
	public static boolean createAttributesValues(Node n, List<Identifier<?>> dattributes, List<Value.Interface<?>> values){
		
		String name = n.getNodeName();// + " (local: " + n.getLocalName() + ")";
		
		NamedNodeMap attributes = n.getAttributes();
		Node dataTypNode = attributes.getNamedItem( "typ" );
		
		Node valueNode = null;		
		if(dataTypNode != null){			
			NodeList children = n.getChildNodes();
			int countOfChildren = children.getLength();
			if(countOfChildren == 0){
				// Primitiv	...
				valueNode = attributes.getNamedItem("value");
			}
			else if(countOfChildren == 1){
				Node valueNodeX = children.item(0);
				short valueNodeType = valueNodeX.getNodeType();
				if(valueNodeType == Node.TEXT_NODE){		
					// Primitiv	...
					valueNode = valueNodeX;
				}
				// Complex ...
			}	
			if(valueNode != null){
				// ... Primitiv	
				String type = dataTypNode.getNodeValue();
				String value = valueNode.getNodeValue();
				Identifier<?> attribute = createSimpleAttribute(name, type); //
				if(attribute != null){
					Value.Interface<?> v = createSimpleValue(attribute, value);
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
		Identifier<Structure> attribute = createComplexAttribute(name); //
		if(attribute != null){
			Structure s = createState(n);
			if(s != null){
				Value.Interface<Structure> v = createComplexValue(attribute, s);
				if(v != null){
					dattributes.add(attribute);
					values.add(v);						
					return true;
				}
			}
		}
		return false;			
				
	}
	

	
	public static Value.Interface<Structure> createComplexValue(Identifier<Structure> attribute, Structure value){
		//new PrimitiveDataType< Complex  , State >  ( Complex.class  , new ValueFactory<State>()   { public Complex   createValueImp() { return new Complex()  ; } } )
		if(attribute != null){
			Value.Interface<Structure> v = attribute.createValue();
			try {
				v.set(value);
			} catch (Value.ValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return v;
		}		
		
		return null;
	}	
	
	public static Identifier<Structure> createComplexAttribute(String name){
		//PrimitiveDataType<?, ?> simpleType = PrimitiveDataType.getInstance( type );	
		PrimitiveDataType<Structure> simpleType = PrimitiveDataType.getInstance( Value.Implementation.Struct.class.getSimpleName() );
		ASCII8 str = new ASCII8(); 
		try {
			str.set(name);
		} catch (Value.ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Identifier<Structure> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}	
		
		return attribute;
		
	}	
	
	public static <T> Value.Interface<T> createSimpleValue(Identifier<T> attribute, String value){
		
		if(attribute != null){
			Value.Interface<T> v = attribute.createValue();
			try {
				v.transSet(value);
			} catch (Value.ValueTransformException | Value.ValueException e) {
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
		PrimitiveDataType<T> simpleType = PrimitiveDataType.getInstance( type );				
		ASCII8 str = new ASCII8(); 
		try {
			str.set(name);
		} catch (Value.ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Identifier<T> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}	
		
		return attribute;
		
	}		
	
}
