package org.conetex.justus.study;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.conetex.contract.data.type.Complex;
import com.conetex.contract.data.valueImplement.exception.Invalid;
import com.conetex.contract.interpreter.Build;
import com.conetex.contract.interpreter.CodeNode;
import com.conetex.contract.interpreter.exception.FunctionNotFound;
import com.conetex.contract.interpreter.exception.MissingSubOperation;
import com.conetex.contract.interpreter.exception.NoAccessToValue;
import com.conetex.contract.interpreter.exception.OperationInterpreterException;
import com.conetex.contract.interpreter.exception.TypeNotDeterminated;
import com.conetex.contract.interpreter.exception.TypesDoNotMatch;
import com.conetex.contract.interpreter.exception.UnexpectedSubOperation;
import com.conetex.contract.interpreter.exception.UnknownComplexType;
import com.conetex.contract.lang.Symbol;

public class ReadXML {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException, Invalid, OperationInterpreterException {

		FileInputStream is = new FileInputStream("input2.xml");

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(is);

		List<Complex> complexTyps = null;
		// List<Value<?>> values = null;
		// List<Accessible<?>> functions = null;

		NodeList children = document.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node r = children.item(i);
			short typOfNode = children.item(i).getNodeType();
			if (typOfNode == Node.ELEMENT_NODE) {
				if (complexTyps == null) {
					CodeNode r2 = createSyntaxNode(r);
					complexTyps = Build.create(r2);
				} else {
					System.err.println("more than one root element! can not proceed!");
				}
			}
		}

	}

	public static CodeNode createSyntaxNode(Node n) {

		short typOfNode = n.getNodeType();
		if (typOfNode != Node.ELEMENT_NODE) {
			return null;
		}

		String name = n.getNodeName();
		String nameAttr = ReadXMLtools.getAttribute(n, Symbol.NAME);
		String value = ReadXMLtools.getNodeValue(n);
		String type = ReadXMLtools.getAttribute(n, Symbol.TYPE);
		List<CodeNode> children = new ArrayList<CodeNode>();

		NodeList xmlChildren = n.getChildNodes();
		for (int i = 0; i < xmlChildren.getLength(); i++) {
			Node c = xmlChildren.item(i);
			CodeNode child = createSyntaxNode(c);
			if (child != null) {
				children.add(child);
			}
		}

		if (children.size() > 0) {
			return CodeNode.create(name, nameAttr, value, type, children);
		} else {
			return CodeNode.create(name, nameAttr, value, type);
		}

	}

}
