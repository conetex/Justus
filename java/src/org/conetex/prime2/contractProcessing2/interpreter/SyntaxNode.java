package org.conetex.prime2.contractProcessing2.interpreter;

import java.util.List;

import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.study.ReadXMLtools;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SyntaxNode {
	
	private String name;
	
	private String nameAttribute;
	
	private String value;
	
	private String type;

	private List<SyntaxNode> children;

	public static SyntaxNode create(String theName, String theNameAttribute, String theValue, String theType){
		if(theName == null || theName.length() == 0){
			return null;
		}
		return new SyntaxNode(theName, theNameAttribute, theValue, theType, null);
	}
	
	public static SyntaxNode create(String theName, String theNameAttribute, String theValue, String theType, List<SyntaxNode> theChildren){
		if(theName == null || theName.length() == 0){
			return null;
		}
		if(theChildren == null || theChildren.size() == 0){
			return null;
		}
		return new SyntaxNode(theName, theNameAttribute, theValue, theType, theChildren);
	}
	
	private SyntaxNode(String theName, String theNameAttribute, String theValue, String theType, List<SyntaxNode> theChildren){
		this.name = theName;
		this.nameAttribute = theNameAttribute;
		this.value = theValue;
		this.type = theType;
		this.children = theChildren;
	}
	

	
	
	public String getNodeName(){
		return this.name;
	}
	
	public String getNameAttribute(){
		return this.nameAttribute;
	}
	
	public String getNodeValue(){
		return this.value;
	}
	
	public String getNodeType(){
		return this.type;
	}
	
	public boolean isType(){
		if( this.name.equals(Symbol.COMPLEX) || this.name.equals(Symbol.FUNCTION) ) {
//System.out.println("isType Y " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );		
			return true;
		}
//System.out.println("isType N " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );		
		return false;
	}

	
	
	public boolean isIdentifier(){
		if( this.name.equals(Symbol.IDENTIFIER) ) {
			return true;
		}
		return false;
	}
	
	public boolean isFunction(){
		if(    this.name.equals(Symbol.PLUS) || this.name.equals(Symbol.MINUS) || this.name.equals(Symbol.TIMES) || this.name.equals(Symbol.DIVIDED_BY) || this.name.equals(Symbol.REMAINS)
			|| this.name.equals(Symbol.SMALLER) || this.name.equals(Symbol.GREATER) || this.name.equals(Symbol.EQUAL)
			|| this.name.equals(Symbol.AND) || this.name.equals(Symbol.OR) || this.name.equals(Symbol.XOR) || this.name.equals(Symbol.NOT) 
			|| this.name.equals(Symbol.REFERENCE) || this.name.equals(Symbol.COPY)
			|| this.name.equals(Symbol.FUNCTION) || this.name.equals(Symbol.RETURN) || this.name.equals(Symbol.CALL)
		  ) {
			return true;
		}
		return false;
	}
	
	public boolean isValue(){
		if( this.name.equals(Symbol.IDENTIFIER) ) {
			String valueNode = this.value;
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
			if( this.name.equals(Symbol.FUNCTION) ){
				//System.out.println("isValue Y " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return true;
			}
			else if( this.isType() || this.isFunction() ){
				//System.out.println("isValue N " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return false;
			}
			else{
				//System.out.println("isValue Y " + name + " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) );	
				return true;				
			}
				
		}
	}
	
	public SyntaxNode getChildElementByIndex(int index){
		if(this.children == null){
			return null;
		}
		if(index >= 0 && index < this.children.size()){
			return this.children.get(index);			
		}
		return null;
	}

	public List<SyntaxNode> getChildNodes() {
		return this.children;
	}
	
}
