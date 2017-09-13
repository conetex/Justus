package org.conetex.prime2.study;

import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.conetex.prime2.contractProcessing2.data.Attribute;
import org.conetex.prime2.contractProcessing2.data.Value;
import org.conetex.prime2.contractProcessing2.data.Attribute.DuplicateIdentifierNameExeption;
import org.conetex.prime2.contractProcessing2.data.type.AbstractType;
import org.conetex.prime2.contractProcessing2.data.type.Complex;
import org.conetex.prime2.contractProcessing2.data.type.Primitive;
import org.conetex.prime2.contractProcessing2.data.type.Complex.DublicateComplexException;
import org.conetex.prime2.contractProcessing2.data.valueImplement.Structure;
import org.conetex.prime2.contractProcessing2.data.valueImplement.exception.Invalid;
import org.conetex.prime2.contractProcessing2.interpreter.SyntaxNode;
import org.conetex.prime2.contractProcessing2.lang.Accessible;
import org.conetex.prime2.contractProcessing2.lang.AccessibleConstant;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValue;
import org.conetex.prime2.contractProcessing2.lang.AccessibleValueNew;
import org.conetex.prime2.contractProcessing2.lang.SetableValue;
import org.conetex.prime2.contractProcessing2.lang.Symbol;
import org.conetex.prime2.contractProcessing2.lang.assignment.Copy;
import org.conetex.prime2.contractProcessing2.lang.assignment.Reference;
import org.conetex.prime2.contractProcessing2.lang.bool.expression.Comparison;
import org.conetex.prime2.contractProcessing2.lang.bool.expression._ComparisonNum;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Binary;
import org.conetex.prime2.contractProcessing2.lang.bool.operator.Not;
import org.conetex.prime2.contractProcessing2.lang.control.function.Call;
import org.conetex.prime2.contractProcessing2.lang.control.function.Function;
import org.conetex.prime2.contractProcessing2.lang.control.function.Return;
import org.conetex.prime2.contractProcessing2.lang.math.ElementaryArithmetic;
import org.conetex.prime2.contractProcessing2.runtime.Program;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ReadXML {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, Invalid {

		FileInputStream is = new FileInputStream( "input01.xml" );

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse( is );

		List<Complex> complexTyps = null;
		//List<Value<?>> values = null; 
		//List<Accessible<?>> functions = null; 		
		
		NodeList children = document.getChildNodes();			
		for(int i = 0; i < children.getLength(); i++){	
			Node r = children.item(i);
			short typOfNode = children.item(i).getNodeType();
			if(typOfNode == Node.ELEMENT_NODE){
				if(complexTyps == null){
					SyntaxNode r2 = createSyntaxNode(r);
					complexTyps = Builder.create(r2);
				}
				else{
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}
		
	}	
	
	public static SyntaxNode createSyntaxNode(Node n){
		
		short typOfNode = n.getNodeType();
		if(typOfNode != Node.ELEMENT_NODE){	
			return null;
		}
		
		String name = n.getNodeName();
		String nameAttr = ReadXMLtools.getAttribute(n, Symbol.NAME);
		String value = ReadXMLtools.getNodeValue(n);
		String type = ReadXMLtools.getAttribute(n, Symbol.TYPE);
		List<SyntaxNode> children = new ArrayList<SyntaxNode>();
		
		NodeList xmlChildren = n.getChildNodes();
		for(int i = 0; i < xmlChildren.getLength(); i++){
			Node c = xmlChildren.item(i);
			SyntaxNode child = createSyntaxNode(c);
			if(child != null){
				children.add(child);
			}
		}
		
		if(children.size() > 0){
			return SyntaxNode.create(name, nameAttr, value, type, children);			
		}
		else{
			return SyntaxNode.create(name, nameAttr, value, type);
		}
		
	}	
	
	
}
