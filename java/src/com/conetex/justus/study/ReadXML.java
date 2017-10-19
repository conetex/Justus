package com.conetex.justus.study;

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

import com.conetex.contract.build.Build;
import com.conetex.contract.build.Build.Main;
import com.conetex.contract.build.CodeNode;
import com.conetex.contract.build.Symbol;
import com.conetex.contract.build.exceptionLang.AbstractInterpreterException;
import com.conetex.contract.build.exceptionLang.UnknownCommand;
import com.conetex.contract.build.exceptionLang.UnknownCommandParameter;
import com.conetex.contract.run.exceptionValue.AbstractRuntimeException;

public class ReadXML {

	public static void main(String[] args)
			throws ParserConfigurationException, SAXException, IOException, AbstractInterpreterException, AbstractRuntimeException {

		Main main = null;

		try (FileInputStream is = new FileInputStream("input2.xml")) {

			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(is);

			// List<Complex> complexTyps_ = null;
			// List<Value<?>> values = null;
			// List<Accessible<?>> functions = null;

			NodeList children = document.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				Node r = children.item(i);
				short typOfNode = children.item(i).getNodeType();
				if (typOfNode == Node.ELEMENT_NODE) {
					if (main == null) {
						CodeNode r2 = createSyntaxNode(r);
						main = Build.create(r2);
					}
					else {
						System.err.println("more than one root element! can not proceed!");
					}
				}
			}

			is.close();
		}

		if (main != null) {
			main.run();
		}

	}

	public static CodeNode createSyntaxNode(Node n) throws UnknownCommandParameter, UnknownCommand {

		short typOfNode = n.getNodeType();
		if (typOfNode != Node.ELEMENT_NODE) {
			return null;
		}

		String name = n.getNodeName();
		String nameAttr = ReadXMLtools.getAttribute(n, Symbol.NAME);
		String value = ReadXMLtools.getNodeValue(n);
		String type = ReadXMLtools.getAttribute(n, Symbol.TYPE);
		List<CodeNode> children = new ArrayList<>();

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
		}
		else {
			return CodeNode.create(name, nameAttr, value, type);
		}

	}

}
