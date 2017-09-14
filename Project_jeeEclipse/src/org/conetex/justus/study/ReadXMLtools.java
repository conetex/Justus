package org.conetex.justus.study;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.conetex.contract.lang.Symbol;

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
	
	public static String getRootType(Node n) {
		return ReadXMLtools.getAttribute(n, Symbol.TYPE_NAME);
	}
	
	public static boolean isType(Node n){
		if(n.getNodeType() != Node.ELEMENT_NODE){
			return false;
		}
		String name = n.getNodeName();
		if( name.equals(Symbol.COMPLEX) || name.equals(Symbol.FUNCTION) ) {
//System.out.println("isType Y " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );		
			return true;
		}
//System.out.println("isType N " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );		
		return false;
	}
	
	public static boolean isIdentifier(Node n){
		if(n.getNodeType() != Node.ELEMENT_NODE){
			return false;
		}
		String name = n.getNodeName();
		if( name.equals(Symbol.ATTRIBUTE) ) {
			return true;
		}
		return false;
	}	
	
	public static boolean isFunction(Node n) {
		if(n.getNodeType() != Node.ELEMENT_NODE){
			return false;
		}		
		String name = n.getNodeName();
		if(    name.equals(Symbol.PLUS) || name.equals(Symbol.MINUS) || name.equals(Symbol.TIMES) || name.equals(Symbol.DIVIDED_BY) || name.equals(Symbol.REMAINS)
			|| name.equals(Symbol.SMALLER) || name.equals(Symbol.GREATER) || name.equals(Symbol.EQUAL)
			|| name.equals(Symbol.AND) || name.equals(Symbol.OR) || name.equals(Symbol.XOR) || name.equals(Symbol.NOT) 
			|| name.equals(Symbol.REFERENCE) || name.equals(Symbol.COPY)
			|| name.equals(Symbol.FUNCTION) || name.equals(Symbol.RETURN) || name.equals(Symbol.CALL)
		  ) {
			return true;
		}
		return false;
	}	
	
	public static boolean isValue(Node n){
		if(n.getNodeType() != Node.ELEMENT_NODE){
			return false;
		}
		String name = n.getNodeName();
		if( name.equals(Symbol.ATTRIBUTE) ) {
			String valueNode = ReadXMLtools.getNodeValue(n);
			if(valueNode == null){
				//System.out.println("isValue N " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return false;
			}
			else{
				//System.out.println("isValue Y " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return true;
			}
		}
		else{
			if( name.equals(Symbol.FUNCTION) ){
				//System.out.println("isValue Y " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return true;
			}
			else if( isType(n) || isFunction(n) ){
				//System.out.println("isValue N " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return false;
			}
			else{
				//System.out.println("isValue Y " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return true;				
			}
				
		}
	}



}
