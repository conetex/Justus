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
				
				 + "<credentials>  &js;               "
	             + "  <author>ein &js;</author>		  "
	             + "  <user>testusr</user>        "
	             + "  <password>testpwd</password>"
	             + "  <Xvalue typ='MailAddress64'>123</Xvalue>"
	             + "</credentials>                " 
				;
		InputStream is = new ByteArrayInputStream( xml.getBytes(StandardCharsets.UTF_8) );
		
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );
		//String usr = document.getElementsByTagName("user").item(0).getTextContent();
		//String pwd = document.getElementsByTagName("password").item(0).getTextContent();
		
		String reStr = "" ;
		NodeList children = document.getChildNodes();	
		List<Attribute<?>> res = parseAttributes(children.item(0));
				
		System.out.println(reStr);
		
	}

	
	public static List<Attribute<?>> parseAttributes(Node n){
		NodeList children = n.getChildNodes();
		List<Attribute<?>> attributes = new LinkedList<Attribute<?>>();
		List<Value<?>> values = new LinkedList<Value<?>>();
		for(int i = 0; i < children.getLength(); i++){
			Node c = children.item(i);
			short type = c.getNodeType();
			System.out.println(c.getNodeName() + " - " + type);
			if(type == Node.ELEMENT_NODE){
				Attribute<?> resultPart = parseAttribute( n, attributes, values );
			}				
		}
		
		Attribute<?>[] theOrderedAttributes = new Attribute<?>[ attributes.size() ];
		attributes.toArray( theOrderedAttributes );
		
		Value<?>[] theVals = new Value<?>[ values.size() ];
		values.toArray( theVals );		
		
		ComplexDataType complexType = null;
		try {
			complexType = State.createComplexDataType(theOrderedAttributes);
		} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		complexType.createState(theVals);
		
		
		return attributes;
	}	
	

	
	public static State parseState(Node n, String indent, String reStr){
		
		String name = n.getNodeName() + " (local: " + n.getLocalName() + ")";
				
		NodeList children = n.getChildNodes();
		int countOfChildren = children.getLength();
		
		// Complex
		List<Attribute<?>> attributes = parseAttributes(n);
		if(! attributes.isEmpty()){
	
			
			
			//return re;	
			return null;
		}
		else{
			// TODO: parse Error
			System.out.println("      " + n.getNodeName() + " Error no children ");
			return null;
		}
			
		
	}	
	
	public static Attribute<?> parseAttribute(Node n, List<Attribute<?>> dattributes, List<Value<?>> values){
		
		String name = n.getNodeName() + " (local: " + n.getLocalName() + ")";
		
		NamedNodeMap attributes = n.getAttributes();
		Node dataTypNode = attributes.getNamedItem( "typ" );
		if(dataTypNode == null){
			// TODO parse Error
			return null;
		}		
		String type = dataTypNode.getNodeValue();
		
		NodeList children = n.getChildNodes();
		int countOfChildren = children.getLength();
		if(countOfChildren == 1){
			Node valueNode = children.item(0);
			short valueNodeType = valueNode.getNodeType();
			if(valueNodeType == Node.TEXT_NODE){		
				// Primitiv	Attribute
				String value = valueNode.getNodeValue();
				// "parsed Obj: " + name + " = " + value + " (" + type + ")";
				createAttribute(name, type, value, dattributes, values); //

			}
			else{
				// Complex
			}				
		}
		else if(countOfChildren == 0){
			// Primitiv	
			Node valueNode = attributes.getNamedItem("value");
			if(valueNode != null){
				String value = valueNode.getNodeValue();
				// "parsed Obj: " + name + " = " + value + " (" + type + ")";
				createAttribute(name, type, value, dattributes, values); 
			}
			else{
				// TODO: parse Error
				System.out.println("      " + n.getNodeName() + " Error no value ");
				return null;			
			}
		}
		// Complex
		return createAttribute(name, "Complex");
	
				
	}
	
	public static void createAttribute(String name, String type, String value, List<Attribute<?>> dattributes, List<Value<?>> values){
		PrimitiveDataType<?, ?> simpleType = PrimitiveDataType.getInstance( type );				
		ASCII8 str = new ASCII8(); 
		try {
			str.set(name);
		} catch (ValueException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		Attribute<?> attribute = null;
		try {
			attribute = simpleType.createAttribute( str );
		} catch (NullLabelException | EmptyLabelException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return;
		}	
		
		if(attribute != null){
			Value<?> v = attribute.createValue();
			try {
				v.transSet(value);
			} catch (ValueException | NumberFormatException | ValueTransformException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			dattributes.add(attribute);
			values.add(v);
		}		
		
	}	
	
}
