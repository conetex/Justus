package com.conetex.contract.interpreter;

import java.util.LinkedList;
import java.util.List;

import com.conetex.contract.lang.Symbol;

public class SyntaxNode {

    private String tag;

    private String name;

    private String value;

    private String type;

    private List<SyntaxNode> children;

    public static SyntaxNode create(String theName, String theNameAttribute, String theValue, String theType) {
        if (theName == null || theName.length() == 0) {
            return null;
        }
        return new SyntaxNode(theName, theNameAttribute, theValue, theType, new LinkedList<SyntaxNode>());
    }

    public static SyntaxNode create(String theName, String theNameAttribute, String theValue, String theType,
            List<SyntaxNode> theChildren) {
        if (theName == null || theName.length() == 0) {
            return null;
        }
        if (theChildren == null || theChildren.size() == 0) {
            return null;
        }
        return new SyntaxNode(theName, theNameAttribute, theValue, theType, theChildren);
    }

    private SyntaxNode(String theName, String theNameAttribute, String theValue, String theType,
            List<SyntaxNode> theChildren) {
        this.tag = theName;
        this.name = theNameAttribute;
        this.value = theValue;
        this.type = theType;
        this.children = theChildren;
    }

    public String getTag() {
        return this.tag;
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
        if (this.tag.equals(Symbol.COMPLEX) || this.tag.equals(Symbol.FUNCTION)) {
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
        if (this.tag.equals(Symbol.ATTRIBUTE) || this.tag.equals(Symbol.VALUE)) {
            return true;
        }
        return false;
    }

    public boolean isBuildInFunction() {
        if (this.tag.equals(Symbol.PLUS) || this.tag.equals(Symbol.MINUS) || this.tag.equals(Symbol.TIMES)
                || this.tag.equals(Symbol.DIVIDED_BY) || this.tag.equals(Symbol.REMAINS)
                || this.tag.equals(Symbol.SMALLER) || this.tag.equals(Symbol.GREATER) || this.tag.equals(Symbol.EQUAL)
                || this.tag.equals(Symbol.AND) || this.tag.equals(Symbol.OR) || this.tag.equals(Symbol.XOR)
                || this.tag.equals(Symbol.NOT) || this.tag.equals(Symbol.REFERENCE) || this.tag.equals(Symbol.COPY)
                || this.tag.equals(Symbol.FUNCTION) || this.tag.equals(Symbol.RETURN) || this.tag.equals(Symbol.CALL)) {
            return true;
        }
        return false;
    }

    public boolean isValue() {
        if (this.tag.equals(Symbol.VALUE)) {
            // System.out.println("isValue Y " + name + " - " +
            // ReadXMLtools.getAttribute(n,
            // Symbol.IDENTIFIER_NAME) );
            return true;
        }
        /*
         * else if( this.tag.equals(Symbol.IDENTIFIER) ) { String valueNode =
         * this.value; if(valueNode == null){ //System.out.println("isValue N " + name +
         * " - " + ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) ); return false;
         * } else{ //System.out.println("isValue Y " + name + " - " +
         * ReadXMLtools.getAttribute(n, Symbol.IDENTIFIER_NAME) ); return true; } }
         */
        else {
            if (this.tag.equals(Symbol.FUNCTION)) {
                // System.out.println("isValue Y " + name + " - " +
                // ReadXMLtools.getAttribute(n,
                // Symbol.IDENTIFIER_NAME) );
                return true;
            }
            else if (this.tag.equals(Symbol.ATTRIBUTE) || this.isType() || this.isBuildInFunction()) {
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

    public SyntaxNode getChildElementByIndex(int index) {
        if (this.children == null) {
            return null;
        }
        if (index >= 0 && index < this.children.size()) {
            return this.children.get(index);
        }
        return null;
    }

    public List<SyntaxNode> getChildNodes() {
        return this.children;
    }

}
