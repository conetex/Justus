package com.conetex.contract.interpreter;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.lang.Symbol;

public class CodeNode {

	private String command;

	private String name;

	private String value;

	private String type;

	private List<CodeNode> children;

	public static CodeNode create(String theName, String theNameAttribute, String theValue, String theType) {
		if (theName == null || theName.length() == 0) {
			return null;
		}
		return new CodeNode(theName, theNameAttribute, theValue, theType, new LinkedList<CodeNode>());
	}

	public static CodeNode create(String theName, String theNameAttribute, String theValue, String theType,
			List<CodeNode> theChildren) {
		if (theName == null || theName.length() == 0) {
			return null;
		}
		if (theChildren == null || theChildren.size() == 0) {
			return null;
		}
		return new CodeNode(theName, theNameAttribute, theValue, theType, theChildren);
	}

	private CodeNode(String theName, String theNameAttribute, String theValue, String theType,
			List<CodeNode> theChildren) {
		this.command = theName;
		this.name = theNameAttribute;
		this.value = theValue;
		this.type = theType;
		this.children = theChildren;
	}

	public String getCommand() {
		return this.command;
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	public String getType() {
		return this.type;
	}

	public boolean isType() {
		if (this.command.equals(Symbol.COMPLEX) || this.command.equals(Symbol.FUNCTION)) {
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

	public boolean isIdentifier() {
		if (this.command.equals(Symbol.ATTRIBUTE) || this.command.equals(Symbol.VALUE)) {
			return true;
		}
		return false;
	}

	public boolean isBuildInFunction() {
		if (this.command.equals(Symbol.PLUS) || this.command.equals(Symbol.MINUS) || this.command.equals(Symbol.TIMES)
				|| this.command.equals(Symbol.DIVIDED_BY) || this.command.equals(Symbol.REMAINS)
				|| this.command.equals(Symbol.SMALLER) || this.command.equals(Symbol.GREATER) || this.command.equals(Symbol.EQUAL)
				|| this.command.equals(Symbol.AND) || this.command.equals(Symbol.OR) || this.command.equals(Symbol.XOR)
				|| this.command.equals(Symbol.NOT) || this.command.equals(Symbol.REFERENCE) || this.command.equals(Symbol.COPY)
				|| this.command.equals(Symbol.FUNCTION) || this.command.equals(Symbol.RETURN) || this.command.equals(Symbol.CALL)) {
			return true;
		}
		return false;
	}

	public boolean isValue() {
		if (this.command.equals(Symbol.VALUE)) {
			// System.out.println("isValue Y " + name + " - " +
			// ReadXMLtools.getAttribute(n,
			// Symbol.IDENTIFIER_NAME) );
			return true;
		}
		/*
		 * else if( this.tag.equals(Symbol.IDENTIFIER) ) { String valueNode =
		 * this.value; if(valueNode == null){ //System.out.println("isValue N "
		 * + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME)
		 * ); return false; } else{ //System.out.println("isValue Y " + name +
		 * " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );
		 * return true; } }
		 */
		else {
			if (this.command.equals(Symbol.FUNCTION)) {
				// System.out.println("isValue Y " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return true;
			} else if (this.command.equals(Symbol.ATTRIBUTE) || this.isType() || this.isBuildInFunction()) {
				// System.out.println("isValue N " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return false;
			} else {
				// System.out.println("isValue Y " + name + " - " +
				// ReadXMLtools.getAttribute(n,
				// Symbol.IDENTIFIER_NAME) );
				return true;
			}

		}
	}

	public CodeNode getChildElementByIndex(int index) {
		if (this.children == null) {
			return null;
		}
		if (index >= 0 && index < this.children.size()) {
			return this.children.get(index);
		}
		return null;
	}

	public List<CodeNode> getChildNodes() {
		return this.children;
	}

	public int getChildNodesSize() {
		if (this.children == null) {
			return 0;
		}
		return this.children.size();
	}

}
