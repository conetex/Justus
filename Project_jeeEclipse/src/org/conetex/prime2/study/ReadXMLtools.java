package org.conetex.prime2.study;

import java.io.FileInputStream;
import java.io.IOException;
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
import org.conetex.prime2.contractProcessing2.data.Identifier.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.Identifier.NullIdentifierException;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Complex.ComplexWasInitializedExeption;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXMLtools {
	
	public static String getAttribute(Node n, String a){
		NamedNodeMap attributes = n.getAttributes();
		Node attributeNode = attributes.getNamedItem( a );			
		if(attributeNode != null){
			return attributeNode.getNodeValue();
		}
		return null;
	}
	
	public static String getNodeValue(Node n){
		String re = ReadXMLtools.getAttribute(n, Symbol.VALUE);
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
	
	public static boolean isType(Node n){
		String name = n.getNodeName();
		if( name.equals(Symbol.COMPLEX) || name.equals(Symbol.FUNCTION) ) {
			return true;
		}
		return false;
	}
	
	public static boolean isIdentifier(Node n){
		String name = n.getNodeName();
		if( name.equals(Symbol.IDENTIFIER) ) {
			return true;
		}
		return false;
	}	
	
	public static boolean isValue(Node n){
		return n.getNodeType() == Node.ELEMENT_NODE && !isType(n) && !isIdentifier(n);
	}
	
	public static String getRootType(Node n) {
		return ReadXMLtools.getAttribute(n, Symbol.TYPE_NAME);
	}
}
