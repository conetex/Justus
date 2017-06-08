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
import org.conetex.prime2.contractProcessing.State.ValueException;
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
		List<State> res = parseStates(children, " ", reStr);
				
		System.out.println(reStr);
		
	}
	
	public static List<State> parseStates(NodeList children, String indent, String reStr){
		String reString = "";
		List<State> re = new LinkedList<State>();
		for(int i = 0; i < children.getLength(); i++){
			Node n = children.item(i);
			short typ = n.getNodeType();
			System.out.println(n.getNodeName() + " - " + typ);
			if(typ == Node.ELEMENT_NODE){
				State resultPart = parseState( n, indent, reString );
				if(resultPart != null){
					re.add(resultPart);					
				}
			}
			/*
			else if(typ == Node.DOCUMENT_TYPE_NODE){
				String resultPart = parseDocumentType( n, indent );
				if(resultPart != null){
					re.add(resultPart);					
				}
			}			
			else if(typ == Node.ENTITY_REFERENCE_NODE){
				System.out.println("ENTITY_REFERENCE_NODE");
			}
			else if(typ == Node.ENTITY_NODE){
				System.out.println("ENTITY_NODE");
			}	
			*/
		}
		return re;
	}
	
	public static List<Attribute<?>> parseAttributes(NodeList children, String indent){
		String reString = "";
		List<Attribute<?>> re = new LinkedList<Attribute<?>>();
		for(int i = 0; i < children.getLength(); i++){
			Node n = children.item(i);
			short typ = n.getNodeType();
			System.out.println(n.getNodeName() + " - " + typ);
			if(typ == Node.ELEMENT_NODE){
				Attribute<?> resultPart = parseAttribute( n, indent, reString );
				if(resultPart != null){
					re.add(resultPart);					
				}
			}				
		}
		return re;
	}	
		
	public static String getNodeAttribute(Node n, String attributeName){
		NamedNodeMap attributes = n.getAttributes();
		Node dataTypNode = attributes.getNamedItem( attributeName );
		if(dataTypNode != null){
			return dataTypNode.getNodeValue();
		}		
		return null;
	}
	
	public static Attribute<?> createAttribute(String name, String type){
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
	
	public static State parseState(Node n, String indent, String reStr){
		
		String name = n.getNodeName() + " (local: " + n.getLocalName() + ")";
				
		NodeList children = n.getChildNodes();
		int countOfChildren = children.getLength();
		
		// Complex
		List<Attribute<?>> attributes = parseAttributes(children, indent + " ");
		if(! attributes.isEmpty()){
	
			Attribute<?>[] theOrderedAttributes = new Attribute<?>[ attributes.size() ];
			attributes.toArray( theOrderedAttributes );
			
			ComplexDataType complexType = null;
			try {
				complexType = State.createComplexDataType(theOrderedAttributes);
			} catch (DuplicateAttributeNameExeption | NullAttributeException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			
			
			//return re;	
			return null;
		}
		else{
			// TODO: parse Error
			System.out.println("      " + n.getNodeName() + " Error no children ");
			return null;
		}
			
		
	}	
	
	public static Attribute<?> parseAttribute(Node n, String indent, String reStr){
		
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
				reStr = reStr + indent + "parsed Obj: " + name + " = " + value + " (" + type + ")";
				return createAttribute(name, type); //
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
				reStr = reStr + indent + "parsed Obj: " + name + " = " + value + " (" + type + ")";
				return createAttribute(name, type);
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
	
	
	
}
