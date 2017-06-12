package org.conetex.prime2.study;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing.State;
import org.conetex.prime2.contractProcessing.State.Attribute;
import org.conetex.prime2.contractProcessing.State.ComplexDataType;
import org.conetex.prime2.contractProcessing.State.DuplicateAttributeNameExeption;
import org.conetex.prime2.contractProcessing.State.EmptyLabelException;
import org.conetex.prime2.contractProcessing.State.NullAttributeException;
import org.conetex.prime2.contractProcessing.State.NullLabelException;
import org.conetex.prime2.contractProcessing.State.PrimitiveDataType;
import org.conetex.prime2.contractProcessing.State.Value;
import org.conetex.prime2.contractProcessing.State.ValueException;

import org.conetex.prime2.contractProcessing.State.ValueTransformException;
import org.conetex.prime2.contractProcessing.Types.ASCII8;
import org.conetex.prime2.contractProcessing.Types.Base64_256;
import org.conetex.prime2.contractProcessing.Types.Complex;
import org.conetex.prime2.contractProcessing.Types.MailAddress64;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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
	             + "  <Xvalue typ='MailAddress64'>12@32543.com</Xvalue>"
	             + "</cred>                " 
				;
		InputStream is = new ByteArrayInputStream( xml.getBytes(StandardCharsets.UTF_8) );
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );
		//String usr = document.getElementsByTagName("user").item(0).getTextContent();
		//String pwd = document.getElementsByTagName("password").item(0).getTextContent();
		
		List<State> re = new LinkedList<State>() ;
		NodeList children = document.getChildNodes();			
		for(int i = 0; i < children.getLength(); i++){	
			State x = createState( children.item(i) );
			if(x != null){
				re.add(x);				
			}
		}
		
		//List<Attribute<?>> res = parseAttributes(children.item(0));
				
		System.out.println("asdfasdf");
		
	}

	

	

	
	
	public static State createState(Node n){
				
		NodeList children = n.getChildNodes();
		List<Attribute<?>> attributes = new LinkedList<Attribute<?>>();
		List<Value<?>> values = new LinkedList<Value<?>>();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			short type = c.getNodeType();
			System.out.println(c.getNodeName() + " - " + type);
			if(type == Node.ELEMENT_NODE){
				createAttributesValues( c, attributes, values );
			}				
		}		
		
		Attribute<?>[] theOrderedAttributes = new Attribute<?>[ attributes.size() ];
		attributes.toArray( theOrderedAttributes );
		
		Value<?>[] theValues = new Value<?>[ values.size() ];
		values.toArray( theValues );		
		
		ComplexDataType complexType = null;
		try {
			complexType = State.createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
		
		if(complexType != null){
			return complexType.createState(theValues);			
		}
		
		return null;
		
	}
	
	public static boolean createAttributesValues(Node n, List<Attribute<?>> dattributes, List<Value<?>> values){
		
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
				Attribute<?> attribute = createSimpleAttribute(name, type); //
				if(attribute != null){
					Value<?> v = createSimpleValue(attribute, value);
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
		Attribute<State> attribute = createComplexAttribute(name); //
		if(attribute != null){
			State s = createState(n);
			if(s != null){
				Value<State> v = createComplexValue(attribute, s);
				if(v != null){
					dattributes.add(attribute);
					values.add(v);						
					return true;
				}
			}
		}
		return false;			
				
	}
	

	
	public static Value<State> createComplexValue(Attribute<State> attribute, State value){
		//new PrimitiveDataType< Complex  , State >  ( Complex.class  , new ValueFactory<State>()   { public Complex   createValueImp() { return new Complex()  ; } } )
		if(attribute != null){
			Value<State> v = attribute.createValue();
			try {
				v.set(value);
			} catch (ValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return v;
		}		
		
		return null;
	}	
	
	public static Attribute<State> createComplexAttribute(String name){
		//PrimitiveDataType<?, ?> simpleType = PrimitiveDataType.getInstance( type );		
		PrimitiveDataType<Value<State>, State> simpleType = PrimitiveDataType.getInstance( "Complex" );
		ASCII8 str = new ASCII8(); 
		try {
			str.set(name);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Attribute<State> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}	
		
		return attribute;
		
	}	
	
	public static Value<?> createSimpleValue(Attribute<?> attribute, String value){
		
		if(attribute != null){
			Value<?> v = attribute.createValue();
			try {
				v.transSet(value);
			} catch (ValueTransformException | ValueException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			return v;
		}		
		
		return null;
	}	

	public static Attribute<?> createSimpleAttribute(String name, String type){
		PrimitiveDataType<?, ?> simpleType = PrimitiveDataType.getInstance( type );				
		ASCII8 str = new ASCII8(); 
		try {
			str.set(name);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		Attribute<?> attribute = null;
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
