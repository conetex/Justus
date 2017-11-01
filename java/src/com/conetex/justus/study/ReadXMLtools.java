package com.conetex.justus.study;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.conetex.contract.build.Symbols;

public class ReadXMLtools {

	public static String getAttribute(Node n, String a) {
		NamedNodeMap attributes = n.getAttributes();
		Node attributeNode = attributes.getNamedItem(a);
		if (attributeNode != null) {
			return attributeNode.getNodeValue();
		}
		return null;
	}

	public static String getNodeValue(Node n) {
		String re = ReadXMLtools.getAttribute(n, Symbols.comValue());
		if (re != null) {
			return re;
		}
		return getNodeContent(n);
	}
	
	public static String getNodeContent(Node n) {
		NodeList children = n.getChildNodes();
		if (children != null && children.getLength() == 1) {
			Node textValueNode = children.item(0);
			short textValueNodeType = textValueNode.getNodeType();
			if (textValueNodeType == Node.TEXT_NODE) {
				return textValueNode.getNodeValue();
			}
		}
		return null;
	}

	public static boolean isType(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		}
		String name = n.getNodeName();
		if (name.equals(Symbols.comComplex()) || name.equals(Symbols.comFunction())) {
			// System.out.println("isType Y " + name + " - " +
			// ReadXMLtools.getAttribute(n,
			// Symbol.IDENTIFIER_NAME) );
			return true;
		}
		// System.out.println("isType N " + name + " - " +
		// ReadXMLtools.getAttribute(n,
		// Symbol.IDENTIFIER_NAME) );
		return false;
	}

	public static boolean isIdentifier(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		}
		String name = n.getNodeName();
		if (name.equals(Symbols.comAttribute())) {
			return true;
		}
		return false;
	}

	public static boolean isFunction(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		}
		String name = n.getNodeName();
		if (name.equals(Symbols.comPlus()) || name.equals(Symbols.comMinus()) || name.equals(Symbols.comTimes()) || name.equals(Symbols.comDividedBy()) || name.equals(Symbols.comRemains())
				|| name.equals(Symbols.comSmaller()) || name.equals(Symbols.comGreater()) || name.equals(Symbols.comEqual()) || name.equals(Symbols.comAnd()) || name.equals(Symbols.comOr())
				|| name.equals(Symbols.comXOr()) || name.equals(Symbols.comNot()) || name.equals(Symbols.comReference()) || name.equals(Symbols.comCopy())
				|| name.equals(Symbols.comFunction()) || name.equals(Symbols.comReturn()) || name.equals(Symbols.comCall())) {
			return true;
		}
		return false;
	}

	public static boolean isValue(Node n) {
		if (n.getNodeType() != Node.ELEMENT_NODE) {
			return false;
		}
		String name = n.getNodeName();
		if (name.equals(Symbols.comAttribute())) {
			String valueNode = ReadXMLtools.getNodeValue(n);
			if (valueNode == null) {
				// System.out.println("isValue N " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return false;
			}
			else {
				// System.out.println("isValue Y " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return true;
			}
		}
		else {
			if (name.equals(Symbols.comFunction())) {
				// System.out.println("isValue Y " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return true;
			}
			else if (isType(n) || isFunction(n)) {
				// System.out.println("isValue N " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return false;
			}
			else {
				// System.out.println("isValue Y " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return true;
			}

		}
	}

}
